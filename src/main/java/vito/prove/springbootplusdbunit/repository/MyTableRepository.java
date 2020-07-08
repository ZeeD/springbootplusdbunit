package vito.prove.springbootplusdbunit.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import vito.prove.springbootplusdbunit.entity.MyTable;

public interface MyTableRepository extends CrudRepository<MyTable, Long> {
    Set<MyTable> findByName(final String label);
}
