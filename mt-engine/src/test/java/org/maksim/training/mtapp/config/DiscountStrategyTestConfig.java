package org.maksim.training.mtapp.config;

import org.maksim.training.mtapp.service.strategy.DiscountStrategy;
import org.maksim.training.mtapp.service.strategy.discount.BirthdayDiscountStrategy;
import org.maksim.training.mtapp.service.strategy.discount.TenthTicketDiscountStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class DiscountStrategyTestConfig {
    @Bean
    public DiscountStrategy tenthTicketStrategy(@Value("${strategy.tenthticket.discount}") byte discount) {
        return new TenthTicketDiscountStrategy(discount);
    }

    @Bean
    public DiscountStrategy birthdayStrategy(@Value("${strategy.birthday.dayswithinairdate}") long daysWithinAirDate,
                                             @Value("${strategy.birthday.discount}") byte discount) {
        return new BirthdayDiscountStrategy(daysWithinAirDate, discount);
    }
}