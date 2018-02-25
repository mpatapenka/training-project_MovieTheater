package org.maksim.training.mtapp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.repository.UserRepository;
import org.maksim.training.mtapp.repository.specification.user.AllUsersSpecification;
import org.maksim.training.mtapp.repository.specification.user.UserByEmailSpecification;
import org.maksim.training.mtapp.repository.specification.user.UserByIdSpecification;
import org.maksim.training.mtapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class UserServiceImpl extends CrudGenericService<User, Long> implements UserService {
    private final Lock lock = new ReentrantLock();

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    @Transactional
    public User save(User user) {
        if (user.getEmail() == null) {
            log.error("User is not eligible to be saved: {}.", user);
            throw new IllegalArgumentException("User's email is required.");
        }

        if (getId(user) == null) {
            boolean requiredToAdd;
            try {
                lock.lock();
                if (requiredToAdd = (getByEmail(user.getEmail()) == null)) {
                    getRepository().add(user);
                }
            } finally {
                lock.unlock();
            }
            if (!requiredToAdd) {
                user = null;
            }
        } else {
            getRepository().update(user);
        }
        return user;
    }

    @Override
    Long getId(User user) {
        return user.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return findOne(getRepository().query(new UserByIdSpecification(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return findOne(getRepository().query(new UserByEmailSpecification(email)));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<User> getAll() {
        return getRepository().query(new AllUsersSpecification());
    }
}