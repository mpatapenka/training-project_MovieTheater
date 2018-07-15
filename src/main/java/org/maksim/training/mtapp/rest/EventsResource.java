package org.maksim.training.mtapp.rest;

import lombok.extern.slf4j.Slf4j;
import org.maksim.training.mtapp.model.Event;
import org.maksim.training.mtapp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rest/events")
public class EventsResource {
    private final EventService eventService;

    @Autowired
    public EventsResource(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllEvents() {
        return new ResponseEntity<>(eventService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEvent(@PathVariable long eventId) {
        Event event = eventService.getById(eventId);
        return event != null
                ? new ResponseEntity<>(event, HttpStatus.OK)
                : new ResponseEntity<>("Event not found.", HttpStatus.CONFLICT);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        try {
            return new ResponseEntity<>(eventService.save(event), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Unhandled exception occur.", e);
            return new ResponseEntity<>("Creation failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{eventId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateEvent(@PathVariable long eventId, @RequestBody Event event) {
        Event originalEvent = eventService.getById(eventId);
        if (originalEvent != null) {
            event.setId(eventId);
            Event updated = eventService.save(event);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        return new ResponseEntity<>("Event not found.", HttpStatus.CONFLICT);
    }

    @DeleteMapping(value = "/{eventId}")
    public ResponseEntity<?> removeUser(@PathVariable long eventId) {
        Event event = eventService.getById(eventId);
        if (event != null) {
            Event removed = eventService.remove(event);
            return new ResponseEntity<>(removed, HttpStatus.OK);
        }
        return new ResponseEntity<>("Event not found.", HttpStatus.CONFLICT);
    }
}