package org.maksim.training.mtapp.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.maksim.training.mtapp.entity.Message;
import org.maksim.training.mtapp.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Random;

@Aspect
@Component
@Slf4j
public final class LuckyWinnerAspect {
    private static final int PERCENTAGE_TO_BE_LUCKY = 95;

    private final Random random;

    @Autowired
    public LuckyWinnerAspect(Random random) {
        this.random = random;
    }

    @Before("execution(* org.maksim.training.mtapp.*.BookingService.bookTickets(..)) && args(tickets)")
    public void checkLuckyWinnerAndUpdatePrice(Collection<Ticket> tickets) {
        int randomPercentage = random.nextInt(100);
        if (randomPercentage >= PERCENTAGE_TO_BE_LUCKY && !tickets.isEmpty()) {
            Ticket ticket = tickets.iterator().next();
            if (ticket.getUser() != null) {
                ticket.getUser().getMessages().add(Message.builder()
                        .text("Dude, You are so lucky. You saved " + ticket.getSellingPrice()).build());
            }
            ticket.setSellingPrice(BigDecimal.ZERO);
            log.debug("!!!We have a lucky winner!!!");
        }
    }
}