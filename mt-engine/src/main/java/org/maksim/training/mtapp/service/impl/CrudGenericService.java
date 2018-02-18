package org.maksim.training.mtapp.service.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.maksim.training.mtapp.repository.Repository;
import org.maksim.training.mtapp.service.CrudService;

import java.util.List;

@RequiredArgsConstructor
@Getter(AccessLevel.PACKAGE)
abstract class CrudGenericService<E, ID> implements CrudService<E, ID> {
    private final Repository<E> repository;

    abstract ID getId(E item);

    E findOne(List<E> items) {
        return !items.isEmpty() ? items.get(0) : null;
    }

    @Override
    public E save(E item) {
        if (getId(item) == null) {
            getRepository().add(item);
        } else {
            getRepository().update(item);
        }
        return item;
    }

    @Override
    public E remove(E item) {
        E retrieved = getById(getId(item));
        if (retrieved != null) {
            getRepository().remove(item);
        }
        return retrieved;
    }
}