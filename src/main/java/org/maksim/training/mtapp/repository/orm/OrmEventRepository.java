package org.maksim.training.mtapp.repository.orm;

import org.maksim.training.mtapp.model.Event;
import org.maksim.training.mtapp.repository.EventRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrmEventRepository extends OrmRepository<Event> implements EventRepository {
}