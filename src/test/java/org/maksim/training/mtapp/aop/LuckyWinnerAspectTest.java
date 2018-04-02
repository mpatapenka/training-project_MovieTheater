package org.maksim.training.mtapp.aop;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maksim.training.mtapp.config.AopTestConfig;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AopTestConfig.class)
public class LuckyWinnerAspectTest {
    @Autowired
    private Random spiedRandom;
    @Autowired
    private BookingService bookingService;

    private User user = User.builder().firstName("Maksim").lastName("Patapenka")
            .email("maksim.patapenka@gmail.com").build();
    private Event event = Event.builder().name("event").build();
    private LocalDateTime dateTime = LocalDateTime.now();

    @Before
    public void before() {
        reset(spiedRandom);
    }

    @Test
    public void checkThatUnLuckyTicketPriceNotChange() {
        doReturn(90).when(spiedRandom).nextInt(anyInt());

        Collection<Ticket> ticketsToBook = Sets.newHashSet(
                Ticket.builder().user(user).event(event).dateTime(dateTime).seat(13).sellingPrice(BigDecimal.TEN).build()
        );

        bookingService.book(ticketsToBook, user);
        Ticket ticketToCheck = bookingService.getPurchasedTicketsForEvent(event, dateTime).stream()
                .filter(t -> t.getSeat() == 13).findFirst().orElse(null);

        assertNotNull(ticketToCheck);
        assertEquals(BigDecimal.TEN, ticketToCheck.getSellingPrice());
    }

    @Test
    public void checkThatLuckyTicketPriceIsSetToZeroAndMessageAdded() {
        doReturn(99).when(spiedRandom).nextInt(anyInt());

        Collection<Ticket> ticketsToBook = Sets.newHashSet(
                Ticket.builder().user(user).event(event).dateTime(dateTime).seat(13).sellingPrice(BigDecimal.TEN).build()
        );

        bookingService.book(ticketsToBook, user);
        Ticket ticketToCheck = bookingService.getPurchasedTicketsForEvent(event, dateTime).stream()
                .filter(t -> t.getSeat() == 13).findFirst().orElse(null);

        assertNotNull(ticketToCheck);
        assertEquals(BigDecimal.ZERO, ticketToCheck.getSellingPrice());
    }
}