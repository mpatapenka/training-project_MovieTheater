package org.maksim.training.mtapp.service;

import org.maksim.training.mtapp.model.Event;
import org.maksim.training.mtapp.model.Ticket;
import org.maksim.training.mtapp.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingService {
    Collection<Ticket> prepareTickets(Event event, LocalDateTime dateTime, User user, Collection<Integer> seats);
    Collection<Ticket> book(Collection<Ticket> tickets, User user);
    Collection<Ticket> getPurchasedTicketsForEvent(Event event, LocalDateTime dateTime);
    Collection<Ticket> getPurchasedTicketsForUser(User user, LocalDateTime dateTime);
}