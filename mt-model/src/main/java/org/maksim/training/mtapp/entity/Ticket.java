package org.maksim.training.mtapp.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public final class Ticket {
    private Long id;
    private User user;
    private Event event;
    private BigDecimal sellingPrice;
    private LocalDateTime dateTime;
    private int seat;
}