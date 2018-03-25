package org.maksim.training.mtapp.entity;

import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.SortedSet;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@EqualsAndHashCode(exclude = "seances")
@ToString
@Entity
@Table(name = "_event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "_name", length = 50, nullable = false)
    private String name;

    @Column(name = "_base_price", nullable = false)
    private BigDecimal basePrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "_rating", nullable = false)
    private EventRating rating;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy
    @Builder.Default private SortedSet<Seance> seances = Sets.newTreeSet(Comparator.comparing(Seance::getDateTime));

    public Auditorium getAuditorium(LocalDateTime dateTime) {
        Seance from = Seance.builder().dateTime(dateTime).build();
        Seance to = Seance.builder().dateTime(dateTime.plusSeconds(1)).build();
        SortedSet<Seance> foundedSeances = seances.subSet(from, to);
        return !foundedSeances.isEmpty() ? foundedSeances.first().getAuditorium() : null;
    }
}