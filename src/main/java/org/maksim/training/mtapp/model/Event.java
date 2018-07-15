package org.maksim.training.mtapp.model;

import com.google.common.collect.Sets;
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.SortedSet;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "seances")
@ToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "_event")
public class Event {
    @XmlAttribute
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement
    @Column(name = "_name", length = 50, nullable = false)
    private String name;

    @XmlElement
    @Column(name = "_base_price", nullable = false)
    private BigDecimal basePrice;

    @XmlElement
    @Enumerated(EnumType.STRING)
    @Column(name = "_rating", nullable = false)
    private EventRating rating;

    @XmlElement
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