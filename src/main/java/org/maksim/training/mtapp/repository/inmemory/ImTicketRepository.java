package org.maksim.training.mtapp.repository.inmemory;

import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.repository.TicketRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ImTicketRepository extends ImRepository<Ticket> implements TicketRepository {
    @Override
    Long getId(Ticket ticket) {
        return ticket.getId();
    }

    @Override
    void generateId(Ticket ticket) {
        ticket.setId(getIdGenerator().getAndIncrement());
    }
}