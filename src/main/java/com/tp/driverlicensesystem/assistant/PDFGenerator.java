package com.tp.driverlicensesystem.assistant;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.tp.driverlicensesystem.model.License;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;


public class PDFGenerator {

    private HttpServletResponse response;
    private License license;
    private Document document;
    private Font font, textFont, textFontCardBold;

    public PDFGenerator(){

    }

    public void getNewLicensePDF(HttpServletResponse r, License lic){
        //response es la respuesta que se devuelve a la solicitud desde el frontend al endpoint.
        response=r;

        //Nueva licencia emitida
        license = lic;

        createPDF();
    }

    private void createPDF(){
        try {
            //Se utiliza la libreria OpenPDF
            //Se crea un nuevo documento.
            document = new Document(PageSize.A4);

            //Se encarga que la instancia document sea enviada al flujo de salida del response, es decir, a la respuesta de la solicitud
            //al endpoint (localhost:9090/license/licensePDF/"idLicencia")
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            //Setear las fuentes a utilizar dentro del PDF.
            setFonts();

            //Sangria
            float indentationLeft = 12;

            //Se crea la primer pagina del PDF, que contiene el comprobante de pago de la nueva licencia.
            firstPdfPage(indentationLeft);

            //Se crea la segunda Pagina del PDF donde se encuentra el nuevo carnet de conducir del titular.
            secondPdfPage();

            document.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setFonts() {
        //Fuente HELVETICA negrita en tamaño 18
        font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLACK);

        //Fuente HELVETICA normal en tamaño 16
        textFont = FontFactory.getFont(FontFactory.HELVETICA);
        textFont.setSize(16);
        textFont.setColor(Color.BLACK);

        //Fuente HELVETICA negrita en tamaño 16 para el carnet de conducir.
        textFontCardBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        textFontCardBold.setSize(16);
        textFontCardBold.setColor(Color.BLACK);
    }


    private void firstPdfPage(float indentationLeft) {

        //Informacion de la primer pagina del PDF
        Paragraph p = new Paragraph("Emisión licencia de conducir",font); //Titulo
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
        LineSeparator lineSeparator = new LineSeparator(2,95, Color.BLACK, Element.ALIGN_CENTER,0);
        p.add(lineSeparator);
        document.add(lineSeparator);

        int widths[] = {65,35}; //Ancho de cada columna de la tabla
        float borderWidth = 3.5f; //Ancho de los bordes de la celda
        float fixedCellHeight = 40; //Altura de las celdas de la tabla

        //Se crea una tabla con 2 columnas.
        PdfPTable pdfTable = new PdfPTable(2);
        pdfTable.setSpacingBefore(40);
        pdfTable.setWidthPercentage(95);

        //Seteo del ancho de las celdas.
        pdfTable.setWidths(widths);

        //Metodo para llenar la tabla con los datos correspondientes: gastos administrativos, costo de la licencia, total.
        fillTable(borderWidth, fixedCellHeight, pdfTable);

        //Se agrega la tabla al documento.
        document.add(pdfTable);
    }

    private void fillTable(float borderWidth, float fixedCellHeight, PdfPTable pdfTable) {

        //Disposicion de los datos en cada celda dentro de la tabla.
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

        //Para calcular la cantidad de años que tiene vigencia la nuevas licencia
        int yearsValid = license.getLicenseTerm().getYear()- LocalDate.now().getYear();

        //StringBuilder para concatenar cadenas de texto eficientemente
        StringBuilder auxString = new StringBuilder("Licencia tipo ");

        auxString.append(license.getLicenseClass()).append(" por ").append(yearsValid).append(" años");

        phrase=new Phrase(auxString.toString(), textFont);
        cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPhrase(phrase);
        cell.setFixedHeight(fixedCellHeight);
        cell.setBorderWidthBottom(0);
        pdfTable.addCell(cell);

        auxString = new StringBuilder("$").append(license.getLicenseCost()-8);
        phrase=new Phrase(auxString.toString(), textFont);
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

        auxString = new StringBuilder("$").append(license.getLicenseCost().toString());

        phrase=new Phrase(auxString.toString(), font);
        cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPhrase(phrase);
        cell.setFixedHeight(fixedCellHeight);
        cell.setBorderWidthTop(borderWidth);
        pdfTable.addCell(cell);
    }

    private void secondPdfPage() throws IOException {

        //Se crea la pagina 2 del PDF
        document.newPage();

        //Posicion absoluta de la imagen del titular en el carnet de conducir
        float xImagePosition = document.leftMargin()+30;
        float yImagePosition = document.getPageSize().getHeight()-120-document.topMargin()-document.topMargin();

        //Disponer e insertar los datos de la nueva licencia en el carnet.
        insertLicenseData();

        //Insetar imagen ejemplo del titular en el carnet.
        Image ownerPhoto = Image.getInstance("src\\main\\resources\\images\\persona.jpg");
        ownerPhoto.scaleAbsolute(120,120);
        ownerPhoto.setAbsolutePosition(xImagePosition,yImagePosition); //Posicion absoluta de la imagen

        document.add(ownerPhoto);

        //Se rodean los datos con un rectangulo, para simular el carnet de conducir.
        Rectangle border = new Rectangle(36, 490, 559, 806);

        border.setBorder(Rectangle.BOX);
        border.setBorderColor(Color.BLACK);
        border.setBorderWidth(1);

        document.add(border);
    }

    private void insertLicenseData() {

        float indentationLeft=170;
        Paragraph pPage2;


        //Se insertan los datos de la licencia en el nuevo carnet de conducir, la segunda pagina del PDF
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
        pPage2.add(new Phrase("/  Tipo sangre: ", textFontCardBold));
        pPage2.add(new Phrase(license.getLicenseOwner().getBloodType()+license.getLicenseOwner().getRhFactor(),textFont));
        pPage2.add(new Phrase(" /  Donante: ", textFontCardBold));
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
    }



}
