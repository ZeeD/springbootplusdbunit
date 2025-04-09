package vito.prove.springbootplusdbunit;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.val;
import vito.prove.springbootplusdbunit.entity.MyOtherTable;
import vito.prove.springbootplusdbunit.entity.MyTable;
import vito.prove.springbootplusdbunit.repository.MyOtherTableRepository;
import vito.prove.springbootplusdbunit.repository.MyTableRepository;

@RequiredArgsConstructor
@RestController
class MyRestController {
    final MyTableRepository myTableRepository;
    final MyOtherTableRepository myOtherTableRepository;

    @GetMapping(value = "init")
    public void init() {
        final var rows = List.of(MyTable.of(null, "name1", new Date(0L)),
                                 MyTable.of(null, "name2", new Date(1_000L)),
                                 MyTable.of(null, "name3", new Date(100_000L)));
        this.myTableRepository.saveAll(rows);
    }

    @GetMapping(value = "find-all")
    public Iterable<MyTable> findAll() {
        return this.myTableRepository.findAll();
    }

    @PostMapping(value = "update-or-copy")
    public void
           updateOrCopy(@RequestBody final MyTable myTable) throws Throwable {
        if (myTable.getId() != null) // update
            this.myTableRepository.save(myTable);
        else { // save and copy
            val myTableId = this.myTableRepository.save(myTable).getId();

            val myOtherTable = MyOtherTable.of(myTableId,
                                               myTable.getName(),
                                               myTable.getWhen());
            this.myOtherTableRepository.save(myOtherTable);
        }
    }
}
