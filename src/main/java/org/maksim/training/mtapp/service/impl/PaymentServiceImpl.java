package org.maksim.training.mtapp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.entity.UserAccount;
import org.maksim.training.mtapp.service.PaymentService;
import org.maksim.training.mtapp.service.PricingService;
import org.maksim.training.mtapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    private final UserService userService;
    private final PricingService pricingService;

    @Autowired
    public PaymentServiceImpl(UserService userService, PricingService pricingService) {
        this.userService = userService;
        this.pricingService = pricingService;
    }

    @Override
    @Transactional
    public void deposit(User user, BigDecimal amount) {
        User actualUser = findActualUser(user);
        UserAccount account;
        if ((account = actualUser.getUserAccount()) == null) {
            account = UserAccount.builder().build();
            actualUser.setUserAccount(account);
        }
        account.setAmount(account.getAmount().add(amount));
        userService.save(actualUser);
    }

    @Override
    @Transactional
    public void pay(Collection<Ticket> tickets, User tempUser) {
        User user = findActualUser(tempUser);
        if (user == null) {
            log.warn("Tickets: {}, could not be paid by user account. User is not specified.", tickets);
        } else {
            tickets.forEach(t -> {
                t.setUser(user);
                t.setAmountPaid(t.getSellingPrice());
            });
            user.getTickets().addAll(tickets);
            withdraw(user, pricingService.calculateTicketsPrice(tickets));
        }
    }

    private void withdraw(User user, BigDecimal amount) {
        UserAccount account = Optional.ofNullable(user.getUserAccount())
                .filter(acc -> acc.getAmount().compareTo(amount) >= 0).orElseThrow(() -> {
                    user.setPassword("");
                    return new RuntimeException("Actual user: " + user + " doesn't have valid account for withdraw operation." +
                            " Account doesn't exists or credit is not enough to cover withdraw amount: " + amount + ".");
                });
        account.setAmount(account.getAmount().subtract(amount));
        userService.save(user);
    }

    private User findActualUser(User user) {
        return Optional.ofNullable(user).map(User::getId).map(userService::getById)
                .orElseGet(() -> Optional.ofNullable(user).map(User::getEmail).map(userService::getByEmail)
                        .orElse(null));
    }
}