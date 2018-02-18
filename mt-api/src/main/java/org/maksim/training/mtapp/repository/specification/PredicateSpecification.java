package org.maksim.training.mtapp.repository.specification;

import java.util.function.Predicate;

public interface PredicateSpecification<T> extends Predicate<T>, Specification {
}