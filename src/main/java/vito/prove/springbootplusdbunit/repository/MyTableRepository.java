package vito.prove.springbootplusdbunit.repository;

import java.util.Set;

import org.springframework.data.repository.ListCrudRepository;

import vito.prove.springbootplusdbunit.entity.MyTable;

public interface MyTableRepository extends ListCrudRepository<MyTable, Long> {
    Set<MyTable> findByName(final String label);
}
