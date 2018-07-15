package org.maksim.training.mtapp.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.maksim.training.mtapp.config.UserServiceImplTestConfig;
import org.maksim.training.mtapp.model.User;
import org.maksim.training.mtapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserServiceImplTestConfig.class)
public class UserServiceImplTest {
    @Autowired
    private UserService userService;

    private User user = User.builder().firstName("Maksim").lastName("Patapenka")
            .email("maksim.patapenka@gmail.com").birthday(LocalDate.of(1994, Month.MAY, 13))
            .build();

    @Test
    public void checkThatUserAddedSuccessful() {
        User savedUser = userService.save(user);
        assertNotNull(savedUser.getId());
    }

    @Test
    public void checkThatAllUsersAddedSuccessfully() {
        userService.save(user);
        userService.save(User.builder().firstName("Ivan").lastName("Ivanov").email("ivan.ivanov@gmail.com").build());
        userService.save(User.builder().firstName("Juri").lastName("Lobov").email("jury.lobov@gmail.com").build());
        assertEquals(3, userService.getAll().size());
    }

    @Test
    public void checkThatUserIsUpdated() {
        User savedUser = userService.save(user);
        userService.save(savedUser);
        assertEquals(1, userService.getAll().size());
    }

    @Test
    public void doubleRegisterNotAllowed() {
        assertNotNull(userService.save(user));

        user = User.builder().firstName("Maksim").lastName("Patapenka")
                .email("maksim.patapenka@gmail.com").birthday(LocalDate.of(1994, Month.MAY, 13))
                .build();

        assertNull(userService.save(user));

        assertEquals(1, userService.getAll().size());
    }
}