package org.maksim.training.mtapp.entity;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserTest {
    @Test
    public void checkThatUserHasDefaultInstantiatedFieldsInCaseEmptyBuilding() {
        User user = User.builder().build();

        assertNull(user.getId());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getEmail());
        assertNull(user.getBirthday());
        assertNotNull(user.getRoles());
        assertNotNull(user.getTickets());
        assertTrue(user.getTickets().isEmpty());
    }
}