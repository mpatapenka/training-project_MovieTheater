package org.maksim.training.mtapp.model;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EventTest {
    @Test
    public void checkThatEventHasDefaultInstantiatedFieldsInCaseEmptyBuilding() {
        Event event = Event.builder().build();

        assertNull(event.getId());
        assertNull(event.getBasePrice());
        assertNull(event.getName());
        assertNull(event.getRating());
        assertNotNull(event.getSeances());
        assertTrue(event.getSeances().isEmpty());
    }
}