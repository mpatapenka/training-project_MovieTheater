package org.maksim.training.mtapp.service;

import org.maksim.training.mtapp.entity.User;

import java.math.BigDecimal;

public interface PaymentService {
    void deposit(User user, BigDecimal amount);
    void withdraw(User user, BigDecimal amount);
}