package vito.prove.springbootplusdbunit.testsupport;

import static java.lang.Thread.currentThread;
import static java.util.stream.Stream.of;
import static org.dbunit.database.DatabaseConfig.FEATURE_BATCHED_STATEMENTS;
import static org.dbunit.database.DatabaseConfig.PROPERTY_DATATYPE_FACTORY;

import java.util.stream.Stream;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.DefaultPrepAndExpectedTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.PrepAndExpectedTestCase;
import org.dbunit.PrepAndExpectedTestCaseSteps;
import org.dbunit.VerifyTableDefinition;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.CsvDataFileLoader;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.val;

public abstract class DbUnitHelper {
    @Autowired
    PrepAndExpectedTestCase dbUnit;

    static {
        ((Logger) LoggerFactory.getLogger("org.dbunit")).setLevel(Level.ALL);
    }

    @SuppressWarnings("unchecked")
    public <T> T runTest(final S<T> supplier,
                         final String... tables) throws Throwable {
        return (T) this.dbUnit.runTest(of(tables).map(this::vtd)
                                                 .toArray(VerifyTableDefinition[]::new),
                                       of(tables).map(this::prepTable)
                                                 .toArray(String[]::new),
                                       of(tables).map(this::expectedTable)
                                                 .toArray(String[]::new),
                                       this.wraps(supplier));
    }

    public void runTest(final A action,
                        final String... tables) throws Throwable {
        this.dbUnit.runTest(of(tables).map(this::vtd)
                                      .toArray(VerifyTableDefinition[]::new),
                            of(tables).map(this::prepTable)
                                      .toArray(String[]::new),
                            of(tables).map(this::expectedTable)
                                      .toArray(String[]::new),
                            this.wraps(action));
    }

    @FunctionalInterface
    public static interface S<T> {
        T get() throws Throwable;
    }

    @FunctionalInterface
    public static interface A {
        void doit() throws Throwable;
    }

    PrepAndExpectedTestCaseSteps wraps(final S<?> s) throws RuntimeException {
        return () -> {
            try {
                return s.get();
            }
            catch (final Exception e) {
                throw e;
            }
            catch (final Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    PrepAndExpectedTestCaseSteps wraps(final A a) throws RuntimeException {
        return () -> {
            try {
                a.doit();
                return null;
            }
            catch (final Exception e) {
                throw e;
            }
            catch (final Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    VerifyTableDefinition vtd(final String table) {
        return new VerifyTableDefinition(table, null);
    }

    String prepTable(final String table) {
        return this.testName("prep", table);
    }

    String expectedTable(final String table) {
        return this.testName("expected", table);
    }

    String testName(final String stem, final String table) {
        for (val ste : currentThread().getStackTrace()) {
            val cls = ste.getClassName();
            if (!cls.equals(Thread.class.getName()) &&
                !cls.equals(DbUnitHelper.class.getName()) &&
                !cls.contains("$") &&
                !cls.startsWith(Stream.class.getPackageName()))
                return String.format("/%s/%s/%s/%s.csv",
                                     cls.replace('.', '/'),
                                     ste.getMethodName(),
                                     stem,
                                     table);
        }
        throw new StackOverflowError();
    }

    public static class H2PrepAndExpectedTestCase extends
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
            config.setProperty(FEATURE_BATCHED_STATEMENTS, true);
            config.setProperty(PROPERTY_DATATYPE_FACTORY,
                               new H2DataTypeFactory());
        }
    }
}
