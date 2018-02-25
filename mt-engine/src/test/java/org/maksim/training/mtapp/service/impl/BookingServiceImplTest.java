package org.maksim.training.mtapp.service.impl;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maksim.training.mtapp.entity.Auditorium;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.EventRating;
import org.maksim.training.mtapp.entity.Seance;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.repository.TicketRepository;
import org.maksim.training.mtapp.service.BookingService;
import org.maksim.training.mtapp.service.DiscountService;
import org.maksim.training.mtapp.service.UserService;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.maksim.training.mtapp.service.DiscountService.FIFTY_PERCENTAGE_DISCOUNT;
import static org.maksim.training.mtapp.service.DiscountService.NO_DISCOUNT;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceImplTest {
    private static final BigDecimal basePrice = new BigDecimal(100);

    private User user = User.builder().build();
    private Event event = Event.builder().build();
    private Auditorium mockAuditorium = Auditorium.builder()
            .numberOfSeats(6)
            .vipSeats(Sets.newHashSet(1, 2, 3))
            .build();

    @Mock private DiscountService mockDiscountService;
    @Mock private UserService mockUserService;
    @Mock private TicketRepository mockTicketRepository;

    private BookingService bookingService;

    @Before
    public void before() {
        bookingService = new BookingServiceImpl(mockDiscountService, mockUserService,mockTicketRepository, 1.2, 2);

        reset(mockDiscountService);
        reset(mockUserService);
        reset(mockTicketRepository);
    }

    @Test
    public void checkTotalPriceInCaseNoDiscountAndOnlyRegularSeatsAndNotHighRatingEvent() {
        doReturn(NO_DISCOUNT).when(mockDiscountService).getDiscount(anyObject(), anyObject(), anyObject(), anyInt());

        LocalDateTime now = LocalDateTime.now();
        Event event = createStubEvent(now, EventRating.MID);
        Set<Integer> seats = Sets.newHashSet(4, 5, 6);

        assertTrue(new BigDecimal(300).compareTo(bookingService.getTicketsPrice(event, now, user, seats)) == 0);
    }

    @Test
    public void checkTotalPriceInCaseNoDiscountAndOnlyRegularSeatsAndHighRatingEvent() {
        doReturn(NO_DISCOUNT).when(mockDiscountService).getDiscount(anyObject(), anyObject(), anyObject(), anyInt());

        LocalDateTime now = LocalDateTime.now();
        Event event = createStubEvent(now, EventRating.HIGH);
        Set<Integer> seats = Sets.newHashSet(4, 5, 6);

        assertTrue(new BigDecimal(360).compareTo(bookingService.getTicketsPrice(event, now, user, seats)) == 0);
    }

    @Test
    public void checkTotalPriceInCaseDiscountAndOnlyRegularSeatsAndNotHighRatingEvent() {
        doReturn(FIFTY_PERCENTAGE_DISCOUNT).when(mockDiscountService).getDiscount(anyObject(), anyObject(), anyObject(), anyInt());

        LocalDateTime now = LocalDateTime.now();
        Event event = createStubEvent(now, EventRating.LOW);
        Set<Integer> seats = Sets.newHashSet(4, 5, 6);

        assertTrue(new BigDecimal(150).compareTo(bookingService.getTicketsPrice(event, now, user, seats)) == 0);
    }

    @Test
    public void checkTotalPriceInCaseDiscountAndOnlyRegularSeatsAndHighRatingEvent() {
        doReturn(FIFTY_PERCENTAGE_DISCOUNT).when(mockDiscountService).getDiscount(anyObject(), anyObject(), anyObject(), anyInt());

        LocalDateTime now = LocalDateTime.now();
        Event event = createStubEvent(now, EventRating.HIGH);
        Set<Integer> seats = Sets.newHashSet(4, 5, 6);

        assertTrue(new BigDecimal(180).compareTo(bookingService.getTicketsPrice(event, now, user, seats)) == 0);
    }

    @Test
    public void checkTotalPriceInCaseDiscountAndVipAndRegularSeatsAndHighRatingEvent() {
        doReturn(FIFTY_PERCENTAGE_DISCOUNT).when(mockDiscountService).getDiscount(anyObject(), anyObject(), anyObject(), anyInt());

        LocalDateTime now = LocalDateTime.now();
        Event event = createStubEvent(now, EventRating.HIGH);
        Set<Integer> seats = Sets.newHashSet(3, 4, 5);

        assertTrue(new BigDecimal(240).compareTo(bookingService.getTicketsPrice(event, now, user, seats)) == 0);
    }

    @Test
    public void checkThatBookTicketsIsStoreNeededInformationForUnregisteredUser() {
        LocalDateTime now = LocalDateTime.now();
        Set<Ticket> tickets = Sets.newHashSet(
                Ticket.builder().event(event).dateTime(now).seat(13).build(),
                Ticket.builder().event(event).dateTime(now).seat(14).build(),
                Ticket.builder().event(event).dateTime(now).seat(15).build()
        );

        bookingService.bookTickets(tickets);

        verify(mockTicketRepository, times(1)).add(anyCollectionOf(Ticket.class));
    }

    @Test
    public void checkThatBookTicketsIsStoreNeededInformationForRegisteredUser() {
        LocalDateTime now = LocalDateTime.now();
        Set<Ticket> tickets = Sets.newHashSet(
                Ticket.builder().user(user).event(event).dateTime(now).seat(13).build(),
                Ticket.builder().user(user).event(event).dateTime(now).seat(14).build(),
                Ticket.builder().event(event).dateTime(now).seat(15).build()
        );

        bookingService.bookTickets(tickets);

        verify(mockUserService, times(2)).save(any());
        verify(mockTicketRepository, times(1)).add(anyCollectionOf(Ticket.class));
    }

    private Event createStubEvent(LocalDateTime airDateTime, EventRating rating) {
        Event event = Event.builder().basePrice(basePrice).rating(rating).build();
        event.getSeances().add(Seance.builder().dateTime(airDateTime).auditorium(mockAuditorium).build());
        return event;
    }
}