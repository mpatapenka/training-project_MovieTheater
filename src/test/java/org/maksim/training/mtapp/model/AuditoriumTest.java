package org.maksim.training.mtapp.model;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AuditoriumTest {
    @Test
    public void checkThatAuditoriumHasDefaultInstantiatedFieldsInCaseEmptyBuilding() {
        Auditorium auditorium = Auditorium.builder().build();

        assertNull(auditorium.getName());
        assertNotNull(auditorium.getVipSeats());
        assertTrue(auditorium.getVipSeats().isEmpty());
    }
}