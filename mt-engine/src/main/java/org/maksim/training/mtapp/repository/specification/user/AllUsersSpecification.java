package org.maksim.training.mtapp.repository.specification.user;

import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

public final class AllUsersSpecification implements PredicateSpecification<User> {
    @Override
    public boolean test(User user) {
        return true;
    }
}