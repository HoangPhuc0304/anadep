package com.hps.anadep.service.impl;

import com.hps.anadep.analyzer.client.GithubClient;
import com.hps.anadep.analyzer.service.UiService;
import com.hps.anadep.analyzer.service.impl.UiServiceImpl;
import com.hps.anadep.exception.NotFoundException;
import com.hps.anadep.model.SummaryLibraryFix;
import com.hps.anadep.model.entity.AuthToken;
import com.hps.anadep.model.entity.Repo;
import com.hps.anadep.model.entity.User;
import com.hps.anadep.model.enums.Ecosystem;
import com.hps.anadep.model.enums.GitHubAction;
import com.hps.anadep.model.github.*;
import com.hps.anadep.model.mapper.RepoMapper;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.SummaryFix;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.model.util.CustomMultipartFile;
import com.hps.anadep.repository.AuthTokenRepository;
import com.hps.anadep.repository.RepoRepository;
import com.hps.anadep.repository.UserRepository;
import com.hps.anadep.scanner.util.MavenTool;
import com.hps.anadep.scanner.util.NpmTool;
import com.hps.anadep.scanner.util.PackageManagementTool;
import com.hps.anadep.service.GitHubService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.hps.anadep.evaluator.service.impl.EvaluateServiceImpl.APPLY_TO_FIX;
import static com.hps.anadep.evaluator.service.impl.EvaluateServiceImpl.REFER_TO_FIX;

@Service
@Slf4j
public class GitHubServiceImpl implements GitHubService {
    @Autowired
    private GithubClient githubClient;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    private UiServiceImpl uiServiceImpl;

    @Value("${anadep.db.enable}")
    private Boolean anadepDbEnable;

    private static final String BRANCH_REF_FORMAT = "refs/heads/%s";
    private static final String BEFORE_FILE_FORMAT = "storage/fix/before/%s";
    private static final String AFTER_FILE_FORMAT = "storage/fix/after/%s";
    private static final String FILE_FORMAT = "%s/%s";
    private static final String FIX_BRANCH_FORMAT = "anadep-fix-%s";
    private static final String REF_BRANCH_PATTERN = "^refs/heads/.+";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String EXPIRES_IN = "expires_in";
    private static final String REFRESH_TOKEN_EXPIRES_IN = "refresh_token_expires_in";
    private static final String SCOPE = "scope";
    private static final String TOKEN_TYPE = "token_type";
    private static final String V1 = "V1";
    private static final String V2 = "V2";
    private static final String NAME_FIELD = "file";
    private static final String DEFAULT_ORIGIN_FILE = "source.zip";
    private static final String CONTENT_TYPE = "application/octet-stream";

    @Autowired
    public GitHubServiceImpl(@Lazy UiServiceImpl uiServiceImpl) {
        this.uiServiceImpl = uiServiceImpl;
    }

    @Override
    public AccessTokenResponse getToken(AccessTokenRequest accessTokenRequest) {
        String response = githubClient.getAccessToken(accessTokenRequest.getCode());
        return getAccessTokenResponse(response);
    }

