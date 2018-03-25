package org.maksim.training.mtapp.service.impl;

import com.google.common.collect.Lists;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.EventRating;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.entity.UserRole;
import org.maksim.training.mtapp.repository.TicketRepository;
import org.maksim.training.mtapp.repository.specification.ticket.TicketsByEventAndDateTimeSpecification;
import org.maksim.training.mtapp.repository.specification.ticket.TicketsByUserAndDateTimeSpecification;
import org.maksim.training.mtapp.service.BookingService;
import org.maksim.training.mtapp.service.DiscountService;
import org.maksim.training.mtapp.service.UserService;
import org.maksim.training.mtapp.service.util.PriceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final DiscountService discountService;
    private final UserService userService;

    private final TicketRepository ticketRepository;

    private final double multiplierForHighRatedEvents;
    private final double multiplierForVipSeats;

    @Autowired
    public BookingServiceImpl(DiscountService discountService, UserService userService, TicketRepository ticketRepository,
                              @Value("${event.highrated.multiplier}") double multiplierForHighRatedEvents,
                              @Value("${seats.vip.multiplier}") double multiplierForVipSeats) {
        this.discountService = discountService;
        this.userService = userService;
        this.ticketRepository = ticketRepository;
        this.multiplierForHighRatedEvents = multiplierForHighRatedEvents;
        this.multiplierForVipSeats = multiplierForVipSeats;
    }

    @Override
    public BigDecimal getTicketsPrice(Event event, LocalDateTime dateTime, User user, Collection<Integer> seats) {
        int ticketNumber = 1;
        BigDecimal overallPrice = BigDecimal.ZERO;
        for (Integer seat : seats) {
            overallPrice = overallPrice.add(getTicketPrice(event, dateTime, user, seat, ticketNumber++));
        }
        return overallPrice;
    }

    @Override
    public Collection<Ticket> reserveTickets(Event event, LocalDateTime dateTime, User user, Collection<Integer> seats) {
        List<Ticket> tickets = Lists.newArrayList();
        int ticketNumber = 1;
        for (Integer seat : seats) {
            tickets.add(Ticket.builder()
                    .sellingPrice(getTicketPrice(event, dateTime, user, seat, ticketNumber++))
                    .dateTime(dateTime)
                    .event(event)
                    .user(user)
                    .seat(seat)
                    .build());
        }
        return tickets;
    }

    private BigDecimal getTicketPrice(Event event, LocalDateTime airDateTime, User user, int seat, int ticketNumber) {
        BigDecimal basePrice = calculateBasePrice(event);
        boolean isVipSeat = event.getAuditorium(airDateTime).countVipSeats(Collections.singleton(seat)) > 0;
        byte discount = discountService.getDiscount(user, event, airDateTime, ticketNumber);

        BigDecimal overallPrice = isVipSeat
                ? PriceUtils.multiply(basePrice, multiplierForVipSeats)
                : basePrice;

        return applyDiscount(overallPrice, discount);
    }

    private BigDecimal calculateBasePrice(Event event) {
        BigDecimal basePrice = event.getBasePrice();
        if (EventRating.HIGH.equals(event.getRating())) {
            basePrice = PriceUtils.multiply(basePrice, multiplierForHighRatedEvents);
        }
        return basePrice;
    }

    private BigDecimal applyDiscount(BigDecimal price, byte discount) {
        return PriceUtils.multiply(price, (double) (100 - discount) / 100);
    }

    @Override
    @Transactional
    public void bookTickets(Collection<Ticket> tickets) {
        tickets.stream()
                .filter(t -> t.getUser() != null && t.getUser().getRoles() != null && !t.getUser().getRoles().contains(UserRole.ANONYMOUS))
                .forEach(ticket -> {
                    ticket.getUser().getTickets().add(ticket);
                    userService.save(ticket.getUser());
                });
        ticketRepository.add(tickets);
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