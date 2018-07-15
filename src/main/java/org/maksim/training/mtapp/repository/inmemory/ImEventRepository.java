package org.maksim.training.mtapp.repository.inmemory;

import org.maksim.training.mtapp.model.Event;
import org.maksim.training.mtapp.repository.EventRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ImEventRepository extends ImRepository<Event> implements EventRepository {
    @Override
    Long getId(Event event) {
        return event.getId();
    }

    @Override
    void generateId(Event event) {
        event.setId(getIdGenerator().getAndIncrement());
    }
}