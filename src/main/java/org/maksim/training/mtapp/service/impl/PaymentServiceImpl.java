package org.maksim.training.mtapp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.entity.UserAccount;
import org.maksim.training.mtapp.service.PaymentService;
import org.maksim.training.mtapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    private final UserService userService;

    @Autowired
    public PaymentServiceImpl(UserService userService) {
        this.userService = userService;
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
    public void withdraw(User user, BigDecimal amount) {
        User actualUser = findActualUser(user);
        UserAccount account = Optional.ofNullable(actualUser.getUserAccount())
                .filter(acc -> acc.getAmount().compareTo(amount) >= 0).orElseThrow(() -> {
                    log.error("Actual user: {} doesn't have valid account for withdraw operation." +
                            " Account doesn't exists or credit is not enough to cover withdraw amount: {}.", user, amount);
                    return new RuntimeException();
                });
        account.setAmount(account.getAmount().subtract(amount));
        userService.save(actualUser);
    }

    private User findActualUser(User user) {
        return Optional.ofNullable(user).map(User::getId).map(userService::getById)
                .orElseGet(() -> Optional.ofNullable(user).map(User::getEmail).map(userService::getByEmail)
                        .orElseThrow(() -> {
                            log.error("Actual user not found by id and email from: {}.", user);
                            return new RuntimeException();
                        }));
    }
}