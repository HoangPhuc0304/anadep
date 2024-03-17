package com.hps.anadep.reporter.service.impl;

import com.hps.anadep.model.enums.Format;
import com.hps.anadep.model.enums.ReportType;
import com.hps.anadep.reporter.service.ReportService;
import com.hps.anadep.reporter.util.ReportTool;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Override
    public void export(List<?> data, String projectName, String author, String type, String format, HttpServletResponse response) throws Exception {
        ReportType reportType = ReportType.getReportType(type);
        Format reportFormat = Format.getFormat(format);
        ReportTool reportTool = reportFormat.getTool();
        Class<?> classType = reportType.getClassType();
        reportTool.export(data, projectName, author, classType, response);
    }
}
