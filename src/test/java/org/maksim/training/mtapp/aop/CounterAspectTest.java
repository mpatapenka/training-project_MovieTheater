package org.maksim.training.mtapp.aop;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maksim.training.mtapp.config.AopTestConfig;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.service.BookingService;
import org.maksim.training.mtapp.service.CounterService;
import org.maksim.training.mtapp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AopTestConfig.class)
public class CounterAspectTest {
    @Autowired
    private CounterService mockCounterService;
    @Autowired
    private EventService eventService;
    @Autowired
    private BookingService bookingService;

    @Before
    public void before() {
        eventService.save(Event.builder().name("event1").build());
        eventService.save(Event.builder().name("event2").build());
        eventService.save(Event.builder().name("event3").build());

        reset(mockCounterService);
    }

    @Test
    public void checkHowManyTimesEventGetNameCalled() {
        eventService.getByName("event1");
        eventService.getByName("event1");

        eventService.getByName("event2");

        eventService.getByName("event3");
        eventService.getByName("event3");
        eventService.getByName("event3");

        verify(mockCounterService, times(2)).countEventByName("event1");
        verify(mockCounterService, times(1)).countEventByName("event2");
        verify(mockCounterService, times(3)).countEventByName("event3");
    }

    @Test
    public void checkHowManyTimesTicketsWereBookedForEvent() {
        Event event1 = eventService.getByName("event1");
        Event event2 = eventService.getByName("event2");
        Event event3 = eventService.getByName("event3");

        Set<Ticket> ticketsToBook = Sets.newHashSet(
                Ticket.builder().event(event1).dateTime(LocalDateTime.now()).seat(1).build(),
                Ticket.builder().event(event1).dateTime(LocalDateTime.now()).seat(2).build(),
                Ticket.builder().event(event2).dateTime(LocalDateTime.now()).seat(3).build(),
                Ticket.builder().event(event3).dateTime(LocalDateTime.now()).seat(4).build(),
                Ticket.builder().event(event3).dateTime(LocalDateTime.now()).seat(5).build(),
                Ticket.builder().event(event3).dateTime(LocalDateTime.now()).seat(6).build()
        );

        bookingService.book(ticketsToBook, null);

        verify(mockCounterService, times(2)).countBookTimesForEvent("event1");
        verify(mockCounterService, times(1)).countBookTimesForEvent("event2");
        verify(mockCounterService, times(3)).countBookTimesForEvent("event3");
    }
}