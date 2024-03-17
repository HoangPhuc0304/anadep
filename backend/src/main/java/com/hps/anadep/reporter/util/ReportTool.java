package com.hps.anadep.reporter.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public abstract class ReportTool {
    protected final String FILE_NAME = "report";
    public abstract void export(List<?> data, String projectName, String author, Class<?> classType, HttpServletResponse response) throws Exception;
}
