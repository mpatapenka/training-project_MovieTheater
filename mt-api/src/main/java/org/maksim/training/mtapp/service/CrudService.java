package org.maksim.training.mtapp.service;

import java.util.Collection;

public interface CrudService<E, ID> {
    E save(E item);
    E remove(E item);
    E getById(ID id);
    Collection<E> getAll();
}