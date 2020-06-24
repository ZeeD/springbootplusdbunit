package vito.prove.springbootplusdbunit;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

public interface MyTableRepository extends CrudRepository<MyTable, Long> {
    Set<MyTable> findByName(final String label);
}
