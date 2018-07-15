package org.maksim.training.mtapp.service.impl;

import org.maksim.training.mtapp.model.Counter;
import org.maksim.training.mtapp.repository.CounterRepository;
import org.maksim.training.mtapp.repository.specification.counter.CounterByDomainAndIdentifierSpecification;
import org.maksim.training.mtapp.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CounterServiceImpl implements CounterService {
    private static final String EVENT_BY_NAME_DOMAIN = "EVENT_BY_NAME";
    private static final String BOOK_TIMES_FOR_EVENT_DOMAIN = "BOOK_TIMES_FOR_EVENT";
    private static final String DISCOUNT_PERCENTAGE_DOMAIN = "DISCOUNT_PERCENTAGE";

    private final Lock eventNameLock = new ReentrantLock();
    private final Lock bookTimesLock = new ReentrantLock();
    private final Lock discountsLock = new ReentrantLock();

    private final CounterRepository counterRepository;

    @Autowired
    public CounterServiceImpl(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    @Override
    @Transactional
    public void countEventByName(String name) {
        safeIncrementCount(EVENT_BY_NAME_DOMAIN, name, eventNameLock);
    }

    @Override
    @Transactional(readOnly = true)
    public long getEventByNameCount(String name) {
        return safeGetCount(counterRepository
                .query(new CounterByDomainAndIdentifierSpecification(EVENT_BY_NAME_DOMAIN, name)));
    }

    @Override
    @Transactional
    public void countBookTimesForEvent(String name) {
        safeIncrementCount(BOOK_TIMES_FOR_EVENT_DOMAIN, name, bookTimesLock);
    }

    @Override
    @Transactional(readOnly = true)
    public long getBookTimesForEventCount(String name) {
        return safeGetCount(counterRepository
                .query(new CounterByDomainAndIdentifierSpecification(BOOK_TIMES_FOR_EVENT_DOMAIN, name)));
    }

    @Override
    @Transactional
    public void countDiscountForUser(String email, byte discount) {
        String identifier = email + ":" + discount;
        safeIncrementCount(DISCOUNT_PERCENTAGE_DOMAIN, identifier, discountsLock);
    }

    @Override
    @Transactional(readOnly = true)
    public long getDiscountForUserCount(String email, byte discount) {
        String identifier = email + ":" + discount;
        return safeGetCount(counterRepository
                .query(new CounterByDomainAndIdentifierSpecification(DISCOUNT_PERCENTAGE_DOMAIN, identifier)));
    }

    @Override
    @Transactional(readOnly = true)
    public long getOverallDiscountCount(byte discount) {
        String identifier = ":" + discount;
        return safeGetCount(counterRepository
                .query(new CounterByDomainAndIdentifierSpecification(DISCOUNT_PERCENTAGE_DOMAIN, identifier)));
    }

    private long safeGetCount(List<Counter> counters) {
        if (counters != null && !counters.isEmpty()) {
            return counters.get(0).getCount().longValue();
        }
        return 0;
    }

    private void safeIncrementCount(String domain, String identifier, Lock lock) {
        lock.lock();
        try {
            List<Counter> counters = counterRepository
                    .query(new CounterByDomainAndIdentifierSpecification(domain, identifier));
            boolean isNew = counters.isEmpty();
            Counter required = !isNew
                    ? counters.get(0)
                    : Counter.builder()
                            .domain(domain)
                            .identifier(identifier)
                            .count(new AtomicLong(0))
                            .build();

            required.getCount().incrementAndGet();

            if (isNew) {
                counterRepository.add(required);
            } else {
                counterRepository.update(required);
            }
        } finally {
            lock.unlock();
        }
    }
}