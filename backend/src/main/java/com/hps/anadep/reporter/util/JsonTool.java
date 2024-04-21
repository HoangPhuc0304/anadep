package com.hps.anadep.reporter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.model.enums.ReportType;
import com.hps.anadep.model.report.SummaryReport;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.model.ui.LibraryScanUI;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class JsonTool extends ReportTool {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void export(Object data, String projectName, String author, Class<?> classType, HttpServletResponse response) throws Exception {
        response = initResponse(response, FILE_NAME, classType);
        ServletOutputStream out = response.getOutputStream();

        if (classType == AnalysisUIResult.class) {
            AnalysisUIResult analysisUIResult = objectMapper.convertValue(data, AnalysisUIResult.class);
            exportVulns(analysisUIResult, projectName, author, out);
        } else {
            ScanningResult scanningResult = objectMapper.convertValue(data, ScanningResult.class);
            exportSbom(scanningResult, projectName, author, out);
        }
        out.close();
    }

    public HttpServletResponse initResponse(HttpServletResponse response, String fileName, Class<?> classType) {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String reportType;
        if (classType == AnalysisUIResult.class) {
            reportType = ReportType.VULNS.name().toLowerCase();
        } else {
            reportType = ReportType.SBOM.name().toLowerCase();
        }
        String headerValue = "attachment; filename=" + fileName + "_" + reportType + "_" + currentDateTime + ".json";
        response.setHeader(headerKey, headerValue);
        return response;
    }

    private void exportVulns(AnalysisUIResult analysisUIResult, String projectName, String author, ServletOutputStream out) throws Exception {
        if (CollectionUtils.isEmpty(analysisUIResult.getLibs())) {
            return;
        }
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        SummaryReport summaryReport = new SummaryReport(
                projectName,
                author,
                "Vulnerability Report",
                currentDateTime,
                analysisUIResult.getEcosystem(),
                analysisUIResult.getLibs().size(),
                analysisUIResult.getLibs(),
                Collections.emptyList()
        );

        try (InputStream in = new ByteArrayInputStream(objectMapper.writeValueAsString(summaryReport).getBytes(StandardCharsets.UTF_8))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private void exportSbom(ScanningResult scanningResult, String projectName, String author, ServletOutputStream out) throws Exception {
        if (CollectionUtils.isEmpty(scanningResult.getLibraries())) {
            return;
        }
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        SummaryReport summaryReport = new SummaryReport(
                projectName,
                author,
                "SBOM Report",
                currentDateTime,
                scanningResult.getEcosystem(),
                scanningResult.getLibraryCount(),
                scanningResult.getLibraries().stream().toList(),
                scanningResult.getDependencies().stream().toList()
        );

        try (InputStream in = new ByteArrayInputStream(objectMapper.writeValueAsString(summaryReport).getBytes(StandardCharsets.UTF_8))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
