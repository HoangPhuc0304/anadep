package com.hps.anadep.reporter.service.helper;

import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class HeaderAndFooterPageEventHelper extends PdfPageEventHelper {
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(760);

        Paragraph pageNumberText = new Paragraph("Page " + document.getPageNumber(), new Font(Font.HELVETICA, 10));
        PdfPCell pageNumberCell = new PdfPCell(pageNumberText);
        pageNumberCell.setBorder(Rectangle.NO_BORDER);
        pageNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(pageNumberCell);

        table.writeSelectedRows(0, -1, 34, 36, writer.getDirectContent());
    }
}
