package vito.prove.springbootplusdbunit;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
@RestController
class MyRestController {
    final MyTableRepository repository;

    @GetMapping(value = "init")
    public void init() {
        val rows = List.of(new MyTable(null, "name1", new Date(0L)),
                           new MyTable(null, "name2", new Date(1_000L)),
                           new MyTable(null, "name3", new Date(100_000L)));

        this.repository.saveAll(rows);
    }

    @GetMapping(value = "findAll")
    public Iterable<MyTable> findAll() {
        return this.repository.findAll();
    }

    @PostMapping(value = "modify")
    public void modify(@RequestBody
    final MyTable myTable) throws Throwable {
        if (myTable.id == null) {
            val foundTables = this.repository.findByName(myTable.name);
            if (foundTables.isEmpty())
                this.repository.save(myTable);
            else {
                for (val foundTable : foundTables)
                    foundTable.when = myTable.when;
                this.repository.saveAll(foundTables);
            }
        }
        else
            this.repository.save(myTable);
    }
}
