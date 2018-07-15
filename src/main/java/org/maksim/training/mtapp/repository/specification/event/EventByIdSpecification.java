package org.maksim.training.mtapp.repository.specification.event;

import lombok.RequiredArgsConstructor;
import org.maksim.training.mtapp.model.Event;
import org.maksim.training.mtapp.repository.specification.CriteriaSpecification;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Objects;

@RequiredArgsConstructor
public final class EventByIdSpecification
        implements PredicateSpecification<Event>, CriteriaSpecification<Event> {
    private final Long id;

    @Override
    public boolean test(Event event) {
        return Objects.equals(id, event.getId());
    }

    @Override
    public TypedQuery<Event> toTypedQuery(EntityManager entityManager) {
        TypedQuery<Event> query = entityManager.createQuery("SELECT e FROM Event e WHERE e.id = :id", Event.class);
        query.setParameter("id", id);
        return query;
    }
}