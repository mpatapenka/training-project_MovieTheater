package org.maksim.training.mtapp.repository.orm;

import org.maksim.training.mtapp.model.Counter;
import org.maksim.training.mtapp.repository.CounterRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrmCounterRepository extends OrmRepository<Counter> implements CounterRepository {
}