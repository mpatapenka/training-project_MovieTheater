package org.maksim.training.mtapp.repository.specification.event;

import lombok.RequiredArgsConstructor;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public final class EventsByDateRangeSpecification implements PredicateSpecification<Event> {
    private final LocalDateTime from;
    private final LocalDateTime to;

    @Override
    public boolean test(Event event) {
        return event.getSeances().higherKey(from) != null
                && event.getSeances().lowerKey(to) != null;
    }
}