package vito.prove.springbootplusdbunit.testsupport;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Path.of;
import static org.dbunit.database.DatabaseConfig.FEATURE_BATCHED_STATEMENTS;
import static org.dbunit.database.DatabaseConfig.PROPERTY_DATATYPE_FACTORY;

import java.util.stream.Stream;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.DefaultPrepAndExpectedTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.PrepAndExpectedTestCase;
import org.dbunit.VerifyTableDefinition;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.CsvDataFileLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import lombok.val;

public interface DbUnitHelperBase {
    @Autowired
    PrepAndExpectedTestCase getDbUnit();

    /**
     * run the tests, do the db checks, return the supplier value
     */
    @SuppressWarnings("unchecked")
    default <T> T runTest(final Supplier<T> supplier) throws Throwable {
        /*
         * really ugly workaround: CsvURLDataSet WANT a single .csv file, but
         * loads ALL THE DIRECTORY CONTENT! the trick is to keep the expected
         * tables from table-ordering.txt (witch MUST be set), and pass to
         * runTest a single "random" csv for the preps and the expected
         */
        val cls = this.getClass();
        val fmt = "%s/table-ordering.txt";

        val e = this.testName("expected");
        val eTables = readAllLines(of(cls.getResource(format(fmt, e)).toURI()));
        val eTable = format("%s/%s.csv", e, eTables.get(0));

        val p = this.testName("prep");
        val pTables = readAllLines(of(cls.getResource(format(fmt, p)).toURI()));
        val pTable = format("%s/%s.csv", p, pTables.get(0));

        return (T) this.getDbUnit()
                       .runTest(eTables.stream()
                                       .map(t -> new VerifyTableDefinition(t,
                                                                           null))
                                       .toArray(VerifyTableDefinition[]::new),
                                new String[] {pTable},
                                new String[] {eTable},
                                () -> {
                                    try {
                                        return supplier.get();
                                    }
                                    catch (final Exception ex) {
                                        throw ex;
                                    }
                                    catch (final Throwable ex) {
                                        throw new RuntimeException(ex);
                                    }
                                });
    }

    default String testName(final String stem) {
        for (val ste : currentThread().getStackTrace()) {
            val cls = ste.getClassName();
            if (cls.equals(Thread.class.getName()) ||
                cls.equals(DbUnitHelperBase.class.getName()) ||
                cls.contains("$") ||
                cls.startsWith(Stream.class.getPackageName()))
                continue;
            return format("/%s/%s/%s",
                          cls.replace('.', '/'),
                          ste.getMethodName(),
                          stem);
        }
        throw new StackOverflowError();
    }

    static class H2PrepAndExpectedTestCase extends
                                           DefaultPrepAndExpectedTestCase {
        public H2PrepAndExpectedTestCase(final DataSource ds) {
            super(new CsvDataFileLoader(), dt(ds));
        }

        static IDatabaseTester dt(final DataSource ds) {
            val dt =
                   new DataSourceDatabaseTester(new TransactionAwareDataSourceProxy(ds));
            dt.setTearDownOperation(DatabaseOperation.DELETE_ALL);
            return dt;
        }

        @Override
        protected void setUpDatabaseConfig(final DatabaseConfig config) {
            config.setProperty(FEATURE_BATCHED_STATEMENTS, Boolean.TRUE);
            config.setProperty(PROPERTY_DATATYPE_FACTORY,
                               new H2DataTypeFactory());
        }
    }

    @FunctionalInterface
    static interface Supplier<T> {
        T get() throws Throwable;
    }
}
