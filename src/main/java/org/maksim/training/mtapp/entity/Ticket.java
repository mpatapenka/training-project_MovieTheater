package org.maksim.training.mtapp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "_ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "_user_id")
    private User user;

    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "_event_id", nullable = false)
    private Event event;

    @Column(name = "_seance_datetime", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    @Column(name = "_selling_price", nullable = false)
    private BigDecimal sellingPrice;

    @Column(name = "_seat", nullable = false)
    private int seat;
}