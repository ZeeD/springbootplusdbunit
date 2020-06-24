package vito.prove.springbootplusdbunit;

import java.util.Date;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import vito.prove.springbootplusdbunit.testsupport.DbUnitHelper;

@SpringBootTest
@AutoConfigureTestDatabase
public class ControllerDbTest implements DbUnitHelper {
    @Autowired
    MyRestController controller;
    @Autowired
    DataSource ds;

    @Test
    void myTest() throws Throwable {
        this.dbtest(() -> {
            this.controller.modify(new MyTable(null,
                                               "name1",
                                               new Date(100_000L)));
        });
    }

    @Test
    void myOtherTest() throws Throwable {
        Assertions.assertFalse(this.controller.findAll().iterator().hasNext());
    }

    @Override
    public DataSource getDataSource() {
        return this.ds;
    }
}
