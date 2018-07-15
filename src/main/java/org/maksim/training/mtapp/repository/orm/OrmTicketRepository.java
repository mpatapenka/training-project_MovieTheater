package org.maksim.training.mtapp.repository.orm;

import org.maksim.training.mtapp.model.Ticket;
import org.maksim.training.mtapp.repository.TicketRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrmTicketRepository extends OrmRepository<Ticket> implements TicketRepository {
}