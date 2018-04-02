package org.maksim.training.mtapp.service;

import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.entity.User;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingService {
    Collection<Ticket> prepareTickets(Event event, LocalDateTime dateTime, User user, Collection<Integer> seats);
    Collection<Ticket> book(Collection<Ticket> tickets, User user);
    Collection<Ticket> getPurchasedTicketsForEvent(Event event, LocalDateTime dateTime);
    Collection<Ticket> getPurchasedTicketsForUser(User user, LocalDateTime dateTime);
}