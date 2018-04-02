package org.maksim.training.mtapp.service.impl;

import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.EventRating;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.service.DiscountService;
import org.maksim.training.mtapp.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Service
public class PricingServiceImpl implements PricingService {
    private final DiscountService discountService;

    private double multiplierForHighRatedEvents = 1;
    private double multiplierForVipSeats = 1;

    @Autowired
    public PricingServiceImpl(DiscountService discountService) {
        this.discountService = discountService;
    }

    @Value("${event.highrated.multiplier}")
    public void setMultiplierForHighRatedEvents(double multiplierForHighRatedEvents) {
        this.multiplierForHighRatedEvents = multiplierForHighRatedEvents;
    }

    @Value("${seats.vip.multiplier}")
    public void setMultiplierForVipSeats(double multiplierForVipSeats) {
        this.multiplierForVipSeats = multiplierForVipSeats;
    }

    @Override
    public BigDecimal calculateTicketPrice(Event event, LocalDateTime dateTime, User user, int seat, int ticketNumber) {
        BigDecimal basePrice = calculateBasePrice(event);
        boolean isVipSeat = event.getAuditorium(dateTime).countVipSeats(Collections.singleton(seat)) > 0;
        byte discount = discountService.getDiscount(user, event, dateTime, ticketNumber);

        BigDecimal overallPrice = isVipSeat
                ? multiply(basePrice, multiplierForVipSeats)
                : basePrice;

        return applyDiscount(overallPrice, discount);
    }

    @Override
    public BigDecimal calculateTicketsPrice(Event event, LocalDateTime dateTime, User user, Collection<Integer> seats) {
        int ticketNumber = 1;
        BigDecimal overallPrice = BigDecimal.ZERO;
        for (Integer seat : seats) {
            overallPrice = overallPrice.add(calculateTicketPrice(event, dateTime, user, seat, ticketNumber++));
        }
        return overallPrice;
    }

    @Override
    public BigDecimal calculateTicketsPrice(Collection<Ticket> tickets) {
        return tickets.stream().map(Ticket::getSellingPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateBasePrice(Event event) {
        BigDecimal basePrice = event.getBasePrice();
        if (EventRating.HIGH.equals(event.getRating())) {
            basePrice = multiply(basePrice, multiplierForHighRatedEvents);
        }
        return basePrice;
    }

    private BigDecimal applyDiscount(BigDecimal price, byte discount) {
        if (discount == DiscountService.NO_DISCOUNT) {
            return price;
        }
        return multiply(price, (double) (100 - discount) / 100);
    }

    private BigDecimal multiply(BigDecimal price, double multiplier) {
        return price.multiply(new BigDecimal(multiplier)).setScale(1, BigDecimal.ROUND_HALF_UP);
    }
}