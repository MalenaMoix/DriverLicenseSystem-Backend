package com.tp.driverlicensesystem.assistant;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.FileOutputStream;

public class PDFGenerator {

    private HttpServletResponse response;
    public PDFGenerator(){

    }

    public void getNewLicensePDF(HttpServletResponse r){
        response=r;
        createPDF();
    }

    private void createPDF(){
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);
            font.setColor(Color.BLACK);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA);
            textFont.setSize(16);
            textFont.setColor(Color.BLACK);

            float indentationLeft = 12;

            Paragraph p = new Paragraph("Emisión licencia de conducir",font);
            p.setAlignment(Paragraph.ALIGN_CENTER);
            p.setSpacingAfter(35);
            document.add(p);
            p = new Paragraph("Ticket N°: ", font);
            p.setSpacingAfter(20);
            p.setIndentationLeft(indentationLeft);
            p.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(p);
            p = new Paragraph("Fecha: ", font);
            p.setIndentationLeft(indentationLeft);
            p.setSpacingAfter(40);
            document.add(p);
            p = new Paragraph();
            p.setAlignment(Paragraph.ALIGN_CENTER);
            LineSeparator lineSeparator = new LineSeparator(2,95,Color.BLACK,Element.ALIGN_CENTER,0);
            p.add(lineSeparator);
            document.add(lineSeparator);
//            p = new Paragraph();
//            LineSeparator lineSeparator = new LineSeparator(5,100,Color.BLACK,Element.ALIGN_CENTER,10);
//            p.add(lineSeparator);
//            document.add(p);
            int widths[] = {65,35};
            float borderWidth = 3.5f;
            float fixedCellHeight = 40;

            PdfPTable pdfTable = new PdfPTable(2);
            pdfTable.setSpacingBefore(40);
            pdfTable.setWidthPercentage(95);
            pdfTable.setWidths(widths);
            pdfTable.setHeaderRows(1);
            Phrase phrase;

            PdfPCell cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(fixedCellHeight);
            phrase=new Phrase("Concepto",font);
            cell.setPhrase(phrase);
            cell.setFixedHeight(fixedCellHeight);
            cell.setBorderWidthBottom(borderWidth);
            pdfTable.addCell(cell);

            phrase=new Phrase("Monto",font);
            cell = new PdfPCell();
            cell.setPhrase(phrase);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(fixedCellHeight);
            cell.setBorderWidthBottom(borderWidth);
            pdfTable.addCell(cell);

            phrase=new Phrase("Gastos Administrativos",textFont);
            cell = new PdfPCell();
            cell.setPhrase(phrase);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(fixedCellHeight);
            cell.setBorderWidthTop(0);
            pdfTable.addCell(cell);

            phrase=new Phrase("$100",textFont);
            cell = new PdfPCell();
            cell.setPhrase(phrase);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidthTop(0);
            pdfTable.addCell(cell);

            phrase=new Phrase("Licencia tipo A por x años", textFont);
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPhrase(phrase);
            cell.setFixedHeight(fixedCellHeight);
            cell.setBorderWidthBottom(0);
            pdfTable.addCell(cell);

            phrase=new Phrase("$100", textFont);
            cell = new PdfPCell();
            cell.setPhrase(phrase);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(fixedCellHeight);
            cell.setBorderWidthBottom(0);
            pdfTable.addCell(cell);

            phrase=new Phrase("TOTAL", font);
            cell = new PdfPCell();
            cell.setPhrase(phrase);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidthTop(borderWidth);
            pdfTable.addCell(cell);

            phrase=new Phrase("$100", font);
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPhrase(phrase);
            cell.setFixedHeight(fixedCellHeight);
            cell.setBorderWidthTop(borderWidth);
            pdfTable.addCell(cell);

            document.add(pdfTable);
            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
