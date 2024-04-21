package com.hps.anadep.reporter.util;

import jakarta.servlet.http.HttpServletResponse;

public abstract class ReportTool {
    protected final String FILE_NAME = "report";
    public abstract void export(Object data, String projectName, String author, Class<?> classType, HttpServletResponse response) throws Exception;
}
