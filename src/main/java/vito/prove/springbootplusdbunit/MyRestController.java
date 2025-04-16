package vito.prove.springbootplusdbunit;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vito.prove.springbootplusdbunit.entity.MyOtherTable;
import vito.prove.springbootplusdbunit.entity.MyTable;
import vito.prove.springbootplusdbunit.model.MyModel;
import vito.prove.springbootplusdbunit.repository.MyOtherTableRepository;
import vito.prove.springbootplusdbunit.repository.MyTableRepository;

@RestController
@RequiredArgsConstructor
class MyRestController {
    final MyTableRepository repository;
    final MyOtherTableRepository myOtherTableRepository;

    @GetMapping(value = "init")
    void init() {
        final var tables =
                         List.of(MyTable.of(null, "name1", new Date(0L)),
                                 MyTable.of(null, "name2", new Date(1_000L)),
                                 MyTable.of(null, "name3", new Date(100_000L)));
        this.repository.saveAll(tables);
    }

    @GetMapping(value = "find-all")
    List<MyModel> findAll() {
        final var tables = this.repository.findAll();

        return tables.stream()
                     .map(table -> new MyModel(table.getId(),
                                               table.getName(),
                                               table.getSomethingDate()))
                     .toList();
    }

    @PostMapping(value = "create-or-update")
    public Long createOrUpdate(@RequestBody final MyModel request) {
        final var id = request.getId();
        if (id == null)
            return this.createMyTable(request);

        this.updateMyTable(id, request);
        this.createMyOtherTable(id, request);
        return id;
    }

    private Long createMyTable(final MyModel model) {
        final var table = MyTable.of(model.getId(),
                                     model.getName(),
                                     model.getSomethingDate());

        return this.repository.save(table).getId();
    }

    private void updateMyTable(final Long id, final MyModel model) {
        final var table = this.repository.findById(id).orElseThrow();
        table.setName(model.getName());
        table.setSomethingDate(model.getSomethingDate());
        this.repository.save(table);
    }

    private void createMyOtherTable(final Long id, final MyModel model) {
        final var otherTable = MyOtherTable.of(id,
                                               model.getName(),
                                               model.getSomethingDate());
        this.myOtherTableRepository.save(otherTable);
    }
}
