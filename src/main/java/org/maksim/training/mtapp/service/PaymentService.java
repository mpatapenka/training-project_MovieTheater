package org.maksim.training.mtapp.service;

import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.entity.User;

import java.math.BigDecimal;
import java.util.Collection;

public interface PaymentService {
    void deposit(User user, BigDecimal amount);
    void pay(Collection<Ticket> tickets, User user);
}