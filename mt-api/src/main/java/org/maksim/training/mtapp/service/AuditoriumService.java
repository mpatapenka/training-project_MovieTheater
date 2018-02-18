package org.maksim.training.mtapp.service;

import org.maksim.training.mtapp.entity.Auditorium;

import java.util.Collection;

public interface AuditoriumService {
    Auditorium getByName(String name);
    Collection<Auditorium> getAll();
}