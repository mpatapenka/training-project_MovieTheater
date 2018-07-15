package org.maksim.training.mtapp.service;

import org.maksim.training.mtapp.model.Event;
import org.maksim.training.mtapp.model.Ticket;
import org.maksim.training.mtapp.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

public interface PricingService {
    BigDecimal calculateTicketPrice(Event event, LocalDateTime dateTime, User user, int seat, int ticketNumber);
    BigDecimal calculateTicketsPrice(Event event, LocalDateTime dateTime, User user, Collection<Integer> seats);
    BigDecimal calculateTicketsPrice(Collection<Ticket> tickets);
}