package org.maksim.training.mtapp.repository.specification.user;

import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.repository.specification.CriteriaSpecification;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public final class AllUsersSpecification
        implements PredicateSpecification<User>, CriteriaSpecification<User> {
    @Override
    public boolean test(User user) {
        return true;
    }

    @Override
    public TypedQuery<User> toTypedQuery(EntityManager entityManager) {
        return entityManager.createQuery("SELECT u FROM User u", User.class);
    }
}