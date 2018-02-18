package org.maksim.training.mtapp.repository.inmemory;

import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ImUserRepository extends ImRepository<User> implements UserRepository {
    @Override
    Long getId(User entity) {
        return entity.getId();
    }

    @Override
    void generateId(User entity) {
        entity.setId(getIdGenerator().getAndIncrement());
    }
}