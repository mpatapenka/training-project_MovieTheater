package org.maksim.training.mtapp.service;

import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.entity.Ticket;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingService {
    BigDecimal getTicketsPrice(Event event, LocalDateTime dateTime, User user, Collection<Integer> seats);
    Collection<Ticket> reserveTickets(Event event, LocalDateTime dateTime, User user, Collection<Integer> seats);
    void bookTickets(Collection<Ticket> tickets);
    Collection<Ticket> getPurchasedTicketsForEvent(Event event, LocalDateTime dateTime);
    Collection<Ticket> getPurchasedTicketsForUser(User user, LocalDateTime dateTime);
}