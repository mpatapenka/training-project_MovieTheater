package org.maksim.training.mtapp.repository.inmemory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.maksim.training.mtapp.config.ImUserRepositoryTestConfig;
import org.maksim.training.mtapp.model.User;
import org.maksim.training.mtapp.model.UserRole;
import org.maksim.training.mtapp.repository.UserRepository;
import org.maksim.training.mtapp.repository.specification.user.AllUsersSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ImUserRepositoryTestConfig.class)
public class ImUserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user = User.builder().firstName("Maksim").lastName("Patapenka").email("maksim.patapenka@gmail.com")
            .birthday(LocalDate.of(1994, Month.MAY, 13)).role(UserRole.REGISTERED_USER).build();

    @Test
    public void checkThatUserIsSavedInMemoryStorage() {
        userRepository.add(user);

        List<User> allUsers = userRepository.query(new AllUsersSpecification());
        assertThat(allUsers, hasSize(1));
        assertThat(allUsers.get(0).getId(), equalTo(1L));
        assertThat(allUsers.get(0), equalTo(user));
    }

    @Test
    public void checkThatUserIsUpdatedInMemoryStorage() {
        userRepository.add(user);

        assertThat(userRepository.query(new AllUsersSpecification()), hasSize(1));

        User userFromRepository = userRepository.query(new AllUsersSpecification()).get(0);

        assertThat(userFromRepository, equalTo(user));

        User userUpdate = User.builder().id(userFromRepository.getId()).firstName("Test").lastName("Test")
                .email("test@test.test").role(UserRole.BOOKING_MANAGER).build();
        userRepository.update(userUpdate);

        assertThat(userRepository.query(new AllUsersSpecification()), hasSize(1));
        userFromRepository = userRepository.query(new AllUsersSpecification()).get(0);
        assertThat(userFromRepository, not(equalTo(user)));
    }
}