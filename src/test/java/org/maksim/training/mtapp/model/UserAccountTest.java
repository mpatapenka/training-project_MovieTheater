package org.maksim.training.mtapp.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserAccountTest {
    @Test
    public void checkThatUserAccountHasDefaultInstantiatedFieldsInCaseEmptyBuilding() {
        UserAccount userAccount = UserAccount.builder().build();

        assertNotNull(userAccount);
        assertNull(userAccount.getId());
        assertEquals(BigDecimal.ZERO, userAccount.getAmount());
    }
}