package tools;

import models.Reclamations;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;

public class PDFGeneratorService {

    public void generatePDF(List<Reclamations> reclamationsList, String filePath) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

                // Write column headers
                drawCell(contentStream, 100, 700, 100, 20, "ID");
                drawCell(contentStream, 200, 700, 100, 20, "Objet");
                drawCell(contentStream, 300, 700, 100, 20, "Description");
                drawCell(contentStream, 400, 700, 100, 20, "Etat");

                // Write reclamations data
                int y = 680; // Starting y position for data
                for (Reclamations reclamation : reclamationsList) {
                    drawCell(contentStream, 100, y, 100, 20, String.valueOf(reclamation.getId()));
                    drawCell(contentStream, 200, y, 100, 20, reclamation.getObjet());
                    drawCell(contentStream, 300, y, 100, 20, reclamation.getDescription());
                    drawCell(contentStream, 400, y, 100, 20, reclamation.isEtat() ? "Processed" : "Not Processed");
                    y -= 20; // Adjust spacing between rows
                }
            }

            document.save(filePath);
        }
    }

    private void drawCell(PDPageContentStream contentStream, float x, float y, float width, float height, String text) throws IOException {
        contentStream.setStrokingColor(0, 0, 0);
        contentStream.setLineWidth(1f);
        contentStream.moveTo(x, y);
        contentStream.lineTo(x + width, y);
        contentStream.moveTo(x + width, y);
        contentStream.lineTo(x + width, y - height);
        contentStream.moveTo(x + width, y - height);
        contentStream.lineTo(x, y - height);
        contentStream.moveTo(x, y - height);
        contentStream.lineTo(x, y);
        contentStream.stroke();
        contentStream.beginText();
        contentStream.newLineAtOffset(x + 2, y - 12);
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.showText(text);
        contentStream.endText();
    }
}
