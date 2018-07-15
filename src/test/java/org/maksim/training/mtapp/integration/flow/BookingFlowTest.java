package org.maksim.training.mtapp.integration.flow;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maksim.training.mtapp.Application;
import org.maksim.training.mtapp.model.Auditorium;
import org.maksim.training.mtapp.model.Event;
import org.maksim.training.mtapp.model.EventRating;
import org.maksim.training.mtapp.model.Seance;
import org.maksim.training.mtapp.model.Ticket;
import org.maksim.training.mtapp.model.User;
import org.maksim.training.mtapp.model.UserRole;
import org.maksim.training.mtapp.service.AuditoriumService;
import org.maksim.training.mtapp.service.BookingService;
import org.maksim.training.mtapp.service.CounterService;
import org.maksim.training.mtapp.service.DiscountService;
import org.maksim.training.mtapp.service.EventService;
import org.maksim.training.mtapp.service.PricingService;
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
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@Slf4j
public class BookingFlowTest {
    @Autowired
    private UserService userService;
    @Autowired
    private PricingService pricingService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private EventService eventService;
    @Autowired
    private AuditoriumService auditoriumService;
    @Autowired
    private CounterService counterService;

    private User meUnregistered = User.builder().firstName("Maksim").lastName("Patapenka")
            .email("maksim.patapenka@gmail.com").role(UserRole.REGISTERED_USER)
            .birthday(LocalDate.of(1994, Month.MAY, 13)).build();

    @Before
    public void before() {
        int iteration = 1;
        for (Auditorium auditorium : auditoriumService.getAll()) {
            Event event = Event.builder().name("Event" + iteration++)
                    .rating(EventRating.HIGH)
                    .basePrice(new BigDecimal(100))
                    .build();
            event.getSeances().add(Seance.builder()
                    .dateTime(LocalDateTime.of(LocalDate.now().getYear(), Month.MAY, 13 + iteration, 14, 20))
                    .auditorium(auditorium)
                    .build());
            eventService.save(event);
        }
        log.info("All events: {}", eventService.getAll());
    }

    @Test
    public void wholeBookingFlowWithRegistration() {
        User meRegistered = userService.save(meUnregistered);
        assertNotNull(meRegistered.getId());
        log.info("Registered: {}", meRegistered);

        List<Event> events = eventService.getForDateRange(LocalDateTime.of(LocalDate.now().getYear(), Month.MAY, 14, 0, 0),
                LocalDateTime.of(LocalDate.now().getYear(), Month.MAY, 16, 0, 0));
        log.info("Events for date range: {}", events);

        Event eventToGo = eventService.getByName(events.get(0).getName());
        log.info("Choose event: {}", eventToGo);

        Seance seance = eventToGo.getSeances().last();
        LocalDateTime seanceDateTime = seance.getDateTime();
        Auditorium auditorium = seance.getAuditorium();
        log.info("Air date time is: {}, for the auditorium: {}", seanceDateTime, auditorium);

        Collection<Integer> allSeats = auditorium.getAllSeats();
        log.info("All seats for the event: {}", allSeats);

        List<Integer> seatsToBuy = allSeats.stream().limit(10).collect(Collectors.toList());
        log.info("Seats to buy: {}", seatsToBuy);

        BigDecimal overallPrice = pricingService.calculateTicketsPrice(eventToGo, seanceDateTime, meRegistered, seatsToBuy);
        log.info("Required to pay: {}", overallPrice);

        Collection<Ticket> tickets = bookingService.prepareTickets(eventToGo, seanceDateTime, meRegistered, seatsToBuy);
        log.info("Reserved tickets: {}", tickets);

        bookingService.book(tickets, meRegistered);

        Collection<Ticket> purchasedTicketsForEvent = bookingService.getPurchasedTicketsForEvent(eventToGo, seanceDateTime);
        log.info("All purchased tickets for event: {}", purchasedTicketsForEvent);

        assertEquals(10, purchasedTicketsForEvent.size());
        assertEquals(10, counterService.getBookTimesForEventCount(eventToGo.getName()));
        assertEquals(1, counterService.getEventByNameCount(eventToGo.getName()));
        assertEquals(2, counterService.getOverallDiscountCount(DiscountService.FIFTY_PERCENTAGE_DISCOUNT));
        assertEquals(18, counterService.getOverallDiscountCount(DiscountService.FIVE_PERCENTAGE_DISCOUNT));
        assertEquals(2, counterService.getDiscountForUserCount(meRegistered.getEmail(), DiscountService.FIFTY_PERCENTAGE_DISCOUNT));
        assertEquals(18, counterService.getDiscountForUserCount(meRegistered.getEmail(), DiscountService.FIVE_PERCENTAGE_DISCOUNT));
    }
}