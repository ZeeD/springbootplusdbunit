package vito.prove.springbootplusdbunit;

import java.util.Date;

import javax.sql.DataSource;

import org.dbunit.PrepAndExpectedTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import vito.prove.springbootplusdbunit.testsupport.DbUnitHelper;

@SpringBootTest
@AutoConfigureTestDatabase
public class ControllerDbTest extends DbUnitHelper {
    @Autowired
    MyRestController controller;

    @Test
    void myTest() throws Throwable {
        this.runTest(() -> this.controller.modify(new MyTable(null,
                                                              "name1",
                                                              new Date(100_000L))));
    }

    @Test
    void myOtherTest() throws Throwable {
        Assertions.assertFalse(this.controller.findAll().iterator().hasNext());
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
