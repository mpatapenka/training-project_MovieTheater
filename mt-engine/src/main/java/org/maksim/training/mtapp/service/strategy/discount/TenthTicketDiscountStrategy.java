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
public class TenthTicketDiscountStrategy implements DiscountStrategy {
    private static final int TICKETS_TO_GIVE_DISCOUNT = 10;

    private final byte discount;

    @Autowired
    public TenthTicketDiscountStrategy(@Value("${strategy.tenthticket.discount}") byte discount) {
        this.discount = discount;
    }

    @Override
    public byte calculateDiscount(User user, Event event, LocalDateTime airDateTime, int numberOfTicket) {
        // Check discount for unregistered user
        if ((user == null && isTenthTicket(numberOfTicket))
                // Check discount for registered user
                || (user != null && isTenthTicket (user.getTickets().size() + numberOfTicket))) {
            return discount;
        }
        return DiscountService.NO_DISCOUNT;
    }

    private boolean isTenthTicket(int numberOfTicket) {
        return numberOfTicket / TICKETS_TO_GIVE_DISCOUNT >= 1
                && numberOfTicket % TICKETS_TO_GIVE_DISCOUNT == 0;
    }
}