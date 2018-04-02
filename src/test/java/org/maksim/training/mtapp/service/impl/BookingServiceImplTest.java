package org.maksim.training.mtapp.service.impl;

import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maksim.training.mtapp.entity.Event;
import org.maksim.training.mtapp.entity.Ticket;
import org.maksim.training.mtapp.repository.TicketRepository;
import org.maksim.training.mtapp.service.PaymentService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.Mockito.anyCollectionOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceImplTest {
    @Mock
    private TicketRepository mockTicketRepository;
    @Mock
    private PaymentService mockPaymentService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    public void checkThatBookTicketsIsStoreNeededInformationForUnregisteredUser() {
        LocalDateTime now = LocalDateTime.now();
        Event event = Event.builder().build();
        Set<Ticket> tickets = Sets.newHashSet(
                Ticket.builder().event(event).dateTime(now).seat(13).build(),
                Ticket.builder().event(event).dateTime(now).seat(14).build(),
                Ticket.builder().event(event).dateTime(now).seat(15).build()
        );

        bookingService.book(tickets, null);

        verify(mockTicketRepository, times(1)).add(anyCollectionOf(Ticket.class));
    }
}