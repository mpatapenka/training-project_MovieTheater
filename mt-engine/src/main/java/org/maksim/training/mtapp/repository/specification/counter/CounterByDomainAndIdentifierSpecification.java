package org.maksim.training.mtapp.repository.specification.counter;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.maksim.training.mtapp.entity.Counter;
import org.maksim.training.mtapp.repository.specification.CriteriaSpecification;
import org.maksim.training.mtapp.repository.specification.PredicateSpecification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Objects;

@RequiredArgsConstructor
public final class CounterByDomainAndIdentifierSpecification
        implements PredicateSpecification<Counter>, CriteriaSpecification<Counter> {
    private final String domain;
    private final String identifier;

    @Override
    public boolean test(Counter counter) {
        return Objects.equals(domain, counter.getDomain())
                && (Objects.equals(identifier, counter.getIdentifier())
                        || StringUtils.contains(counter.getIdentifier(), identifier));
    }

    @Override
    public TypedQuery<Counter> toTypedQuery(EntityManager entityManager) {
        TypedQuery<Counter> query = entityManager
                .createQuery("SELECT c FROM Counter c WHERE c.domain = :domain AND c.identifier LIKE :identifier", Counter.class);
        query.setParameter("domain", domain);
        query.setParameter("identifier", "%" + identifier + "%");
        return query;
    }
}