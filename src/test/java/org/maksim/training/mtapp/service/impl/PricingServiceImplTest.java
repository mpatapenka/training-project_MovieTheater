package org.maksim.training.mtapp.service.impl;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maksim.training.mtapp.entity.Auditorium;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.EventRating;
import org.maksim.training.mtapp.entity.Seance;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.service.DiscountService;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.maksim.training.mtapp.service.DiscountService.FIFTY_PERCENTAGE_DISCOUNT;
import static org.maksim.training.mtapp.service.DiscountService.NO_DISCOUNT;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class PricingServiceImplTest {
    private static final BigDecimal basePrice = new BigDecimal(100);
    private static final User user = User.builder().build();
    private static final Auditorium auditorium = Auditorium.builder()
            .numberOfSeats(6)
            .vipSeats(Sets.newHashSet(1, 2, 3))
            .build();

    private PricingServiceImpl pricingService;

    @Mock
    private DiscountService mockDiscountService;

    @Before
    public void before() {
        pricingService = new PricingServiceImpl(mockDiscountService);
        pricingService.setMultiplierForHighRatedEvents(1.2);
        pricingService.setMultiplierForVipSeats(2);
    }

    @Test
    public void checkTotalPriceInCaseNoDiscountAndOnlyRegularSeatsAndNotHighRatingEvent() {
        doReturn(NO_DISCOUNT).when(mockDiscountService).getDiscount(anyObject(), anyObject(), anyObject(), anyInt());

        LocalDateTime now = LocalDateTime.now();
        Event event = createStubEvent(now, EventRating.MID);
        Set<Integer> seats = Sets.newHashSet(4, 5, 6);

        assertEquals(0, new BigDecimal(300).compareTo(pricingService.calculateTicketsPrice(event, now, user, seats)));
    }

    @Test
    public void checkTotalPriceInCaseNoDiscountAndOnlyRegularSeatsAndHighRatingEvent() {
        doReturn(NO_DISCOUNT).when(mockDiscountService).getDiscount(anyObject(), anyObject(), anyObject(), anyInt());

        LocalDateTime now = LocalDateTime.now();
        Event event = createStubEvent(now, EventRating.HIGH);
        Set<Integer> seats = Sets.newHashSet(4, 5, 6);

        assertEquals(0, new BigDecimal(360).compareTo(pricingService.calculateTicketsPrice(event, now, user, seats)));
    }

    @Test
    public void checkTotalPriceInCaseDiscountAndOnlyRegularSeatsAndNotHighRatingEvent() {
        doReturn(FIFTY_PERCENTAGE_DISCOUNT).when(mockDiscountService).getDiscount(anyObject(), anyObject(), anyObject(), anyInt());

        LocalDateTime now = LocalDateTime.now();
        Event event = createStubEvent(now, EventRating.LOW);
        Set<Integer> seats = Sets.newHashSet(4, 5, 6);

        assertEquals(0, new BigDecimal(150).compareTo(pricingService.calculateTicketsPrice(event, now, user, seats)));
    }

    @Test
    public void checkTotalPriceInCaseDiscountAndOnlyRegularSeatsAndHighRatingEvent() {
        doReturn(FIFTY_PERCENTAGE_DISCOUNT).when(mockDiscountService).getDiscount(anyObject(), anyObject(), anyObject(), anyInt());

        LocalDateTime now = LocalDateTime.now();
        Event event = createStubEvent(now, EventRating.HIGH);
        Set<Integer> seats = Sets.newHashSet(4, 5, 6);

        assertEquals(0, new BigDecimal(180).compareTo(pricingService.calculateTicketsPrice(event, now, user, seats)));
    }

    @Test
    public void checkTotalPriceInCaseDiscountAndVipAndRegularSeatsAndHighRatingEvent() {
        doReturn(FIFTY_PERCENTAGE_DISCOUNT).when(mockDiscountService).getDiscount(anyObject(), anyObject(), anyObject(), anyInt());

        LocalDateTime now = LocalDateTime.now();
        Event event = createStubEvent(now, EventRating.HIGH);
        Set<Integer> seats = Sets.newHashSet(3, 4, 5);

        assertEquals(0, new BigDecimal(240).compareTo(pricingService.calculateTicketsPrice(event, now, user, seats)));
    }

    private Event createStubEvent(LocalDateTime airDateTime, EventRating rating) {
        Event event = Event.builder().basePrice(basePrice).rating(rating).build();
        event.getSeances().add(Seance.builder().dateTime(airDateTime).auditorium(auditorium).build());
        return event;
    }
}