    @Override
    public AccessTokenResponse getRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        String response = githubClient.getRefreshToken(refreshTokenRequest.getRefreshToken());
        return getAccessTokenResponse(response);
    }

    @Override
    public void createFixPullRequest(Repo repo, AnalysisUIResult analysisUIResult, ScanningResult scanningResult, SummaryFix summaryFix, String accessToken) throws IOException {
        Branch branch = getDefaultBranch(repo, accessToken);
        String fixBranchName = FIX_BRANCH_FORMAT.formatted(new Date().getTime());
        Branch fixBranch = createFixBranch(repo, branch, fixBranchName, accessToken);
        BranchContent branchContent = getBranchContent(repo, fixBranchName, accessToken, scanningResult);
        String encodedString = null;

        String uuid = UUID.randomUUID().toString();
        String fileName = FILE_FORMAT.formatted(uuid, branchContent.getName());
        String beforePath = BEFORE_FILE_FORMAT.formatted(uuid);
        String afterPath = AFTER_FILE_FORMAT.formatted(uuid);

        try {
            createContentFile(branchContent, fileName);
            createFixFile(summaryFix, fileName);
            log.info("Create fix file successfully to path: {}", AFTER_FILE_FORMAT.formatted(fileName));
            String afterFilePath = AFTER_FILE_FORMAT.formatted(fileName);
            byte[] fileContent = FileUtils.readFileToByteArray(new File(afterFilePath));
            encodedString = Base64.getEncoder().encodeToString(fileContent);
            updateFixBranch(repo, fixBranchName, branchContent, encodedString, accessToken, scanningResult);
            createPullRequest(repo, fixBranchName, branchContent, summaryFix, analysisUIResult, scanningResult, accessToken);
        } catch (Exception e) {
            log.error("Creating pull request to fix error with message: {}", e.getMessage());
        } finally {
            File beforeFile = new File(beforePath);
            File afterFile = new File(afterPath);
            FileUtils.deleteDirectory(beforeFile);
            FileUtils.deleteDirectory(afterFile);
        }
    }

    @Override
    public void handleDelivery(WebhookPayload webhookPayload, GitHubAction action) {
        log.info("A new event is triggered with action type: {}", action.getName());
        try {
            if (validateValidAction(webhookPayload, action)) {
                Long repoId = webhookPayload.getRepository().getId();
                Repo repo = repoRepository.findByGithubRepoId(repoId).get();
                Owner owner = webhookPayload.getSender();
                User user = userRepository.findByGithubUserId(owner.getId()).orElseThrow(
                        () -> new RuntimeException(String.format("The user with github id [%s] doesn't exist", owner.getId())));
                AuthToken authToken = authTokenRepository.findByUser(user).orElseThrow(
                        () -> new RuntimeException(String.format("The user with id [%s] doesn't have token", user.getId())));

                byte[] bytes = null;
                try {
                    bytes = githubClient.download(repo.getFullName(), authToken.getGithubToken());
                } catch (Exception e) {
                    RefreshTokenRequest request = new RefreshTokenRequest(authToken.getUser().getId(), authToken.getRefreshToken());
                    AccessTokenResponse response = getRefreshToken(request);
                    authToken.setGithubToken(response.getAccessToken());
                    authToken.setRefreshToken(response.getRefreshToken());
                    authTokenRepository.save(authToken);
                    bytes = githubClient.download(repo.getFullName(), authToken.getGithubToken());
                }

                String version;
                if (anadepDbEnable) {
                    version = V2;
                } else {
                    version = V1;
                }

                if (bytes != null) {
                    CustomMultipartFile customMultipartFile = new CustomMultipartFile(NAME_FIELD, DEFAULT_ORIGIN_FILE, CONTENT_TYPE, bytes);
                    AnalysisUIResult analysisUIResult = uiServiceImpl.getAnalysisUIResult(
                            customMultipartFile,
                            false,
                            repo,
                            version,
                            user.getId()
                    );
                }
            }
        } catch (Exception e) {
            log.error("Error handle delivery with massage: {}", e.getMessage());
        }
    }

    private AccessTokenResponse getAccessTokenResponse(String response) {
        if (!response.contains(ACCESS_TOKEN.concat("=")) || !response.contains(REFRESH_TOKEN.concat("="))) {
            throw new RuntimeException("Authorize failed");
        }

        String accessToken = Arrays.stream(response.split("&")).filter(s -> s.startsWith(ACCESS_TOKEN.concat("="))).findFirst().get().split("=")[1];
        String refreshToken = Arrays.stream(response.split("&")).filter(s -> s.startsWith(REFRESH_TOKEN.concat("="))).findFirst().get().split("=")[1];
        String expiresIn = Arrays.stream(response.split("&")).filter(s -> s.startsWith(EXPIRES_IN.concat("="))).findFirst().get().split("=")[1];
        String refreshTokenExpiresIn = Arrays.stream(response.split("&")).filter(s -> s.startsWith(REFRESH_TOKEN_EXPIRES_IN.concat("="))).findFirst().get().split("=")[1];
        String tokenType = Arrays.stream(response.split("&")).filter(s -> s.startsWith(TOKEN_TYPE.concat("="))).findFirst().get().split("=")[1];

        return AccessTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(Long.parseLong(expiresIn))
                .refreshTokenExpiresIn(Long.parseLong(refreshTokenExpiresIn))
                .scope("")
                .tokenType(tokenType)
                .build();
    }

    private boolean validateValidAction(WebhookPayload webhookPayload, GitHubAction action) {
        if (action.equals(GitHubAction.PUSH)) {
            Long repoId = webhookPayload.getRepository().getId();
            String ref = webhookPayload.getRef();

            Repo repo = repoRepository.findByGithubRepoId(repoId).orElse(null);
            if (repo != null && ref.matches(REF_BRANCH_PATTERN)) {
                String branch = ref.split("/")[2];
                if (branch.equals(webhookPayload.getRepository().getDefaultBranch())) {
                    BeanUtils.copyProperties(webhookPayload.getRepository(), repo, "id", "user", "owner");
                    repo.setGithubRepoId(webhookPayload.getRepository().getId());
                    repo.setOwner(webhookPayload.getRepository().getOwner().getLogin());
                    repo.setPublic(!webhookPayload.getRepository().isPrivate());
                    repoRepository.save(repo);
                    return true;
                }
            }
        }
        return false;
    }

    private Branch getDefaultBranch(Repo repo, String accessToken) {
        return githubClient.getBranch(repo.getFullName(), repo.getDefaultBranch(), accessToken);
    }

    private Branch createFixBranch(Repo repo, Branch branch, String fixBranchName, String accessToken) {
        BranchRequest branchRequest = BranchRequest.builder()
                .ref(BRANCH_REF_FORMAT.formatted(fixBranchName))
                .sha(branch.getObject().getSha())
                .build();
        return githubClient.createBranch(repo.getFullName(), branchRequest, accessToken);
    }

    private BranchContent getBranchContent(Repo repo, String fixBranchName, String accessToken, ScanningResult scanningResult) {
        return githubClient.getBranchContent(repo.getFullName(), fixBranchName, scanningResult.getPath(), accessToken);
    }

    private void updateFixBranch(Repo repo, String fixBranchName, BranchContent branchContent, String encodedString, String accessToken, ScanningResult scanningResult) {
        githubClient.updateBranch(repo.getFullName(), fixBranchName, scanningResult.getPath(), branchContent, encodedString, accessToken);
    }

    private void createPullRequest(Repo repo, String fixBranchName, BranchContent branchContent, SummaryFix summaryFix, AnalysisUIResult analysisUIResult, ScanningResult scanningResult, String accessToken) {
        String content = createContent(branchContent, summaryFix, analysisUIResult, scanningResult);
        githubClient.createPullRequest(repo.getFullName(), fixBranchName, repo.getDefaultBranch(), content, accessToken);
    }

    private void createContentFile(BranchContent branchContent, String fileName) throws Exception {
        String filePath = BEFORE_FILE_FORMAT.formatted(fileName);
        byte[] contentBytes = Base64.getDecoder().decode(
                branchContent.getContent().replace("\n",""));
        FileUtils.writeByteArrayToFile(new File(filePath), contentBytes);
        log.info("Download file successfully to path: {}", filePath);
    }

    private void createFixFile(SummaryFix summaryFix, String fileName) throws Exception {
        PackageManagementTool packageManagementTool = null;
        Ecosystem ecosystem = Ecosystem.getEcosystem(summaryFix.getEcosystem());
        switch (ecosystem) {
            case MAVEN -> packageManagementTool = applicationContext.getBean(MavenTool.class);
            case NPM -> packageManagementTool = applicationContext.getBean(NpmTool.class);
        }
        packageManagementTool.createFixFile(summaryFix, fileName);
    }

    private String createContent(BranchContent branchContent, SummaryFix summaryFix, AnalysisUIResult analysisUIResult, ScanningResult scanningResult) {
        List<SummaryLibraryFix> libraryFixes = summaryFix.getLibs();
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>Fix vulnerabilities</h3>\n");
        sb.append(String.format("<p>There are %s vulnerable dependencies (with %s direct & %s transitive)</p>\n",
                libraryFixes.size(), libraryFixes.stream().filter(lf -> lf.getDescription().equals(APPLY_TO_FIX)).count(),
                libraryFixes.stream().filter(lf -> lf.getDescription().equals(REFER_TO_FIX)).count()));
        sb.append("<h4>File changes:</h4>\n");
        sb.append("<ul><li>%s</li></ul>\n".formatted(branchContent.getPath()));
        sb.append("<h4>Vulnerabilities that will be fixed:</h4>\n");
        sb.append("<table>\n");
        sb.append("<tr>\n" +
                "    <th>Name</th>\n" +
                "    <th>Ecosystem</th>\n" +
                "    <th>Severity</th>\n" +
                "    <th>Vulnerable Version</th>\n" +
                "    <th>Fixed Version</th>\n" +
                "    <th>Used By</th>\n" +
                "    <th>Description</th>\n" +
                "  </tr>");
        libraryFixes.forEach(lf -> {
            sb.append("<tr>\n" +
                    "    <td>%s</td>\n".formatted(lf.getName()) +
                    "    <td>%s</td>\n".formatted(lf.getEcosystem()) +
                    "    <td>%s</td>\n".formatted(lf.getSeverity()) +
                    "    <td>%s</td>\n".formatted(lf.getCurrentVersion()) +
                    "    <td>%s</td>\n".formatted(lf.getFixedVersion()) +
                    "    <td>\n" +
                    "       <ul>");
            lf.getUsedBy().forEach(l -> {
                sb.append("<li>%s:%s</li>".formatted(l.getName(), l.getVersion()));
            });
            sb.append("</ul>\n" +
                    "    </td>\n" +
                    "    <td>%s</td>\n".formatted(lf.getDescription()) +
                    "  </tr>");
        });
        sb.append("</table>\n");
        return sb.toString();
    }
}
