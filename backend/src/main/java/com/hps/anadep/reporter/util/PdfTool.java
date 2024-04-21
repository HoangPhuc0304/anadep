package com.hps.anadep.reporter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.depgraph.Dependency;
import com.hps.anadep.model.enums.ReportType;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.model.ui.LibraryScanUI;
import com.hps.anadep.reporter.service.helper.HeaderAndFooterPageEventHelper;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class PdfTool extends ReportTool {

    @Override
    public void export(Object data, String projectName, String author, Class<?> classType, HttpServletResponse response) throws IOException {
        response = initResponse(response, FILE_NAME, classType);
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        writer.setPageEvent(new HeaderAndFooterPageEventHelper());
        String[] headers;
        document.open();
        ObjectMapper objectMapper = new ObjectMapper();

        if (classType == AnalysisUIResult.class) {
            headers = new String[]{"No.", "Database Id", "Package Name", "Version", "Summary", "Fix Version", "Severity", "Score"};
            exportVulns(document, headers, projectName, author, objectMapper.convertValue(data, AnalysisUIResult.class));
        } else {
            headers = new String[]{"No.", "Package Name", "Version", "Ecosystem"};
            exportSbom(document, headers, projectName, author, objectMapper.convertValue(data, ScanningResult.class));
        }
        document.close();
    }

    private HttpServletResponse initResponse(HttpServletResponse response, String fileName, Class<?> classType) {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String reportType;
        if (classType == LibraryScanUI.class) {
            reportType = ReportType.VULNS.name().toLowerCase();
        } else {
            reportType = ReportType.SBOM.name().toLowerCase();
        }
        String headerValue = "attachment; filename=" + fileName + "_" + reportType + "_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        return response;
    }

    private void exportVulns(Document document, String[] headers, String projectName, String author, AnalysisUIResult analysisUIResult) throws IOException {
        if (CollectionUtils.isEmpty(analysisUIResult.getLibs())) {
            return;
        }
        generateHeading(document, "Vulnerability Report", projectName, author, analysisUIResult.getEcosystem(), analysisUIResult.getLibs().size());
        generateVulnsContent(document, headers, analysisUIResult.getLibs());
    }

    private void exportSbom(Document document, String[] headers, String projectName, String author, ScanningResult scanningResult) throws IOException {
        if (CollectionUtils.isEmpty(scanningResult.getLibraries())) {
            return;
        }
        generateHeading(document, "SBOM Report", projectName, author, scanningResult.getEcosystem(), scanningResult.getLibraryCount());
        enterSpace(document);

        Paragraph libParagraph = new Paragraph("Libraries:", getFontHeader());
        libParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(libParagraph);
        enterSpace(document);
        generateSbomContent(document, headers, scanningResult.getLibraries());
        enterSpace(document);

        Paragraph depParagraph = new Paragraph("Dependencies:", getFontHeader());
        depParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(depParagraph);
        enterSpace(document);
        String[] dependencyHeaders = new String[]{"No.", "From", "To", "Numeric From", "Numeric To", "Resolution"};
        generateDependenciesContent(document, dependencyHeaders, scanningResult.getDependencies());
    }

    private void generateHeading(Document document, String title, String projectName, String author, String ecosystem, int libs) throws IOException {
        Paragraph titleParagraph = new Paragraph(title, getFontTitle());
        titleParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(titleParagraph);

        Paragraph projectNameParagraph = new Paragraph("Project Name: ", getFontLabel());
        projectNameParagraph.add(new Phrase(projectName, getFontSubtitle()));
        projectNameParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(projectNameParagraph);

        Paragraph authorParagraph = new Paragraph("Author: ", getFontLabel());
        authorParagraph.add(new Phrase(author, getFontSubtitle()));
        authorParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(authorParagraph);

        Paragraph exportDateParagraph = new Paragraph("Exported On: ", getFontLabel());
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        exportDateParagraph.add(new Phrase(currentDateTime, getFontSubtitle()));
        exportDateParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(exportDateParagraph);

        Paragraph ecosystemParagraph = new Paragraph("Ecosystem: ", getFontLabel());
        ecosystemParagraph.add(new Phrase(ecosystem, getFontSubtitle()));
        ecosystemParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(ecosystemParagraph);

        Paragraph libsParagraph = new Paragraph("Amount: ", getFontLabel());
        libsParagraph.add(new Phrase(String.valueOf(libs), getFontSubtitle()));
        libsParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(libsParagraph);

        Image image = Image.getInstance("logo.png");
        image.scaleAbsolute(150, 150);
        image.setAbsolutePosition(PageSize.A4.getHeight() - image.getScaledHeight() - 20, PageSize.A4.getWidth() - image.getScaledWidth() - 20);
        document.add(image);

        enterSpace(document);
    }

    private void generateVulnsContent(Document document, String[] headers, List<LibraryScanUI> libs) {
        PdfPTable tableHeader = new PdfPTable(headers.length);
        writeTableHeaderPdf(tableHeader, headers);
        document.add(tableHeader);

        // table content
        PdfPTable tableData = new PdfPTable(headers.length);
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
        writeTableData(tableData, data);
        document.add(tableData);
    }

    private void generateSbomContent(Document document, String[] headers, Set<Library> libs) {
        PdfPTable tableHeader = new PdfPTable(headers.length);
        writeTableHeaderPdf(tableHeader, headers);
        document.add(tableHeader);

        // table content
        PdfPTable tableData = new PdfPTable(headers.length);
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
        writeTableData(tableData, data);
        document.add(tableData);
    }

    private void generateDependenciesContent(Document document, String[] headers, Set<Dependency> dependencies) {
        PdfPTable tableHeader = new PdfPTable(headers.length);
        writeTableHeaderPdf(tableHeader, headers);
        document.add(tableHeader);

        // table content
        PdfPTable tableData = new PdfPTable(headers.length);
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
        writeTableData(tableData, data);
        document.add(tableData);
    }

    public void writeTableHeaderPdf(PdfPTable table, String[] headers) {
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        for (String header : headers) {
            cell.setPhrase(new Phrase(header, getFontHeader()));
            table.addCell(cell);
        }
    }

    public void writeTableData(PdfPTable table, List<String[]> data) {
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        for (String[] row : data) {
            for (String col : row) {
                cell.setPhrase(new Phrase(col, getFontContent()));
                table.addCell(cell);
            }
        }
    }

    public Font getFontTitle() {
        Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
        font.setSize(24);
        return font;
    }

    public Font getFontLabel() {
        Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
        font.setSize(16);
        return font;
    }

    public Font getFontSubtitle() {
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setSize(16);
        return font;
    }

    public Font getFontHeader() {
        Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
        font.setSize(10);
        return font;
    }

    public Font getFontContent() {
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setSize(10);
        return font;
    }

    public void enterSpace(Document document) {
        Paragraph space = new Paragraph(" ", getFontSubtitle());
        space.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(space);
    }
}
