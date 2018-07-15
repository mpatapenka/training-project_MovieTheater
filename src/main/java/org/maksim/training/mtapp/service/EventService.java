package org.maksim.training.mtapp.service;

import org.maksim.training.mtapp.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService extends CrudService<Event, Long> {
    Event getByName(String name);
    List<Event> getForDateRange(LocalDateTime from, LocalDateTime to);
    List<Event> getNextEvents(LocalDateTime to);
}