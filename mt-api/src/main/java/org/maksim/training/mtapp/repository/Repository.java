package org.maksim.training.mtapp.repository;

import org.maksim.training.mtapp.repository.specification.Specification;

import java.util.List;

public interface Repository<T> {
    void add(T item);
    void add(Iterable<T> items);
    void update(T item);
    void remove(T item);
    void remove(Specification specification);
    List<T> query(Specification specification);
}