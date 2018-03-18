package org.maksim.training.mtapp.controller.view.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.maksim.training.mtapp.entity.Ticket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

public final class TicketPdfView extends CustomAbstractPdfView {
    @Override
    void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        @SuppressWarnings("unchecked") Collection<Ticket> tickets = (Collection<Ticket>) model.get("tickets");

        document.add(new Paragraph("Booked tickets"));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100.0f);
        table.setWidths(new float[] {2.0f, 2.0f, 1.0f, 1.0f, 2.0f, 2.0f});
        table.setSpacingBefore(10);

        // define font for table header row
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(BaseColor.WHITE);

        // define table header cell
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.BLUE);
        cell.setPadding(5);

        // write table header
        cell.setPhrase(new Phrase("Event name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Date & Time", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Auditorium", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Seat", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Selling price", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("User", font));
        table.addCell(cell);

        tickets.forEach(t -> {
            table.addCell(t.getEvent().getName());
            table.addCell(String.valueOf(t.getDateTime()));
            table.addCell(t.getEvent().getAuditorium(t.getDateTime()).getName());
            table.addCell(String.valueOf(t.getSeat()));
            table.addCell(String.valueOf(t.getSellingPrice()));
            table.addCell(t.getUser() != null ? t.getUser().getFirstName() + " " + t.getUser().getLastName() : " - ");
        });
        document.add(table);
    }
}