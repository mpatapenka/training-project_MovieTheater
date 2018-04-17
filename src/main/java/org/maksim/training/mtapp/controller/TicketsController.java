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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

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

    @GetMapping
    public String getPageWithTickets(Model model) {
        model.addAttribute("eventNames", eventService.getAll().stream().map(Event::getName).collect(Collectors.toList()));
        return "tickets";
    }

    @GetMapping(params = {"eventName", "dateTime"}, consumes = MediaType.APPLICATION_PDF_VALUE)
    public ModelAndView getBookedTicketsForEvent(HttpServletResponse response, @RequestParam String eventName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        response.setHeader("Content-disposition", "attachment; filename=tickets-" + eventName + "-" + dateTime + ".pdf");
        Event event = eventService.getByName(eventName);
        Collection<Ticket> purchasedTicketsForEvent = bookingService.getPurchasedTicketsForEvent(event, dateTime);
        return new ModelAndView(new TicketPdfView(), "tickets", purchasedTicketsForEvent);
    }

    @GetMapping(params = {"email", "dateTime"}, consumes = MediaType.APPLICATION_PDF_VALUE)
    public ModelAndView getBookedTicketsForUser(HttpServletResponse response, @RequestParam String email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        response.setHeader("Content-disposition", "attachment; filename=tickets-" + email + "-" + dateTime + ".pdf");
        User user = userService.getByEmail(email);
        Collection<Ticket> purchasedTicketsForUser = bookingService.getPurchasedTicketsForUser(user, dateTime);
        return new ModelAndView(new TicketPdfView(), "tickets", purchasedTicketsForUser);
    }

    @GetMapping(params = {"eventName", "dateTime"}, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> getBookedTicketsForEventRest(@RequestParam String eventName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        Event event = eventService.getByName(eventName);
        Collection<Ticket> purchasedTicketsForEvent = bookingService.getPurchasedTicketsForEvent(event, dateTime);
        return new ResponseEntity<>(purchasedTicketsForEvent, HttpStatus.OK);
    }

    @GetMapping(params = {"email", "dateTime"}, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> getBookedTicketsForUserRest(@RequestParam String email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        User user = userService.getByEmail(email);
        Collection<Ticket> purchasedTicketsForUser = bookingService.getPurchasedTicketsForUser(user, dateTime);
        return new ResponseEntity<>(purchasedTicketsForUser, HttpStatus.OK);
    }

    @GetMapping(params = {"eventName", "dateTime"})
    public String getPageWithBookedTicketsForEvent(@RequestParam String eventName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            Model model) {
        Event event = eventService.getByName(eventName);
        Collection<Ticket> purchasedTicketsForEvent = bookingService.getPurchasedTicketsForEvent(event, dateTime);
        model.addAttribute("tickets", purchasedTicketsForEvent);
        return "booked-tickets";
    }

    @GetMapping(params = {"email", "dateTime"})
    public String getPageWithBookedTicketsForUser(@RequestParam String email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            Model model) {
        User user = userService.getByEmail(email);
        Collection<Ticket> purchasedTicketsForUser = bookingService.getPurchasedTicketsForUser(user, dateTime);
        model.addAttribute("tickets", purchasedTicketsForUser);
        return "booked-tickets";
    }
}