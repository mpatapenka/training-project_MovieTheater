package org.maksim.training.mtapp.service;

import org.maksim.training.mtapp.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends CrudService<User, Long>, UserDetailsService {
    User getByEmail(String email);
}