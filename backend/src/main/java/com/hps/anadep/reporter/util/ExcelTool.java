package com.hps.anadep.reporter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.depgraph.Dependency;
import com.hps.anadep.model.enums.ReportType;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.model.ui.LibraryScanUI;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ExcelTool extends ReportTool {

    @Override
    public void export(Object data, String projectName, String author, Class<?> classType, HttpServletResponse response) throws IOException {
        initResponse(response, FILE_NAME, classType);
        ServletOutputStream out = response.getOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        String[] headers;
        ObjectMapper objectMapper = new ObjectMapper();

        if (classType == AnalysisUIResult.class) {
            headers = new String[]{"No.", "Database Id", "Package Name", "Version", "Summary", "Fix Version", "Severity", "Score"};
            exportVulns(workbook, headers, projectName, author, objectMapper.convertValue(data, AnalysisUIResult.class));
        } else {
            headers = new String[]{"No.", "Package Name", "Version", "Ecosystem"};
            exportSbom(workbook, headers, projectName, author, objectMapper.convertValue(data, ScanningResult.class));
        }
        workbook.write(out);
        workbook.close();
        out.close();
    }

    private void initResponse(HttpServletResponse response, String fileName, Class<?> classType) {
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
        String headerValue = "attachment; filename=" + fileName + "_" + reportType + "_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
    }


    private void exportVulns(XSSFWorkbook workbook, String[] headers, String projectName, String author, AnalysisUIResult analysisUIResult) throws IOException {
        XSSFSheet sheet = workbook.createSheet("Vulnerability Report");
        if (CollectionUtils.isEmpty(analysisUIResult.getLibs())) {
            return;
        }
        generateHeading(workbook, sheet, "Vulnerability Report", projectName, author, analysisUIResult.getEcosystem(), analysisUIResult.getLibs().size());
        generateVulnsContent(workbook, sheet, headers, analysisUIResult.getLibs(), 7);
    }

    private void exportSbom(XSSFWorkbook workbook, String[] headers, String projectName, String author, ScanningResult scanningResult) throws IOException {
        XSSFSheet sheet = workbook.createSheet("SBOM Report");
        if (CollectionUtils.isEmpty(scanningResult.getLibraries())) {
            return;
        }
        generateHeading(workbook, sheet, "SBOM Report", projectName, author, scanningResult.getEcosystem(), scanningResult.getLibraryCount());
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(getFontHeader(workbook));

        Row rowLibrary = sheet.createRow(7);
        createCell(sheet, rowLibrary, 0, "Libraries", cellStyle);
        generateSbomContent(workbook, sheet, headers, scanningResult.getLibraries(), 8);

        int rowNumber = scanningResult.getLibraries().size() + 10;
        Row rowDependency = sheet.createRow(rowNumber);
        createCell(sheet, rowDependency, 0, "Dependencies", cellStyle);
        String[] dependencyHeaders = new String[]{"No.", "From", "To", "Numeric From", "Numeric To", "Resolution"};
        generateDependenciesContent(workbook, sheet, dependencyHeaders, scanningResult.getDependencies(), rowNumber + 1);
    }

    private void generateHeading(XSSFWorkbook workbook, XSSFSheet sheet, String title, String projectName, String author, String ecosystem, int libs) throws IOException {
        CellStyle cellStyleLabel = workbook.createCellStyle();
        CellStyle cellStyleValue = workbook.createCellStyle();
        cellStyleLabel.setFont(getFontLabel(workbook));
        cellStyleValue.setFont(getFontValue(workbook));

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String[][] matrix = new String[][]{{"Project Name", projectName}, {"Author", author}, {"Report Type", title}, {"Exported On", currentDateTime}, {"Ecosystem", ecosystem}, {"Amount", String.valueOf(libs)}};
        generateHeading(sheet, cellStyleLabel, cellStyleValue, matrix);
    }

    private void generateHeading(XSSFSheet sheet, CellStyle cellStyleLabel, CellStyle cellStyleValue, String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            Row row = sheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue(matrix[i][0]);
            cell.setCellStyle(cellStyleLabel);
            cell = row.createCell(1);
            cell.setCellValue(matrix[i][1]);
            cell.setCellStyle(cellStyleValue);
        }
    }

    private void generateVulnsContent(XSSFWorkbook workbook, XSSFSheet sheet, String[] headers, List<LibraryScanUI> libs, int rowNumber) {
        writeTableHeader(workbook, sheet, headers, rowNumber);

        List<String[]> data = new ArrayList<>();
        int number = 1;
        for (LibraryScanUI lib : libs) {
            data.add(new String[]{
                    String.valueOf(number),
                    lib.getVuln().getId(),
                    lib.getInfo().getName(),
                    lib.getInfo().getVersion(),
                    lib.getVuln().getSummary(),
                    lib.getVuln().getFixed(),
                    CollectionUtils.isEmpty(lib.getVuln().getSeverity()) ? "" : lib.getVuln().getSeverity().get(0).getRanking(),
                    CollectionUtils.isEmpty(lib.getVuln().getSeverity()) ? "" : String.valueOf(lib.getVuln().getSeverity().get(0).getBaseScore())
            });
            number++;
        }
        writeTableData(workbook, sheet, data, rowNumber + 1);
    }

    private void generateSbomContent(XSSFWorkbook workbook, XSSFSheet sheet, String[] headers, Set<Library> libs, int rowNumber) {
        writeTableHeader(workbook, sheet, headers, rowNumber);

        List<String[]> data = new ArrayList<>();
        int number = 1;
        for (Library lib : libs) {
            data.add(new String[]{
                    String.valueOf(number),
                    lib.getName(),
                    lib.getVersion(),
                    lib.getEcosystem()
            });
            number++;
        }
        writeTableData(workbook, sheet, data, rowNumber + 1);
    }

    private void generateDependenciesContent(XSSFWorkbook workbook, XSSFSheet sheet, String[] headers, Set<Dependency> dependencies, int rowNumber) {
        writeTableHeader(workbook, sheet, headers, rowNumber);

        List<String[]> data = new ArrayList<>();
        int number = 1;
        for (Dependency dep : dependencies) {
            data.add(new String[]{
                    String.valueOf(number),
                    dep.getFrom(),
                    dep.getTo(),
                    String.valueOf(dep.getNumericFrom()),
                    String.valueOf(dep.getNumericTo()),
                    dep.getResolution(),
            });
            number++;
        }
        writeTableData(workbook, sheet, data, rowNumber + 1);
    }

    public void writeTableHeader(XSSFWorkbook workbook, XSSFSheet sheet, String[] headers, int rowNumber) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(getFontHeader(workbook));
        Row row = sheet.createRow(rowNumber);

        int i = 0;
        for (String header : headers) {
            createCell(sheet, row, i, header, cellStyle);
            i++;
        }
    }

    public void writeTableData(XSSFWorkbook workbook, XSSFSheet sheet, List<String[]> data, int rowNumber) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(getFontContent(workbook));

        int rowNum = rowNumber;
        for (String[] rowData : data) {
            Row row = sheet.createRow(rowNum);
            int colNum = 0;
            for (String col : rowData) {
                createCell(sheet, row, colNum, col, cellStyle);
                colNum++;
            }
            rowNum++;
        }
    }

    public void createCell(XSSFSheet sheet, Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    public XSSFFont getFontLabel(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        return font;
    }

    public XSSFFont getFontValue(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        return font;
    }

    public XSSFFont getFontHeader(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(12);
        return font;
    }

    public XSSFFont getFontContent(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        return font;
    }
}
