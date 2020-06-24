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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import lombok.val;

@SpringBootTest
@AutoConfigureTestDatabase
public class ControllerDbTest {
    @Autowired
    MyRestController controller;
    @Autowired
    DataSource ds;
    PrepAndExpectedTestCase testCase;

    @BeforeEach
    void setup() throws Throwable {
        val databaseTester =
                           new DataSourceDatabaseTester(new TransactionAwareDataSourceProxy(this.ds));
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
        this.testCase =
                      new DefaultPrepAndExpectedTestCase(new CsvDataFileLoader(),
                                                         databaseTester);
    }

    @Test
    void myTest() throws Throwable {
        this.testCase.runTest(new VerifyTableDefinition[] { new VerifyTableDefinition("MY_TABLE",
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
}
