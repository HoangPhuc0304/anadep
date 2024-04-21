package com.hps.anadep.service.impl;

import com.hps.anadep.analyzer.client.GithubClient;
import com.hps.anadep.model.SummaryLibraryFix;
import com.hps.anadep.model.entity.Repo;
import com.hps.anadep.model.enums.Ecosystem;
import com.hps.anadep.model.github.*;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.SummaryFix;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.scanner.util.MavenTool;
import com.hps.anadep.scanner.util.NpmTool;
import com.hps.anadep.scanner.util.PackageManagementTool;
import com.hps.anadep.service.GitHubService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

    private static final String BRANCH_REF_FORMAT = "refs/heads/%s";
    private static final String BEFORE_FILE_FORMAT = "storage/fix/before/%s";
    private static final String AFTER_FILE_FORMAT = "storage/fix/after/%s";
    private static final String FILE_FORMAT = "%s/%s";
    private static final String FIX_BRANCH_FORMAT = "anadep-fix-%s";

    @Override
    public AccessTokenResponse getToken(AccessTokenRequest accessTokenRequest) {
        String response = githubClient.getAccessToken(accessTokenRequest.getCode());
        if (!response.contains("access_token=")) {
            throw new RuntimeException("Authorize failed");
        }

        String accessToken = response.split("&")[0].split("=")[1];
        String scope = response.split("&")[1].split("=")[1];
        String tokenType = response.split("&")[2].split("=")[1];
        return AccessTokenResponse.builder()
                .accessToken(accessToken)
                .scope(scope)
                .tokenType(tokenType)
                .build();
    }

    @Override
    public void createFixPullRequest(Repo repo, AnalysisUIResult analysisUIResult, ScanningResult scanningResult, SummaryFix summaryFix, String accessToken) throws IOException {
        Branch branch = getDefaultBranch(repo, accessToken);
        String fixBranchName = FIX_BRANCH_FORMAT.formatted(new Date().getTime());
        Branch fixBranch = createFixBranch(repo, branch, fixBranchName, accessToken);
        BranchContent branchContent = getBranchContent(repo, fixBranchName, summaryFix, accessToken);
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
            updateFixBranch(repo, fixBranchName, summaryFix, branchContent, encodedString, accessToken);
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

    private BranchContent getBranchContent(Repo repo, String fixBranchName, SummaryFix summaryFix, String accessToken) {
        Ecosystem ecosystem = Ecosystem.getEcosystem(summaryFix.getEcosystem());
        return githubClient.getBranchContent(repo.getFullName(), fixBranchName, ecosystem.getPackageManagementFile(), accessToken);
    }

    private void updateFixBranch(Repo repo, String fixBranchName, SummaryFix summaryFix, BranchContent branchContent, String encodedString, String accessToken) {
        Ecosystem ecosystem = Ecosystem.getEcosystem(summaryFix.getEcosystem());
        githubClient.updateBranch(repo.getFullName(), fixBranchName, ecosystem.getPackageManagementFile(), branchContent, encodedString, accessToken);
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
