package org.maksim.training.mtapp.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.maksim.training.mtapp.entity.serializer.json.AuditoriumDeserializer;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonDeserialize(using = AuditoriumDeserializer.class)
public class Auditorium {
    private String name;
    private int numberOfSeats;
    @Builder.Default private Set<Integer> vipSeats = Sets.newLinkedHashSet();

    public int countVipSeats(Collection<Integer> seats) {
        return seats.stream().filter(vipSeats::contains).collect(Collectors.toList()).size();
    }

    public Collection<Integer> getAllSeats() {
        return IntStream.range(1, numberOfSeats + 1).boxed().collect(Collectors.toList());
    }
}