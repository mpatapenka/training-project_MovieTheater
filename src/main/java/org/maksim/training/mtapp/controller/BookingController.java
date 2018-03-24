package org.maksim.training.mtapp.controller;

import lombok.Data;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.service.BookingService;
import org.maksim.training.mtapp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

@Controller
@RequestMapping("/book")
public class BookingController {
    private final BookingService bookingService;
    private final EventService eventService;

    @Autowired
    public BookingController(BookingService bookingService, EventService eventService) {
        this.bookingService = bookingService;
        this.eventService = eventService;
    }

    @GetMapping(value = "/selling-price", params = {"eventName", "dateTime", "seats"})
    public @ResponseBody BigDecimal getTicketsPrice(@RequestParam String eventName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam Collection<Integer> seats) {
        Event event = eventService.getByName(eventName);
        return bookingService.getTicketsPrice(event, dateTime, null, seats);
    }

    @PostMapping
    public String bookTickets(BookingForm bookingForm) {
        Event event = eventService.getByName(bookingForm.getEventName());
        Collection<Ticket> tickets =
                bookingService.reserveTickets(event, bookingForm.getDateTime(), null, bookingForm.getSeats());
        bookingService.bookTickets(tickets);
        return "redirect:/";
    }

    @Data
    private class BookingForm {
        private String eventName;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime dateTime;
        private Collection<Integer> seats;
    }
}