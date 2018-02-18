package org.maksim.training.mtapp.service;

public interface CounterService {
    void countEventByName(String name);
    long getEventByNameCount(String name);

    void countBookTimesForEvent(String name);
    long getBookTimesForEventCount(String name);

    void countDiscountForUser(String email, byte discount);
    long getDiscountForUserCount(String email, byte discount);
    long getOverallDiscountCount(byte discount);
}