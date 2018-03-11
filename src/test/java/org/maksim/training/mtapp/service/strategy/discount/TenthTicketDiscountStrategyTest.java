package org.maksim.training.mtapp.service.strategy.discount;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.maksim.training.mtapp.config.DiscountStrategyTestConfig;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.service.strategy.DiscountStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.maksim.training.mtapp.service.DiscountService.FIFTY_PERCENTAGE_DISCOUNT;
import static org.maksim.training.mtapp.service.DiscountService.NO_DISCOUNT;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DiscountStrategyTestConfig.class)
public class TenthTicketDiscountStrategyTest {
    @Autowired
    private DiscountStrategy tenthTicketStrategy;

    private User user = User.builder().firstName("Maksim").build();
    private Event event = Event.builder().name("Godzilla").build();

    @Test
    public void noDiscountInCaseNotRegisteredUserAndNotEnoughTickets() {
        assertEquals(NO_DISCOUNT, tenthTicketStrategy.calculateDiscount(null, event, LocalDateTime.now(), 5));
    }

    @Test
    public void noDiscountIfUserDidNotBuyEnoughTickets() {
        assertEquals(NO_DISCOUNT, tenthTicketStrategy.calculateDiscount(user, event, LocalDateTime.now(), 8));
    }

    @Test
    public void discountInCaseNotRegisteredUserIfHeBoughtEnoughTickets() {
        assertEquals(FIFTY_PERCENTAGE_DISCOUNT, tenthTicketStrategy.calculateDiscount(null, event, LocalDateTime.now(), 10));
    }

    @Test
    public void discountInCaseNotRegisteredUserIfHeBoughtMoreThanEnoughTickets() {
        assertEquals(FIFTY_PERCENTAGE_DISCOUNT, tenthTicketStrategy.calculateDiscount(null, event, LocalDateTime.now(), 20));
    }

    @Test
    public void noDiscountForNotTenthTicket() {
        assertEquals(NO_DISCOUNT, tenthTicketStrategy.calculateDiscount(null, event, LocalDateTime.now(), 21));
    }

    @Test
    public void noDiscountInCaseRegisteredUserBoughtEnoughTickets() {
        user.getTickets().addAll(IntStream.range(0, 6)
                .mapToObj(seat -> Ticket.builder().event(event).dateTime(LocalDateTime.now().plusDays(5)).seat(seat).build())
                .collect(Collectors.toSet()));
        assertEquals(NO_DISCOUNT, tenthTicketStrategy.calculateDiscount(user, event, LocalDateTime.now(), 5));
    }

    @Test
    public void noDiscountInCaseRegisteredUserBoughtNotEnoughTickets() {
        user.getTickets().addAll(IntStream.range(0, 6)
                .mapToObj(seat -> Ticket.builder().event(event).dateTime(LocalDateTime.now().plusDays(5)).seat(seat).build())
                .collect(Collectors.toSet()));
        assertEquals(NO_DISCOUNT, tenthTicketStrategy.calculateDiscount(user, event, LocalDateTime.now(), 3));
    }

    @Test
    public void discountInCaseRegisteredUserBoughtExactTenTickets() {
        user.getTickets().addAll(IntStream.range(0, 9)
                .mapToObj(seat -> Ticket.builder().event(event).dateTime(LocalDateTime.now().plusDays(5)).seat(seat).build())
                .collect(Collectors.toSet()));
        assertEquals(FIFTY_PERCENTAGE_DISCOUNT, tenthTicketStrategy.calculateDiscount(user, event, LocalDateTime.now(), 1));
    }
}