package org.maksim.training.mtapp.entity;

import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NavigableMap;

@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = "seances")
@ToString
public final class Event {
    private Long id;
    private String name;
    private BigDecimal basePrice;
    private EventRating rating;
    @Builder.Default
    private NavigableMap<LocalDateTime, Auditorium> seances = Maps.newTreeMap();
}