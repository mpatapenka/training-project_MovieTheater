package org.maksim.training.mtapp.repository.specification.ticket;

import lombok.RequiredArgsConstructor;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.repository.specification.CriteriaSpecification;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
public final class TicketsByEventAndDateTimeSpecification
        implements PredicateSpecification<Ticket>, CriteriaSpecification<Ticket> {
    private final Event event;
    private final LocalDateTime dateTime;

    @Override
    public boolean test(Ticket ticket) {
        return Objects.equals(event, ticket.getEvent())
                && dateTime.compareTo(ticket.getDateTime()) == 0;
    }

    @Override
    public TypedQuery<Ticket> toTypedQuery(EntityManager entityManager) {
        TypedQuery<Ticket> query = entityManager
                .createQuery("SELECT t FROM Ticket t WHERE t.event.id = :id AND t.dateTime = :dateTime", Ticket.class);
        query.setParameter("id", event.getId());
        query.setParameter("dateTime", dateTime);
        return query;
    }
}