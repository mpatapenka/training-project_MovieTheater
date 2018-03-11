package org.maksim.training.mtapp.repository.specification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public interface CriteriaSpecification<T> extends Specification {
    TypedQuery<T> toTypedQuery(EntityManager entityManager);
}