package org.maksim.training.mtapp.rest.message.converter;

import org.maksim.training.mtapp.entity.Ticket;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

public final class TicketToPdfHttpMessageConverter extends AbstractHttpMessageConverter<Ticket> {
    public TicketToPdfHttpMessageConverter() {
        super(MediaType.APPLICATION_PDF);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Ticket.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    protected Ticket readInternal(Class<? extends Ticket> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(Ticket ticket, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        outputMessage.getBody().write(ticket.toString().getBytes());
    }
}