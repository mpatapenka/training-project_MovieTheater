package org.maksim.training.mtapp.service.strategy.discount;

import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.service.DiscountService;
import org.maksim.training.mtapp.service.annotation.DiscountStrategyQualifier;
import org.maksim.training.mtapp.service.strategy.DiscountStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@DiscountStrategyQualifier
public class BirthdayDiscountStrategy implements DiscountStrategy {
    private final long daysWithinAirDate;
    private final byte discount;

    @Autowired
    public BirthdayDiscountStrategy(@Value("${strategy.birthday.dayswithinairdate}") long daysWithinAirDate,
            @Value("${strategy.birthday.discount}") byte discount) {
        this.daysWithinAirDate = daysWithinAirDate;
        this.discount = discount;
    }

    @Override
    public byte calculateDiscount(User user, Event event, LocalDateTime airDateTime, int numberOfTicket) {
        if (user != null && user.getBirthday() != null
                && user.getBirthday().withYear(airDateTime.getYear()).compareTo(airDateTime.minusDays(daysWithinAirDate).toLocalDate()) >= 0
                && user.getBirthday().withYear(airDateTime.getYear()).compareTo(airDateTime.plusDays(daysWithinAirDate).toLocalDate()) <= 0) {
            return discount;
        }
        return DiscountService.NO_DISCOUNT;
    }
}