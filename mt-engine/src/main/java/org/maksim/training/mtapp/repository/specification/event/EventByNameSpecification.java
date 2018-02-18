package org.maksim.training.mtapp.repository.specification.event;

import lombok.RequiredArgsConstructor;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import java.util.Objects;

@RequiredArgsConstructor
public final class EventByNameSpecification implements PredicateSpecification<Event> {
    private final String name;

    @Override
    public boolean test(Event event) {
        return Objects.equals(name, event.getName());
    }
}