package org.maksim.training.mtapp.service.strategy.discount;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.maksim.training.mtapp.config.DiscountStrategyTestConfig;
import org.maksim.training.mtapp.model.Event;
import org.maksim.training.mtapp.model.User;
import org.maksim.training.mtapp.service.strategy.DiscountStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.maksim.training.mtapp.service.DiscountService.FIVE_PERCENTAGE_DISCOUNT;
import static org.maksim.training.mtapp.service.DiscountService.NO_DISCOUNT;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DiscountStrategyTestConfig.class)
public class BirthdayDiscountStrategyTest {
    @Autowired
    private DiscountStrategy birthdayStrategy;

    private User user = User.builder().firstName("Maksim").build();
    private Event event = Event.builder().name("Godzilla").build();

    @Test
    public void noDiscountInCaseNotRegisteredUser() {
        assertEquals(NO_DISCOUNT, birthdayStrategy.calculateDiscount(null, event, LocalDateTime.now(), 0));
    }

    @Test
    public void noDiscountIfUserDidNotSetBirthday() {
        assertEquals(NO_DISCOUNT, birthdayStrategy.calculateDiscount(user, event, LocalDateTime.now(), 0));
    }

    @Test
    public void noDiscountIfUserBirthdayBeforeAirDateNotWithinDiscountDays() {
        LocalDate birthday = LocalDate.now();
        LocalDateTime airDate = birthday.atStartOfDay().minusDays(5);
        User user = User.builder().birthday(birthday).build();
        assertEquals(NO_DISCOUNT, birthdayStrategy.calculateDiscount(user, event, airDate, 0));
    }

    @Test
    public void noDiscountIfUserBirthdayAfterAirDateNotWithinDiscountDays() {
        LocalDate birthday = LocalDate.now();
        LocalDateTime airDate = birthday.atStartOfDay().plusDays(5);
        User user = User.builder().birthday(birthday).build();
        assertEquals(NO_DISCOUNT, birthdayStrategy.calculateDiscount(user, event, airDate, 0));
    }

    @Test
    public void discountIfUserBirthdayBeforeAirDateWithinDiscountDays() {
        LocalDate birthday = LocalDate.now();
        LocalDateTime airDate = birthday.atStartOfDay().plusDays(1);
        User user = User.builder().birthday(birthday).build();
        assertEquals(FIVE_PERCENTAGE_DISCOUNT, birthdayStrategy.calculateDiscount(user, event, airDate, 0));
    }

    @Test
    public void discountIfUserBirthdayAfterAirDateWithinDiscountDays() {
        LocalDate birthday = LocalDate.now();
        LocalDateTime airDate = birthday.atStartOfDay().minusDays(1);
        User user = User.builder().birthday(birthday).build();
        assertEquals(FIVE_PERCENTAGE_DISCOUNT, birthdayStrategy.calculateDiscount(user, event, airDate, 0));
    }
}