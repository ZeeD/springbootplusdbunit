package vito.prove.springbootplusdbunit;

import static org.dbunit.database.DatabaseConfig.PROPERTY_DATATYPE_FACTORY;

import java.util.Date;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.DefaultPrepAndExpectedTestCase;
import org.dbunit.PrepAndExpectedTestCase;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.ext.h2.H2DataTypeFactory;
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
                                                              new Date(100_000L))),
                     "MY_TABLE");
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
        PrepAndExpectedTestCase prepAndExpectedTestCase() throws Exception {
            val dt =
                   new DataSourceDatabaseTester(new TransactionAwareDataSourceProxy(this.ds));
            dt.getConnection()
              .getConfig()
              .setProperty(PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
            dt.setTearDownOperation(DatabaseOperation.DELETE_ALL);
            return new DefaultPrepAndExpectedTestCase(new CsvDataFileLoader(),
                                                      dt);
        }

        @Bean
        IDataTypeFactory dataTypeFactory() {
            return new H2DataTypeFactory();
        }
    }
}
