package org.maksim.training.mtapp.service.impl;

import com.google.common.collect.Maps;
import org.maksim.training.mtapp.service.CounterService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class CounterServiceImpl implements CounterService {
    private final ReadWriteLock eventNameLock = new ReentrantReadWriteLock();
    private final ReadWriteLock bookTimesLock = new ReentrantReadWriteLock();
    private final ReadWriteLock discountsLock = new ReentrantReadWriteLock();

    private final Map<String, AtomicLong> eventByNameAccesses = Maps.newConcurrentMap();
    private final Map<String, AtomicLong> bookTimesForEvent = Maps.newConcurrentMap();
    private final Map<CompositeKey, AtomicLong> usersDiscounts = Maps.newConcurrentMap();
    private final Map<Byte, AtomicLong> overallDiscounts = Maps.newConcurrentMap();

    @Override
    public void countEventByName(String name) {
        safeIncrementCount(eventByNameAccesses, name, eventNameLock);
    }

    @Override
    public long getEventByNameCount(String name) {
        return safeGetCount(eventByNameAccesses.get(name));
    }

    @Override
    public void countBookTimesForEvent(String name) {
        safeIncrementCount(bookTimesForEvent, name, bookTimesLock);
    }

    @Override
    public long getBookTimesForEventCount(String name) {
        return safeGetCount(bookTimesForEvent.get(name));
    }

    @Override
    public void countDiscountForUser(String email, byte discount) {
        safeIncrementCount(usersDiscounts, new CompositeKey(email, discount), discountsLock);
        safeIncrementCount(overallDiscounts, discount, discountsLock);
    }

    @Override
    public long getDiscountForUserCount(String email, byte discount) {
        return safeGetCount(usersDiscounts.get(new CompositeKey(email, discount)));
    }

    @Override
    public long getOverallDiscountCount(byte discount) {
        return safeGetCount(overallDiscounts.get(discount));
    }

    private final class CompositeKey {
        private final String stringKey;
        private final byte byteKey;

        private final int hash;

        private CompositeKey(String stringKey, byte byteKey) {
            this.stringKey = stringKey;
            this.byteKey = byteKey;
            this.hash = Objects.hash(this.stringKey, this.byteKey);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CompositeKey that = (CompositeKey) o;
            return byteKey == that.byteKey
                    && Objects.equals(stringKey, that.stringKey);
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    private long safeGetCount(AtomicLong count) {
        if (count != null) {
            return count.get();
        }
        return 0;
    }

    private <K> void safeIncrementCount(Map<K, AtomicLong> counters, K key, ReadWriteLock lock) {
        AtomicLong count = counters.get(key);
        if (count == null) {
            lock.writeLock().lock();
            if ((count = counters.get(key)) == null) {
                count = new AtomicLong(0);
                counters.put(key, count);
            }
            lock.writeLock().unlock();
        }
        count.incrementAndGet();
    }
}