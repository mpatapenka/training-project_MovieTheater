package org.maksim.training.mtapp.service.impl;

import org.maksim.training.mtapp.model.Event;
import org.maksim.training.mtapp.model.User;
import org.maksim.training.mtapp.service.DiscountService;
import org.maksim.training.mtapp.service.annotation.DiscountStrategyQualifier;
import org.maksim.training.mtapp.service.strategy.DiscountStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final Collection<DiscountStrategy> discountStrategies;

    @DiscountStrategyQualifier
    public DiscountServiceImpl(Collection<DiscountStrategy> discountStrategies) {
        this.discountStrategies = discountStrategies;
    }

    @Override
    public byte getDiscount(User user, Event event, LocalDateTime airDateTime, int numberOfTicket) {
        return discountStrategies.stream()
                .map(ds -> ds.calculateDiscount(user, event, airDateTime, numberOfTicket))
                .max(Byte::compareTo).orElse(NO_DISCOUNT);
    }
}