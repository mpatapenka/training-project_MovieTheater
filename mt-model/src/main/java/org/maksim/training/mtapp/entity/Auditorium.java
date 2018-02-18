package org.maksim.training.mtapp.entity;

import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@Value
public final class Auditorium {
    String name;
    int numberOfSeats;
    @Builder.Default @NonFinal
    Set<Integer> vipSeats = Sets.newLinkedHashSet();

    public int countVipSeats(Collection<Integer> seats) {
        return seats.stream().filter(vipSeats::contains).collect(Collectors.toList()).size();
    }

    public Set<Integer> getAllSeats() {
        return IntStream.range(1, numberOfSeats + 1).boxed().collect(Collectors.toSet());
    }
}