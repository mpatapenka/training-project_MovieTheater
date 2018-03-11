package org.maksim.training.mtapp.repository.specification.event;

import lombok.RequiredArgsConstructor;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.repository.specification.CriteriaSpecification;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Objects;

@RequiredArgsConstructor
public final class EventByNameSpecification
        implements PredicateSpecification<Event>, CriteriaSpecification<Event> {
    private final String name;

    @Override
    public boolean test(Event event) {
        return Objects.equals(name, event.getName());
    }

    @Override
    public TypedQuery<Event> toTypedQuery(EntityManager entityManager) {
        TypedQuery<Event> query = entityManager.createQuery("SELECT e FROM Event e WHERE e.name = :name", Event.class);
        query.setParameter("name", name);
        return query;
    }
}