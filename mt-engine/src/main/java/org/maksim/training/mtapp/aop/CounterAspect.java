package org.maksim.training.mtapp.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Aspect
@Component
@Slf4j
public final class CounterAspect {
    private final CounterService counterService;

    @Autowired
    public CounterAspect(CounterService counterService) {
        this.counterService = counterService;
    }

    @Before("execution(* org.maksim.training.mtapp.*.EventService.getByName(..)) && args(name)")
    public void countEventByNameAccesses(String name) {
        log.debug("Count event by name: {}", name);
        counterService.countEventByName(name);
    }

    @Before("execution(* org.maksim.training.mtapp.*.BookingService.bookTickets(..)) && args(tickets)")
    public void countHowManyTicketsWereBookedForEvent(Collection<Ticket> tickets) {
        if (tickets != null) {
            log.debug("Count booked tickets for event");
            tickets.stream().filter(t -> t.getEvent() != null && t.getEvent().getName() != null)
                    .map(t -> t.getEvent().getName())
                    .forEach(counterService::countBookTimesForEvent);
        }
    }
}