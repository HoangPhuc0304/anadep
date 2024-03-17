package com.hps.anadep.reporter.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.enums.ReportType;
import com.hps.anadep.model.report.SummaryReport;
import com.hps.anadep.model.ui.LibraryScanUI;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JsonTool extends ReportTool {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void export(List<?> data, String projectName, String author, Class<?> classType, HttpServletResponse response) throws Exception {
        response = initResponse(response, FILE_NAME, classType);
        ServletOutputStream out = response.getOutputStream();

        if (classType == LibraryScanUI.class) {
            List<LibraryScanUI> libs = objectMapper.convertValue(data, new TypeReference<List<LibraryScanUI>>() {
            });
            exportVulns(libs, projectName, author, out);
        } else {
            List<Library> libs = objectMapper.convertValue(data, new TypeReference<List<Library>>() {
            });
            exportSbom(libs, projectName, author, out);
        }
        out.close();
    }

    public HttpServletResponse initResponse(HttpServletResponse response, String fileName, Class<?> classType) {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String reportType;
        if (classType == LibraryScanUI.class) {
            reportType = ReportType.VULNS.name().toLowerCase();
        } else {
            reportType = ReportType.SBOM.name().toLowerCase();
        }
        String headerValue = "attachment; filename=" + fileName + "_" + reportType + "_" + currentDateTime + ".json";
        response.setHeader(headerKey, headerValue);
        return response;
    }

    private void exportVulns(List<LibraryScanUI> libs, String projectName, String author, ServletOutputStream out) throws Exception {
        if (CollectionUtils.isEmpty(libs)) {
            return;
        }
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        SummaryReport summaryReport = new SummaryReport(
                projectName,
                author,
                "Vulnerability Report",
                currentDateTime, libs.get(0).getInfo().getEcosystem(),
                libs.size(),
                libs
        );

        try (InputStream in = new ByteArrayInputStream(objectMapper.writeValueAsString(summaryReport).getBytes(StandardCharsets.UTF_8))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private void exportSbom(List<Library> libs, String projectName, String author, ServletOutputStream out) throws Exception {
        if (CollectionUtils.isEmpty(libs)) {
            return;
        }
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        SummaryReport summaryReport = new SummaryReport(
                projectName,
                author,
                "SBOM Report",
                currentDateTime, libs.get(0).getEcosystem(),
                libs.size(),
                libs
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
