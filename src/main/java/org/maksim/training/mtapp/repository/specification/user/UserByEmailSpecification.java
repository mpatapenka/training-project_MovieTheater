package org.maksim.training.mtapp.repository.specification.user;

import lombok.RequiredArgsConstructor;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.repository.specification.CriteriaSpecification;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Objects;

@RequiredArgsConstructor
public final class UserByEmailSpecification
        implements PredicateSpecification<User>, CriteriaSpecification<User> {
    private final String email;

    @Override
    public boolean test(User user) {
        return Objects.equals(email, user.getEmail());
    }

    @Override
    public TypedQuery<User> toTypedQuery(EntityManager entityManager) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query;
    }
}