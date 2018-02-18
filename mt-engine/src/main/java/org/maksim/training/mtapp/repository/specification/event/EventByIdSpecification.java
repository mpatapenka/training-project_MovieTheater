package org.maksim.training.mtapp.repository.specification.event;

import lombok.RequiredArgsConstructor;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import java.util.Objects;

@RequiredArgsConstructor
public final class EventByIdSpecification implements PredicateSpecification<Event> {
    private final Long id;

    @Override
    public boolean test(Event event) {
        return Objects.equals(id, event.getId());
    }
}