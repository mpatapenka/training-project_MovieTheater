package org.maksim.training.mtapp.service.strategy;

import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.User;

import java.time.LocalDateTime;

public interface DiscountStrategy {
    byte calculateDiscount(User user, Event event, LocalDateTime airDateTime, int numberOfTicket);
}