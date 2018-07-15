package org.maksim.training.mtapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.migesok.jaxb.adapter.javatime.LocalDateXmlAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.Collection;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"tickets", "messages"})
@ToString(exclude = "tickets")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @XmlAttribute
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "_first_name", length = 50)
    private String firstName;

    @Column(name = "_last_name", length = 50)
    private String lastName;

    @Column(name = "_email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "_password", length = 75, nullable = false)
    private String password;

    @XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
    @Column(name = "_birthday")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @OneToOne(targetEntity = UserAccount.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserAccount userAccount;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = UserRole.class)
    @Column(name = "_roles", nullable = false)
    @Singular private Collection<UserRole> roles;

    @OneToMany(mappedBy = "user")
    @Builder.Default private Collection<Ticket> tickets = Lists.newArrayList();

    @ElementCollection
    @CollectionTable(name = "_messages", joinColumns = @JoinColumn(name = "_message_id"))
    @Column(name = "_message")
    @Builder.Default private Collection<String> messages = Lists.newArrayList();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}