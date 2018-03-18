package org.maksim.training.mtapp.controller;

import org.maksim.training.mtapp.controller.view.pdf.TicketPdfView;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.service.BookingService;
import org.maksim.training.mtapp.service.EventService;
import org.maksim.training.mtapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Collection;

@Controller
@RequestMapping("/tickets")
public class TicketsController {
    private final BookingService bookingService;
    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public TicketsController(BookingService bookingService, EventService eventService, UserService userService) {
        this.bookingService = bookingService;
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping(params = {"eventName", "dateTime"}, consumes = MediaType.APPLICATION_PDF_VALUE)
    public ModelAndView getBookedTicketsForEvent(@RequestParam String eventName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        Event event = eventService.getByName(eventName);
        Collection<Ticket> purchasedTicketsForEvent = bookingService.getPurchasedTicketsForEvent(event, dateTime);
        return new ModelAndView(new TicketPdfView(), "tickets", purchasedTicketsForEvent);
    }

    @GetMapping(params = {"email", "dateTime"}, consumes = MediaType.APPLICATION_PDF_VALUE)
    public ModelAndView getBookedTicketsForUser(@RequestParam String email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        User user = userService.getByEmail(email);
        Collection<Ticket> purchasedTicketsForUser = bookingService.getPurchasedTicketsForUser(user, dateTime);
        return new ModelAndView(new TicketPdfView(), "tickets", purchasedTicketsForUser);
    }
}