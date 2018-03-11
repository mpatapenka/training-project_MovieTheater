package org.maksim.training.mtapp.repository.specification.user;

import lombok.RequiredArgsConstructor;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.repository.specification.CriteriaSpecification;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Objects;

@RequiredArgsConstructor
public final class UserByIdSpecification
        implements PredicateSpecification<User>, CriteriaSpecification<User> {
    private final Long id;

    @Override
    public boolean test(User user) {
        return Objects.equals(id, user.getId());
    }

    @Override
    public TypedQuery<User> toTypedQuery(EntityManager entityManager) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
        query.setParameter("id", id);
        return query;
    }
}