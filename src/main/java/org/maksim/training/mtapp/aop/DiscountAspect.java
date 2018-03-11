package org.maksim.training.mtapp.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public final class DiscountAspect {
    private final CounterService counterService;

    @Autowired
    public DiscountAspect(CounterService counterService) {
        this.counterService = counterService;
    }

    @AfterReturning(value = "execution(* org.maksim.training.mtapp.*.DiscountService.getDiscount(..)) && args(user, ..)",
            returning = "discount")
    public void calculateOveralDiscounts(User user, byte discount) {
        if (user != null && user.getEmail() != null && discount > 0) {
            log.debug("Count discounts count, email: {}, discount: {}", user.getEmail(), discount);
            counterService.countDiscountForUser(user.getEmail(), discount);
        }
    }
}