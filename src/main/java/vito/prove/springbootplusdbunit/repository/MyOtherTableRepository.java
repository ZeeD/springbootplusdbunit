package vito.prove.springbootplusdbunit.repository;

import org.springframework.data.repository.CrudRepository;

import vito.prove.springbootplusdbunit.entity.MyOtherTable;

public interface MyOtherTableRepository
        extends CrudRepository<MyOtherTable, Long> {
    /* pass */
}
