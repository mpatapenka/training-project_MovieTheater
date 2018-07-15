package org.maksim.training.mtapp.service.impl;

import com.google.common.collect.Lists;
import org.maksim.training.mtapp.model.Event;
import org.maksim.training.mtapp.model.Ticket;
import org.maksim.training.mtapp.model.User;
import org.maksim.training.mtapp.repository.TicketRepository;
import org.maksim.training.mtapp.repository.specification.ticket.TicketsByEventAndDateTimeSpecification;
import org.maksim.training.mtapp.repository.specification.ticket.TicketsByUserAndDateTimeSpecification;
import org.maksim.training.mtapp.service.BookingService;
import org.maksim.training.mtapp.service.PaymentService;
import org.maksim.training.mtapp.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final PricingService pricingService;
    private final PaymentService paymentService;
    private final TicketRepository ticketRepository;

    @Autowired
    public BookingServiceImpl(PricingService pricingService, PaymentService paymentService, TicketRepository ticketRepository) {
        this.pricingService = pricingService;
        this.paymentService = paymentService;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Collection<Ticket> prepareTickets(Event event, LocalDateTime dateTime, User user, Collection<Integer> seats) {
        List<Ticket> tickets = Lists.newArrayList();
        int ticketNumber = 0;
        for (Integer seat : seats) {
            tickets.add(Ticket.builder()
                    .sellingPrice(pricingService.calculateTicketPrice(event, dateTime, user, seat, ++ticketNumber))
                    .dateTime(dateTime).event(event).seat(seat)
                    .build());
        }
        return tickets;
    }

    @Override
    @Transactional
    public Collection<Ticket> book(Collection<Ticket> tickets, User user) {
        ticketRepository.add(tickets);
        paymentService.pay(tickets, user);
        return tickets;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Ticket> getPurchasedTicketsForEvent(Event event, LocalDateTime dateTime) {
        return ticketRepository.query(new TicketsByEventAndDateTimeSpecification(event, dateTime));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Ticket> getPurchasedTicketsForUser(User user, LocalDateTime dateTime) {
        return ticketRepository.query(new TicketsByUserAndDateTimeSpecification(user, dateTime));
    }
}