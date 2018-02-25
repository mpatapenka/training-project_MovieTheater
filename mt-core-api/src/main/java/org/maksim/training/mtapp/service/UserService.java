package org.maksim.training.mtapp.service;

import org.maksim.training.mtapp.entity.User;

public interface UserService extends CrudService<User, Long> {
    User getByEmail(String email);
}