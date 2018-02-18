package org.maksim.training.mtapp.repository.specification.user;

import lombok.RequiredArgsConstructor;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import java.util.Objects;

@RequiredArgsConstructor
public final class UserByEmailSpecification implements PredicateSpecification<User> {
    private final String email;

    @Override
    public boolean test(User user) {
        return Objects.equals(email, user.getEmail());
    }
}