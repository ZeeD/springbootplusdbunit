package vito.prove.springbootplusdbunit;

import java.util.Date;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.DefaultPrepAndExpectedTestCase;
import org.dbunit.PrepAndExpectedTestCase;
import org.dbunit.VerifyTableDefinition;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.CsvDataFileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import lombok.val;

@SpringBootTest
@AutoConfigureTestDatabase
public class ControllerDbTest {
    @Autowired
    MyRestController controller;
    @Autowired
    PrepAndExpectedTestCase dbUnit;

    @Test
    void myTest() throws Throwable {
        this.dbUnit.runTest(new VerifyTableDefinition[] { new VerifyTableDefinition("MY_TABLE",
                                                                                    null) },
                            new String[] { "/vito/prove/springbootplusdbunit/ControllerDbTest/myTest/prep/MY_TABLE.csv" },
                            new String[] { "/vito/prove/springbootplusdbunit/ControllerDbTest/myTest/expected/MY_TABLE.csv" },
                            () -> {
                                try {
                                    this.controller.modify(new MyTable(null,
                                                                       "name1",
                                                                       new Date(100_000L)));
                                }
                                catch (final Throwable t) {
                                    throw new Exception(t);
                                }
                                return null;
                            });
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
            val dt =
                   new DataSourceDatabaseTester(new TransactionAwareDataSourceProxy(this.ds));
            dt.setTearDownOperation(DatabaseOperation.DELETE_ALL);
            return new DefaultPrepAndExpectedTestCase(new CsvDataFileLoader(),
                                                      dt);
        }
    }
}
