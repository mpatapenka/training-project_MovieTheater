package org.maksim.training.mtapp.controller;

import org.maksim.training.mtapp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/events")
public class EventsController {
    private final EventService eventService;

    @Autowired
    public EventsController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String getPageWithAllEvents(Model model) {
        model.addAttribute("events", eventService.getAll());
        return "events";
    }

    @GetMapping(params = {"from", "to"})
    public String getPageWithDateRangedEvents(@RequestParam LocalDateTime from, @RequestParam LocalDateTime to,
            Model model) {
        model.addAttribute("events", eventService.getForDateRange(from, to));
        return "events";
    }

    @GetMapping(params = "to")
    public String getPageWithNextEvents(@RequestParam LocalDateTime to, Model model) {
        model.addAttribute("events", eventService.getNextEvents(to));
        return "events";
    }
}