package org.maksim.training.mtapp.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.service.BookingService;
import org.maksim.training.mtapp.service.EventService;
import org.maksim.training.mtapp.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/book")
public class BookingController {
    private final PricingService pricingService;
    private final BookingService bookingService;
    private final EventService eventService;

    @Autowired
    public BookingController(PricingService pricingService, BookingService bookingService, EventService eventService) {
        this.pricingService = pricingService;
        this.bookingService = bookingService;
        this.eventService = eventService;
    }

    @GetMapping(value = "/selling-price", params = {"eventName", "dateTime", "seats"})
    public @ResponseBody BigDecimal getTicketsPrice(@AuthenticationPrincipal User user, @RequestParam String eventName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam Collection<Integer> seats) {
        Event event = eventService.getByName(eventName);
        return pricingService.calculateTicketsPrice(event, dateTime, user, seats);
    }

    @PostMapping
    public String bookTickets(@AuthenticationPrincipal User user, BookingForm bookForm) {
        Event event = eventService.getByName(bookForm.getEventName());
        Collection<Ticket> tickets = bookingService.prepareTickets(event, bookForm.getDateTime(), user, bookForm.getSeats());
        bookingService.book(tickets, user);
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