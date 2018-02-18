package org.maksim.training.mtapp.service.impl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AuditoriumServiceImplTest {
    private AuditoriumServiceImpl auditoriumService;

    @Test
    public void checkThatAllAuditoriumsDataHasBeenLoaded() {
        auditoriumService = new AuditoriumServiceImpl("auditoriums");
        auditoriumService.init();

        assertEquals(3, auditoriumService.getAll().size());
        assertNotNull(auditoriumService.getByName("Main Hall"));
        assertNotNull(auditoriumService.getByName("Small Hall"));
        assertNotNull(auditoriumService.getByName("Medium Hall"));
    }

    @Test(expected = RuntimeException.class)
    public void checkThatIfAuditoriumsDirectoryNotFoundThenProduceException() {
        auditoriumService = new AuditoriumServiceImpl("dir");
        auditoriumService.init();
    }
}