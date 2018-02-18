package org.maksim.training.mtapp.repository.specification.event;

import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

public final class AllEventsSpecification implements PredicateSpecification<Event> {
    @Override
    public boolean test(Event event) {
        return true;
    }
}