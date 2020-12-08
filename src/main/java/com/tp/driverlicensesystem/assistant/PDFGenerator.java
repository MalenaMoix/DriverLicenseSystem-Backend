package com.tp.driverlicensesystem.assistant;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.tp.driverlicensesystem.model.License;
import com.tp.driverlicensesystem.model.Owner;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.time.LocalDate;


public class PDFGenerator {

    private HttpServletResponse response;
    private License license;
    public PDFGenerator(){

    }

    public void getNewLicensePDF(HttpServletResponse r, License lic){
        response=r;
        license = lic;
//        license = new License();
//        license.setLicenseClass("A");
//        license.setLicenseTerm(LocalDate.now());
//        license.setIdLicense(1);
//        license.setIsRevoked(false);
//        license.setLicenseStart(LocalDate.of(2018,12,12));
//        license.setObservations("Estas son observaciones de la licencia");
//        license.setLicenseCost(20.0);
//        Owner owner = new Owner();
//        owner.setBirthDate(LocalDate.of(1998,4,4));
//        owner.setDocument(40905305);
//        owner.setDonor(true);
//        owner.setBloodType("0");
//        owner.setRhFactor("+");
//        owner.setName("Tomas");
//        owner.setLastName("Ravelli");
//        owner.setAddress("Marcelino escalada Marcelino escalaa Marcelino escalada skks");
//        owner.setGender("Masculino");

       // license.setLicenseOwner(owner);
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
            Font textFontCardBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            textFontCardBold.setSize(16);
            textFontCardBold.setColor(Color.BLACK);

            float indentationLeft = 12;

            Paragraph p = new Paragraph("Emisión licencia de conducir",font);
            p.setAlignment(Paragraph.ALIGN_CENTER);
            p.setSpacingAfter(35);
            document.add(p);
            p = new Paragraph("Ticket N°: ", font);
            p.add(new Phrase(license.getIdLicense().toString(),font));
            p.setSpacingAfter(20);
            p.setIndentationLeft(indentationLeft);
            p.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(p);
            p = new Paragraph("Fecha: ", font);
            p.add(new Phrase(LocalDate.now().toString(),font));
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

            phrase=new Phrase("$8",textFont);
            cell = new PdfPCell();
            cell.setPhrase(phrase);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidthTop(0);
            pdfTable.addCell(cell);

            int yearsValid = license.getLicenseTerm().getYear()-LocalDate.now().getYear();

            phrase=new Phrase("Licencia tipo " +license.getLicenseClass()+ " por " + yearsValid + " años", textFont);
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPhrase(phrase);
            cell.setFixedHeight(fixedCellHeight);
            cell.setBorderWidthBottom(0);
            pdfTable.addCell(cell);

            phrase=new Phrase("$"+(license.getLicenseCost()-8), textFont);
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

                phrase=new Phrase("$" + license.getLicenseCost().toString(), font);
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPhrase(phrase);
            cell.setFixedHeight(fixedCellHeight);
            cell.setBorderWidthTop(borderWidth);
            pdfTable.addCell(cell);

            document.add(pdfTable);

            document.newPage();
            Paragraph pPage2;

            float xImagePosition = document.leftMargin()+30;
            System.out.println(document.getPageSize().getHeight());
            float yImagePosition = document.getPageSize().getHeight()-120-document.topMargin()-document.topMargin();
            indentationLeft=170;

            pPage2 = new Paragraph("Apellido: ", textFontCardBold);
            pPage2.add(new Phrase(license.getLicenseOwner().getLastName(), textFont));
            pPage2.setIndentationLeft(indentationLeft);
            pPage2.setLeading(pPage2.getLeading()+20);
            document.add(pPage2);

            pPage2 = new Paragraph("Nombre: ", textFontCardBold);
            pPage2.add(new Phrase(license.getLicenseOwner().getName(), textFont));
            pPage2.setIndentationLeft(indentationLeft);
            document.add(pPage2);

            pPage2 = new Paragraph("Sexo: ", textFontCardBold);
            pPage2.add(new Phrase(license.getLicenseOwner().getGender().equals("Masculino")?"M ":"F ", textFont));
            pPage2.add(new Phrase("/  Factor sanguineo: ", textFontCardBold));
            pPage2.add(new Phrase(license.getLicenseOwner().getBloodType()+license.getLicenseOwner().getRhFactor(),textFont));
            pPage2.add(new Phrase("  / Donante: ", textFontCardBold));
            pPage2.add(new Phrase(license.getLicenseOwner().getDonor()?"SI":"NO",textFont));
            pPage2.setIndentationLeft(indentationLeft);
            document.add(pPage2);

            pPage2 = new Paragraph("Fecha de nacimiento: ", textFontCardBold);
            pPage2.add(new Phrase(license.getLicenseOwner().getBirthDate().toString(), textFont));
            pPage2.setIndentationLeft(indentationLeft);
            document.add(pPage2);

            pPage2 = new Paragraph("Domicilio: ", textFontCardBold);
            pPage2.add(new Phrase(license.getLicenseOwner().getAddress(), textFont));
            pPage2.setIndentationLeft(indentationLeft);
            document.add(pPage2);

            pPage2 = new Paragraph("Inicio: ", textFontCardBold);
            pPage2.add(new Phrase(license.getLicenseStart().toString(), textFont));
            pPage2.setIndentationLeft(indentationLeft);
            document.add(pPage2);

            pPage2 = new Paragraph("Vencimiento: ", textFontCardBold);
            pPage2.add(new Phrase(license.getLicenseTerm().toString(), textFont));
            pPage2.setIndentationLeft(indentationLeft);
            document.add(pPage2);

            pPage2 = new Paragraph("Clase: ", textFontCardBold);
            pPage2.add(new Phrase(license.getLicenseClass(), textFont));
            pPage2.setIndentationLeft(indentationLeft);
            document.add(pPage2);

            pPage2 = new Paragraph("Categoria: ", textFontCardBold);
            pPage2.add(new Phrase("Original", textFont));
            pPage2.setIndentationLeft(indentationLeft);
            document.add(pPage2);
            pPage2 = new Paragraph("Obervaciones: ", textFontCardBold);
            pPage2.add(new Phrase(license.getObservations()==(null)?"":license.getObservations(), textFont));
            pPage2.setIndentationLeft(pPage2.getIndentationLeft()+10);
            document.add(pPage2);

            Image ownerPhoto = Image.getInstance("src\\main\\resources\\images\\persona.jpg");
            ownerPhoto.scaleAbsolute(120,120);
            ownerPhoto.setAbsolutePosition(xImagePosition,yImagePosition);

            document.add(ownerPhoto);

            Rectangle border = new Rectangle(36, 490, 559, 806);

            border.setBorder(Rectangle.BOX);
            border.setBorderColor(Color.BLACK);
            border.setBorderWidth(1);

            document.add(border);

            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
