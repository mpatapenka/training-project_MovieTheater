package org.maksim.training.mtapp.repository.inmemory;

import org.maksim.training.mtapp.entity.Counter;
import org.maksim.training.mtapp.repository.CounterRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ImCounterRepository extends ImRepository<Counter> implements CounterRepository {
    @Override
    Long getId(Counter item) {
        return item.getId();
    }

    @Override
    void generateId(Counter item) {
        item.setId(getIdGenerator().getAndIncrement());
    }
}