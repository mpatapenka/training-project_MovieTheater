package org.maksim.training.mtapp.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = {"tickets", "messages"})
@ToString(exclude = "tickets")
public final class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthday;
    private UserRole role;
    @Builder.Default
    private Set<Ticket> tickets = Sets.newHashSet();
    @Builder.Default
    private Collection<Message> messages = Lists.newArrayList();
}