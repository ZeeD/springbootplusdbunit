package vito.prove.springbootplusdbunit.testsupport;

import static org.dbunit.database.DatabaseConfig.FEATURE_BATCHED_STATEMENTS;
import static org.dbunit.database.DatabaseConfig.PROPERTY_DATATYPE_FACTORY;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.DefaultPrepAndExpectedTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.PrepAndExpectedTestCase;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.CsvDataFileLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import lombok.RequiredArgsConstructor;

class H2PrepAndExpectedTestCase extends DefaultPrepAndExpectedTestCase {
    public H2PrepAndExpectedTestCase(final DataSource ds) {
        super(new CsvDataFileLoader(), dt(ds));
    }

    static IDatabaseTester dt(final DataSource ds) {
        final var dsp = new TransactionAwareDataSourceProxy(ds);
        final var dt = new DataSourceDatabaseTester(dsp);
        dt.setTearDownOperation(DatabaseOperation.DELETE_ALL);
        return dt;
    }

    @Override
    protected void setUpDatabaseConfig(final DatabaseConfig config) {
        config.setProperty(FEATURE_BATCHED_STATEMENTS, Boolean.TRUE);
        config.setProperty(PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
    }
}

@Configuration
@RequiredArgsConstructor
class DbUnitConfiguration {
    final DataSource ds;

    @Bean
    PrepAndExpectedTestCase prepAndExpectedTestCase() {
        return new H2PrepAndExpectedTestCase(this.ds);
    }
}
