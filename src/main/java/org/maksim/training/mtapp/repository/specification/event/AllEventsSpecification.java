package org.maksim.training.mtapp.repository.specification.event;

import org.maksim.training.mtapp.model.Event;
import org.maksim.training.mtapp.repository.specification.CriteriaSpecification;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public final class AllEventsSpecification
        implements PredicateSpecification<Event>, CriteriaSpecification<Event> {
    @Override
    public boolean test(Event event) {
        return true;
    }

    @Override
    public TypedQuery<Event> toTypedQuery(EntityManager entityManager) {
        return entityManager.createQuery("SELECT e FROM Event e", Event.class);
    }
}