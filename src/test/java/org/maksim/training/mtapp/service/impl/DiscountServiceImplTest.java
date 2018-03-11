package org.maksim.training.mtapp.service.impl;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.service.DiscountService;
import org.maksim.training.mtapp.service.strategy.DiscountStrategy;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.maksim.training.mtapp.service.DiscountService.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;

@RunWith(MockitoJUnitRunner.class)
public class DiscountServiceImplTest {
    @Mock private DiscountStrategy mockDiscountStrategy1;
    @Mock private DiscountStrategy mockDiscountStrategy2;

    private DiscountService discountService;

    private User user = User.builder().build();
    private Event event = Event.builder().build();

    @Before
    public void before() {
        discountService = new DiscountServiceImpl(Lists.newArrayList(mockDiscountStrategy1, mockDiscountStrategy2));

        reset(mockDiscountStrategy1);
        reset(mockDiscountStrategy2);
    }

    @Test
    public void noDiscountInCaseNoOneAppliedDiscountStrategy() {
        doReturn(NO_DISCOUNT).when(mockDiscountStrategy1).calculateDiscount(anyObject(), anyObject(), anyObject(), anyInt());
        doReturn(NO_DISCOUNT).when(mockDiscountStrategy2).calculateDiscount(anyObject(), anyObject(), anyObject(), anyInt());

        assertEquals(NO_DISCOUNT, discountService.getDiscount(user, event, LocalDateTime.now(), 5));
    }

    @Test
    public void discountAccordingToOneOfDiscountStrategy() {
        doReturn(NO_DISCOUNT).when(mockDiscountStrategy1).calculateDiscount(anyObject(), anyObject(), anyObject(), anyInt());
        doReturn(FIVE_PERCENTAGE_DISCOUNT).when(mockDiscountStrategy2).calculateDiscount(anyObject(), anyObject(), anyObject(), anyInt());

        assertEquals(FIVE_PERCENTAGE_DISCOUNT, discountService.getDiscount(user, event, LocalDateTime.now(), 5));
    }

    @Test
    public void maximalDiscountAccordingToAllDiscountStrategies() {
        doReturn(FIFTY_PERCENTAGE_DISCOUNT).when(mockDiscountStrategy1).calculateDiscount(anyObject(), anyObject(), anyObject(), anyInt());
        doReturn(FIVE_PERCENTAGE_DISCOUNT).when(mockDiscountStrategy2).calculateDiscount(anyObject(), anyObject(), anyObject(), anyInt());

        assertEquals(FIFTY_PERCENTAGE_DISCOUNT, discountService.getDiscount(user, event, LocalDateTime.now(), 5));
    }
}