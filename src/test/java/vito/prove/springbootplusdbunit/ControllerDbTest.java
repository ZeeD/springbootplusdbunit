package vito.prove.springbootplusdbunit;

import static java.time.Instant.parse;
import static java.util.Date.from;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.sql.DataSource;

import org.dbunit.PrepAndExpectedTestCase;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import lombok.val;
import vito.prove.springbootplusdbunit.entity.MyTable;
import vito.prove.springbootplusdbunit.repository.MyTableRepository;
import vito.prove.springbootplusdbunit.testsupport.DbUnitHelper;

@SpringBootTest
@AutoConfigureTestDatabase
@TestMethodOrder(OrderAnnotation.class)
public class ControllerDbTest extends DbUnitHelper {
    @Autowired
    MyRestController controller;
    @Autowired
    MyTableRepository repository;

    @Test
    @Order(1)
    void alwaysStartWithEmptyDb1() throws Throwable {
        assertFalse(this.controller.findAll().iterator().hasNext());
    }

    @Test
    @Order(2)
    void updateOrCopyUpdate() throws Throwable {
        val myTable = new MyTable(123L,
                                  "newName",
                                  from(parse("2020-11-05T05:58:13Z")));

        this.runTest(() -> {
            this.controller.updateOrCopy(myTable);
            return null;
        });
    }

    @Test
    @Order(3)
    void alwaysStartWithEmptyDb2() throws Throwable {
        assertFalse(this.controller.findAll().iterator().hasNext());
    }

    @Test
    @Order(4)
    void updateOrCopyCopy() throws Throwable {
        val myTable = new MyTable(null,
                                  "name",
                                  from(parse("1982-11-05T05:58:13Z")));

        this.runTest(() -> {
            this.controller.updateOrCopy(myTable);
            return null;
        });
    }

    @Test
    @Order(5)
    void alwaysStartWithEmptyDb3() throws Throwable {
        assertFalse(this.controller.findAll().iterator().hasNext());
    }

    @TestConfiguration
    static class DbUnitConfiguration {
        @Autowired
        DataSource ds;

        @Bean
        PrepAndExpectedTestCase prepAndExpectedTestCase() {
            return new H2PrepAndExpectedTestCase(this.ds);
        }
    }
}
