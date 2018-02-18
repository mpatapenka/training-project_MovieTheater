package org.maksim.training.mtapp.repository.specification.ticket;

import lombok.RequiredArgsConstructor;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
public final class TicketsByEventAndDateTimeSpecification implements PredicateSpecification<Ticket> {
    private final Event event;
    private final LocalDateTime dateTime;

    @Override
    public boolean test(Ticket ticket) {
        return Objects.equals(event, ticket.getEvent())
                && dateTime.compareTo(ticket.getDateTime()) == 0;
    }
}