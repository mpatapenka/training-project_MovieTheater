package org.maksim.training.mtapp.controller.view.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.maksim.training.mtapp.entity.Ticket;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.util.Collection;
import java.util.Map;

public final class TicketPdfView extends AbstractPdfView {
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        document.add(new Paragraph("Booked tickets"));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100.0f);
        table.setWidths(new float[]{2.0f, 2.0f, 1.0f, 1.0f, 2.0f, 2.0f});
        table.setSpacingBefore(10);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

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

        @SuppressWarnings("unchecked") Collection<Ticket> tickets = (Collection<Ticket>) model.get("tickets");
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