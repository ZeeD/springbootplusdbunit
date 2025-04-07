package vito.prove.springbootplusdbunit.repository;

import org.springframework.data.repository.CrudRepository;

import vito.prove.springbootplusdbunit.entity.IDontCareAboutThisTable;

public interface IDontCareAboutThisTableRepository
        extends CrudRepository<IDontCareAboutThisTable, Long> {
    /* pass */
}
