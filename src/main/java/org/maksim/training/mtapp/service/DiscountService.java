package org.maksim.training.mtapp.service;

import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.User;

import java.time.LocalDateTime;

public interface DiscountService {
    byte NO_DISCOUNT = 0;
    byte FIVE_PERCENTAGE_DISCOUNT = 5;
    byte FIFTY_PERCENTAGE_DISCOUNT = 50;

    byte getDiscount(User user, Event event, LocalDateTime airDateTime, int numberOfTicket);
}