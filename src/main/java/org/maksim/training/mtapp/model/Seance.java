package org.maksim.training.mtapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.migesok.jaxb.adapter.javatime.LocalDateTimeXmlAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.maksim.training.mtapp.model.converter.AuditoriumToNameConverter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@XmlRootElement
@Entity
@Table(name = "_seance")
public class Seance implements Comparable<Seance> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlJavaTypeAdapter(LocalDateTimeXmlAdapter.class)
    @Column(name = "_datetime", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    @Column(name = "_auditorium_name")
    @Convert(converter = AuditoriumToNameConverter.class)
    private Auditorium auditorium;

    @Override
    public int compareTo(Seance other) {
        return this.dateTime.compareTo(other.dateTime);
    }
}