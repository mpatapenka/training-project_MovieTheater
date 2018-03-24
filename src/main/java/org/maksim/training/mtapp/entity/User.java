package org.maksim.training.mtapp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Collection;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@EqualsAndHashCode(exclude = {"tickets", "messages"})
@ToString(exclude = "tickets")
@Entity
@Table(name = "_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "_first_name", length = 50)
    private String firstName;

    @Column(name = "_last_name", length = 50)
    private String lastName;

    @Column(name = "_email", length = 50, nullable = false)
    private String email;

    @Column(name = "_birthday")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "_role", nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "user")
    @Builder.Default private Collection<Ticket> tickets = Lists.newArrayList();

    @ElementCollection
    @CollectionTable(name = "_messages", joinColumns = @JoinColumn(name = "_message_id"))
    @Column(name = "_message")
    @Builder.Default private Collection<String> messages = Lists.newArrayList();
}