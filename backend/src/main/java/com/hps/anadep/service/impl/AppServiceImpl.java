package com.hps.anadep.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.analyzer.service.AnalyzerService;
import com.hps.anadep.evaluator.service.EvaluateService;
import com.hps.anadep.exception.NotFoundException;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.entity.AuthToken;
import com.hps.anadep.model.entity.History;
import com.hps.anadep.model.entity.Repo;
import com.hps.anadep.model.entity.User;
import com.hps.anadep.model.report.ReportRequest;
import com.hps.anadep.model.osv.Severity;
import com.hps.anadep.model.response.*;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.model.ui.LibraryScanUI;
import com.hps.anadep.reporter.service.ReportService;
import com.hps.anadep.repository.AuthTokenRepository;
import com.hps.anadep.repository.HistoryRepository;
import com.hps.anadep.repository.RepoRepository;
import com.hps.anadep.repository.UserRepository;
import com.hps.anadep.scanner.service.ScannerService;
import com.hps.anadep.security.AppUser;
import com.hps.anadep.service.AppService;
import com.hps.anadep.service.GitHubService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private ScannerService scannerService;
    @Autowired
    private AnalyzerService analyzerService;
    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private GitHubService gitHubService;
    @Autowired
    private RepoRepository repoRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthTokenRepository authTokenRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public AnalysisResult retrieve(Library library) {
        return analyzerService.analyze(library);
    }

    @Override
    public AnalysisResult retrieveV2(Library library) {
        return analyzerService.analyzeV2(library);
    }

    @Override
    public ScanningResult scan(MultipartFile file, boolean includeTransitive) throws Exception {
        long start = System.currentTimeMillis();
        ScanningResult scanningResult = scannerService.scan(file, includeTransitive);
        return new ScanningResult(
                scanningResult,
                System.currentTimeMillis() - start
        );
    }

    @Override
    public AnalysisResult analyze(MultipartFile file, Boolean includeSafe) throws Exception {
        long start = System.currentTimeMillis();
        ScanningResult scanningResult = scannerService.scan(file, true);
        long scanningTime = System.currentTimeMillis() - start;
        AnalysisResult analysisResult = analyzerService.analyze(scanningResult.getLibraries(), includeSafe);
        analysisResult.setResponseTime(analysisResult.getResponseTime() + scanningTime);
        return analysisResult;
    }

    @Override
    public AnalysisResult analyze(MultipartFile file, Boolean includeSafe, History history) throws Exception {
        long start = System.currentTimeMillis();
        ScanningResult scanningResult = scannerService.scan(file, true);
        long scanningTime = System.currentTimeMillis() - start;

        history.setScanningResult(objectMapper.writeValueAsString(scanningResult));

        AnalysisResult analysisResult = analyzerService.analyze(scanningResult.getLibraries(), includeSafe);
        analysisResult.setResponseTime(analysisResult.getResponseTime() + scanningTime);
        return analysisResult;
    }

    @Override
    public AnalysisResult analyzeV2(MultipartFile file, Boolean includeSafe) throws Exception {
        long start = System.currentTimeMillis();
        ScanningResult scanningResult = scannerService.scan(file, true);
        long scanningTime = System.currentTimeMillis() - start;
        AnalysisResult analysisResult = analyzerService.analyzeV2(scanningResult.getLibraries(), includeSafe);
        analysisResult.setResponseTime(analysisResult.getResponseTime() + scanningTime);
        return analysisResult;
    }

    @Override
    public AnalysisResult analyzeV2(MultipartFile file, Boolean includeSafe, History history) throws Exception {
        long start = System.currentTimeMillis();
        ScanningResult scanningResult = scannerService.scan(file, true);
        long scanningTime = System.currentTimeMillis() - start;

        history.setScanningResult(objectMapper.writeValueAsString(scanningResult));

        AnalysisResult analysisResult = analyzerService.analyzeV2(scanningResult.getLibraries(), includeSafe);
        analysisResult.setResponseTime(analysisResult.getResponseTime() + scanningTime);
        return analysisResult;
    }

    @Override
    public AnalysisResult analyzeFast(ScanningResult scanningResult, Boolean includeSafe) throws Exception {
        Set<Library> libraries = scanningResult.getLibraries();
        AnalysisResult analysisResult = analyzerService.analyze(libraries, includeSafe);
        analysisResult.setResponseTime(analysisResult.getResponseTime());
        return analysisResult;
    }

    @Override
    public AnalysisResult analyzeFastV2(ScanningResult scanningResult, Boolean includeSafe) throws Exception {
        Set<Library> libraries = scanningResult.getLibraries();
        AnalysisResult analysisResult = analyzerService.analyzeV2(libraries, includeSafe);
        analysisResult.setResponseTime(analysisResult.getResponseTime());
        return analysisResult;
    }

    @Override
    public Severity evaluate(String vector) {
        return evaluateService.evaluate(vector);
    }

    @Override
    public void export(ReportRequest reportRequest, String projectName, String author, String type, String format, HttpServletResponse response) throws Exception {
        reportService.export(reportRequest.getData(), projectName, author, type, format, response);
    }

    @Override
    public AnalysisUIResult reformat(AnalysisResult analysisResult) {
        List<LibraryScanUI> libs = new ArrayList<>();
        for (LibraryScan libScan : analysisResult.getLibs()) {
            Library info = new Library(libScan.getInfo().getName(), libScan.getInfo().getVersion(), libScan.getInfo().getEcosystem());
            for (VulnerabilityResponse vulRes : libScan.getVulns()) {
                libs.add(new LibraryScanUI(info, vulRes));
            }
        }
        return new AnalysisUIResult(
                libs,
                analysisResult.getEcosystem(),
                analysisResult.getIssuesCount(),
                analysisResult.getLibraryCount(),
                analysisResult.isIncludeSafe(),
                analysisResult.getResponseTime()
        );
    }

    @Override
    public SummaryFix applyFix(String repoId, String historyId, AppUser appUser) throws IOException {
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new NotFoundException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUser().getId(), appUser);
        History history = historyRepository.findByIdAndRepo(UUID.fromString(historyId), repo).orElseThrow(
                () -> new NotFoundException(String.format("The history with id [%s] doesn't exist", historyId)));
        AnalysisUIResult analysisUIResult;
        ScanningResult scanningResult;
        try {
            analysisUIResult = objectMapper.readValue(history.getVulnerabilityResult(), AnalysisUIResult.class);
            scanningResult = objectMapper.readValue(history.getScanningResult(), ScanningResult.class);
        } catch (Exception e) {
            analysisUIResult = new AnalysisUIResult();
            scanningResult = new ScanningResult();
        }
        SummaryFix summaryFix = evaluateService.applyFix(analysisUIResult, scanningResult);

        User user = userRepository.findById(appUser.getId()).orElseThrow(
                () -> new RuntimeException(String.format("The user with id [%s] doesn't exist", appUser.getId())));
        AuthToken authToken = authTokenRepository.findByUser(user).orElseThrow(
                () -> new RuntimeException(String.format("The user with id [%s] doesn't have token", appUser.getId())));
        gitHubService.createFixPullRequest(repo, analysisUIResult, scanningResult, summaryFix, authToken.getGithubToken());
        return summaryFix;
    }

    @Override
    public void advisory(String repoId, String historyId, AppUser appUser) {
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new RuntimeException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUser().getId(), appUser);
        History history = historyRepository.findByIdAndRepo(UUID.fromString(historyId), repo).orElseThrow(
                () -> new RuntimeException(String.format("The history with id [%s] doesn't exist", historyId)));
        try {
            AnalysisUIResult analysisUIResult = objectMapper.readValue(history.getVulnerabilityResult(), AnalysisUIResult.class);
            evaluateService.advisory(repo, analysisUIResult, appUser);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void advisoryV2(String repoId, String historyId, AppUser appUser) {
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new RuntimeException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUser().getId(), appUser);
        History history = historyRepository.findByIdAndRepo(UUID.fromString(historyId), repo).orElseThrow(
                () -> new RuntimeException(String.format("The history with id [%s] doesn't exist", historyId)));
        try {
            AnalysisUIResult analysisUIResult = objectMapper.readValue(history.getVulnerabilityResult(), AnalysisUIResult.class);
            evaluateService.advisoryV2(repo, analysisUIResult, appUser);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public FixResult autoFix(AnalysisUIResult analysisUIResult) {
        return evaluateService.autoFix(analysisUIResult);
    }

    private void validateUserId(UUID id, AppUser appUser) {
        if (!appUser.getId().equals(id)) {
            throw new RuntimeException("Authorize failed");
        }
    }
}
