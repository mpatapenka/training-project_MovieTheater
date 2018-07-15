package org.maksim.training.mtapp.repository.orm;

import org.maksim.training.mtapp.model.User;
import org.maksim.training.mtapp.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrmUserRepository extends OrmRepository<User> implements UserRepository {
}