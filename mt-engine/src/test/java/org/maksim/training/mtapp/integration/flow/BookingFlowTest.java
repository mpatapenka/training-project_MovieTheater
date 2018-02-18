package org.maksim.training.mtapp.integration.flow;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maksim.training.mtapp.config.AppConfig;
import org.maksim.training.mtapp.entity.Auditorium;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.EventRating;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.entity.UserRole;
import org.maksim.training.mtapp.service.AuditoriumService;
import org.maksim.training.mtapp.service.BookingService;
import org.maksim.training.mtapp.service.EventService;
import org.maksim.training.mtapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Slf4j
public class BookingFlowTest {
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private EventService eventService;
    @Autowired
    private AuditoriumService auditoriumService;

    @Before
    public void before() {
        int iteration = 1;
        for (Auditorium auditorium : auditoriumService.getAll()) {
            Event event = Event.builder().name("Event" + iteration++)
                    .rating(EventRating.HIGH)
                    .basePrice(new BigDecimal(100))
                    .build();
            event.getSeances().put(LocalDateTime.of(LocalDate.now().getYear(), Month.MAY, 13 + iteration, 14, 20), auditorium);
            eventService.save(event);
        }

        log.info("All events: {}", eventService.getAll());
    }

    private User meUnregistered = User.builder().firstName("Maksim").lastName("Patapenka")
            .email("maksim.patapenka@gmail.com").role(UserRole.USER)
            .birthday(LocalDate.of(1994, Month.MAY, 13)).build();

    @Test
    public void wholeBookingFlowWithRegistration() {
        User meRegistered = userService.save(meUnregistered);
        assertNotNull(meRegistered.getId());
        log.info("Registered: {}", meRegistered);

        List<Event> events = eventService.getForDateRange(LocalDateTime.of(LocalDate.now().getYear(), Month.MAY, 14, 0, 0),
                LocalDateTime.of(LocalDate.now().getYear(), Month.MAY, 16, 0, 0));
        log.info("Events for date range: {}", events);

        Event eventToGo = events.get(0);
        log.info("Choose event: {}", eventToGo);

        Map.Entry<LocalDateTime, Auditorium> seance = eventToGo.getSeances().lastEntry();
        log.info("Air date time is: {}, for the auditorium: {}", seance.getKey(), seance.getValue());

        Set<Integer> allSeats = seance.getValue().getAllSeats();
        log.info("All seats for the event: {}", allSeats);

        List<Integer> seatsToBuy = allSeats.stream().limit(10).collect(Collectors.toList());
        log.info("Seats to buy: {}", seatsToBuy);

        BigDecimal overallPrice = bookingService.getTicketsPrice(eventToGo, seance.getKey(), meRegistered, seatsToBuy);
        log.info("Required to pay: {}", overallPrice);

        Collection<Ticket> tickets = bookingService.reserveTickets(eventToGo, seance.getKey(), meRegistered, seatsToBuy);
        log.info("Reserved tickets: {}", tickets);

        bookingService.bookTickets(tickets);

        Collection<Ticket> purchasedTicketsForEvent = bookingService.getPurchasedTicketsForEvent(eventToGo, seance.getKey());
        log.info("All purchased tickets for event: {}", purchasedTicketsForEvent);

        assertEquals(10, purchasedTicketsForEvent.size());
    }
}