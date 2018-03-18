package org.maksim.training.mtapp.controller;

import org.maksim.training.mtapp.entity.Auditorium;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventsController {
    private final EventService eventService;

    @Autowired
    public EventsController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String getPageWithEvents(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            Model model) {
        Collection<Event> events;
        if (from != null && to != null) {
            events = eventService.getForDateRange(from, to);
        } else if (to != null) {
            events = eventService.getNextEvents(to);
        } else {
            events = eventService.getAll();
        }
        model.addAttribute("events", events);
        return "events";
    }

    @GetMapping("/{eventId}")
    public String getEventDetails(@PathVariable Long eventId, Model model) {
        model.addAttribute("event", eventService.getById(eventId));
        return "event-details";
    }

    @GetMapping("/{eventId}/{dateTime}/seatmap")
    public String getEventSeatMap(@PathVariable Long eventId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime, Model model) {
        Event event = eventService.getById(eventId);
        event.getSeances().removeIf(s -> s.getDateTime().compareTo(dateTime) != 0);
        Auditorium auditorium = event.getAuditorium(dateTime);
        model.addAttribute("event", event);
        model.addAttribute("auditorium", auditorium);
        return "event-seatmap";
    }

    @PostMapping(value = "/upload", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadEvents(@RequestBody List<Event> events) {
        try {
            events.forEach(eventService::save);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Creation failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}