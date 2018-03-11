package org.maksim.training.mtapp.repository.specification.event;

import lombok.RequiredArgsConstructor;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.Seance;
import org.maksim.training.mtapp.repository.specification.CriteriaSpecification;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.SortedSet;

@RequiredArgsConstructor
public final class EventsByDateRangeSpecification
        implements PredicateSpecification<Event>, CriteriaSpecification<Event> {
    private final LocalDateTime from;
    private final LocalDateTime to;

    @Override
    public boolean test(Event event) {
        SortedSet<Seance> seances = event.getSeances().subSet(Seance.builder().dateTime(from).build(),
                Seance.builder().dateTime(to).build());
        return !seances.isEmpty();
    }

    @Override
    public TypedQuery<Event> toTypedQuery(EntityManager entityManager) {
        TypedQuery<Event> query = entityManager
                .createQuery("SELECT e FROM Event e LEFT JOIN e.seances s WHERE s.dateTime BETWEEN :from AND :to", Event.class);
        query.setParameter("from", from);
        query.setParameter("to", to);
        return query;
    }
}