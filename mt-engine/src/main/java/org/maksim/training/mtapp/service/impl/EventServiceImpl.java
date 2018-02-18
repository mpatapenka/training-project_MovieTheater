package org.maksim.training.mtapp.service.impl;

import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.repository.EventRepository;
import org.maksim.training.mtapp.repository.specification.event.AllEventsSpecification;
import org.maksim.training.mtapp.repository.specification.event.EventByIdSpecification;
import org.maksim.training.mtapp.repository.specification.event.EventByNameSpecification;
import org.maksim.training.mtapp.repository.specification.event.EventsByDateRangeSpecification;
import org.maksim.training.mtapp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class EventServiceImpl extends CrudGenericService<Event, Long> implements EventService {
    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        super(eventRepository);
    }

    @Override
    Long getId(Event event) {
        return event.getId();
    }

    @Override
    public Event getById(Long id) {
        return findOne(getRepository().query(new EventByIdSpecification(id)));
    }

    @Override
    public Event getByName(String name) {
        return findOne(getRepository().query(new EventByNameSpecification(name)));
    }

    @Override
    public List<Event> getForDateRange(LocalDateTime from, LocalDateTime to) {
        return getRepository().query(new EventsByDateRangeSpecification(from, to));
    }

    @Override
    public List<Event> getNextEvents(LocalDateTime to) {
        return getRepository().query(new EventsByDateRangeSpecification(LocalDateTime.now(), to));
    }

    @Override
    public Collection<Event> getAll() {
        return getRepository().query(new AllEventsSpecification());
    }
}