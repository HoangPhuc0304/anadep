package com.hps.anadep.reporter.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface ReportService {

    void export(List<?> data, String projectName, String author, String type, String format, HttpServletResponse response) throws Exception;
}
