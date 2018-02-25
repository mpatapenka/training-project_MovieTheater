package org.maksim.training.mtapp.repository.inmemory;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.maksim.training.mtapp.repository.Repository;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;
import org.maksim.training.mtapp.repository.specification.Specification;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
abstract class ImRepository<T> implements Repository<T> {
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final Map<Long, T> storage = Maps.newConcurrentMap();

    AtomicLong getIdGenerator() {
        return idGenerator;
    }

    abstract Long getId(T item);

    abstract void generateId(T item);

    @Override
    public void add(T item) {
        generateId(item);
        storage.put(getId(item), item);
    }

    @Override
    public void add(Iterable<T> items) {
        items.forEach(this::add);
    }

    @Override
    public void update(T item) {
        Long id = getId(item);
        if (id != null) {
            storage.replace(id, item);
        } else {
            log.error("Item {} is not allowed to update.", item);
            throw new IllegalArgumentException("ID should be specified to update into in-memory repository.");
        }
    }

    @Override
    public void remove(T item) {
        storage.remove(getId(item));
    }

    @Override
    public void remove(Specification specification) {
        query(specification).forEach(i -> storage.remove(getId(i)));
    }

    @Override
    public List<T> query(Specification specification) {
        @SuppressWarnings("unchecked") PredicateSpecification<T> predicate = (PredicateSpecification<T>) specification;
        return storage.values().stream().filter(predicate).collect(Collectors.toList());
    }
}