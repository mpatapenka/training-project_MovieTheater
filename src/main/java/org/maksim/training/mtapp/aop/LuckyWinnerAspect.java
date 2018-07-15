package org.maksim.training.mtapp.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.maksim.training.mtapp.model.Ticket;
import org.maksim.training.mtapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Random;

@Slf4j
@Component
@Aspect
public final class LuckyWinnerAspect {
    private static final int PERCENTAGE_TO_BE_LUCKY = 95;

    private final Random random;

    @Autowired
    public LuckyWinnerAspect(Random random) {
        this.random = random;
    }

    @Before(value = "execution(* org.maksim.training.mtapp..*.BookingService.book(..)) && args(tickets, user)",
            argNames = "tickets,user")
    public void checkLuckyWinnerAndUpdatePrice(Collection<Ticket> tickets, User user) {
        int randomPercentage = random.nextInt(100);
        if (randomPercentage >= PERCENTAGE_TO_BE_LUCKY && !tickets.isEmpty()) {
            Ticket ticket = tickets.iterator().next();
            if (user != null) {
                user.getMessages().add("Dude, You are so lucky. You saved " + ticket.getSellingPrice());
            }
            ticket.setSellingPrice(BigDecimal.ZERO);
            log.debug("!!!We have a lucky winner!!!");
        }
    }
}