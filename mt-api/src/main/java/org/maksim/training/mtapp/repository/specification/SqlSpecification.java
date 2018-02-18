package org.maksim.training.mtapp.repository.specification;

public interface SqlSpecification extends Specification {
    String toSqlQuery();
}