package org.maksim.training.mtapp.repository.orm;

import lombok.extern.slf4j.Slf4j;
import org.maksim.training.mtapp.repository.Repository;
import org.maksim.training.mtapp.repository.specification.CriteriaSpecification;
import org.maksim.training.mtapp.repository.specification.Specification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
abstract class OrmRepository<T> implements Repository<T> {
    private static final int DEFAULT_BATCH_SIZE = 10;

    @PersistenceContext
    private EntityManager em;

    @Override
    public void add(T item) {
        em.persist(item);
    }

    @Override
    public void add(Iterable<T> items) {
        int i = 1;
        for (T item : items) {
            em.persist(item);
            if (i++ % DEFAULT_BATCH_SIZE == 0) {
                log.debug("Flushing on iteration {}", i - 1);
                em.flush();
                em.clear();
            }
        }
    }

    @Override
    public void update(T item) {
        em.merge(item);
    }

    @Override
    public void remove(T item) {
        em.remove(item);
    }

    @Override
    public void remove(Specification specification) {
        @SuppressWarnings("unchecked") CriteriaSpecification<T> criteria = (CriteriaSpecification<T>) specification;
        criteria.toTypedQuery(em).executeUpdate();
    }

    @Override
    public List<T> query(Specification specification) {
        @SuppressWarnings("unchecked") CriteriaSpecification<T> criteria = (CriteriaSpecification<T>) specification;
        return criteria.toTypedQuery(em).getResultList();
    }
}