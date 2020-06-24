package vito.prove.springbootplusdbunit.testsupport;

import java.io.File;
import java.util.Iterator;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.operation.DatabaseOperation;

import lombok.RequiredArgsConstructor;
import lombok.val;

public interface DbUnitHelper {
    DataSource getDataSource();

    String PREP = "prep";
    String EXPECTED = "expected";

    default void prepare() throws Throwable {
        val ds = ds(this.getClass(), testName(), PREP);

        val c = this.getConnection();
        try {
            DatabaseOperation.CLEAN_INSERT.execute(c, ds);
        }
        finally {
            c.close();
        }
    }

    default ACIter<Table> validate() throws Throwable {
        val ds = ds(this.getClass(), testName(), EXPECTED);

        val c = this.getConnection();

        return new ACIter<>(c, Stream.of(ds.getTableNames()).map(tn -> {
            return new Table(getTable(ds, tn), createTable(c, tn));
        })::iterator);
    }

    default void assertTableEquals(final Table table) throws Throwable {
        Assertion.assertEquals(table.expected, table.actual);
    }

    default void clean() throws Throwable {
        val tn = testName();
        val ds = new CompositeDataSet(ds(this.getClass(), tn, PREP),
                                      ds(this.getClass(), tn, EXPECTED));

        val c = this.getConnection();
        try {
            DatabaseOperation.TRUNCATE_TABLE.execute(c, ds);
        }
        finally {
            c.close();
        }
    }

    default void dbtest(final A a) throws Throwable {
        try {
            this.prepare();
            a.action();
            try (val tables = this.validate()) {
                for (val table : tables)
                    this.assertTableEquals(table);
            }
        }
        finally {
            this.clean();
        }
    }

    default IDatabaseConnection getConnection() throws Exception {
        val ret =
                new DataSourceDatabaseTester(this.getDataSource()).getConnection();
        ret.getConfig()
           .setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
        return ret;
    }

    static String testName() {
        for (val ste : Thread.currentThread().getStackTrace())
            if (!ste.getClassName().equals(Thread.class.getName()) &&
                !ste.getClassName().equals(DbUnitHelper.class.getName()))
                return ste.getMethodName();
        throw new StackOverflowError();
        // return Thread.currentThread().getStackTrace()[4].getMethodName();
    }

    private static <T extends DbUnitHelper>
            IDataSet
            ds(final Class<T> cls,
               final String testName,
               final String stem) throws DataSetException {
        val uri = String.format("%s/%s/%s",
                                cls.getCanonicalName().replace('.', '/'),
                                testName,
                                stem);
        val f = new File(cls.getClassLoader().getResource(uri).getPath());
        return new CsvDataSet(f);
    }

    private static ITable getTable(final IDataSet ds, final String tableName) {
        return wrap(() -> ds.getTable(tableName));
    }

    private static ITable createTable(final IDatabaseConnection dc,
                                      final String tableName) {
        return wrap(() -> dc.createTable(tableName));
    }

    @FunctionalInterface
    static interface S<T> {
        T get() throws Throwable;
    }

    @FunctionalInterface
    static interface A {
        void action() throws Throwable;
    }

    private static <T> T wrap(final S<T> s) throws RuntimeException {
        try {
            return s.get();
        }
        catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @RequiredArgsConstructor
    static class Table {
        public final ITable expected;
        public final ITable actual;
    }

    @RequiredArgsConstructor
    static class ACIter<T> implements AutoCloseable, Iterable<T> {
        final IDatabaseConnection conn;
        final Iterable<T> iter;

        @Override
        public Iterator<T> iterator() {
            return this.iter.iterator();
        }

        @Override
        public void close() throws Exception {
            this.conn.close();
        }
    }
}
