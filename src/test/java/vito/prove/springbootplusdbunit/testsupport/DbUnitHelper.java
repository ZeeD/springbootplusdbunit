package vito.prove.springbootplusdbunit.testsupport;

import java.util.stream.Stream;

import org.dbunit.PrepAndExpectedTestCase;
import org.dbunit.PrepAndExpectedTestCaseSteps;
import org.dbunit.VerifyTableDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.val;

public abstract class DbUnitHelper {
    @Autowired
    PrepAndExpectedTestCase dbUnit;

    public <T> T runTest(final S<T> supplier,
                         final String... tables) throws Throwable {
        @SuppressWarnings("unchecked")
        val ret =
                (T) this.dbUnit.runTest(Stream.of(tables)
                                              .map(this::vtd)
                                              .toArray(VerifyTableDefinition[]::new),
                                        Stream.of(tables)
                                              .map(this::prepTable)
                                              .toArray(String[]::new),
                                        Stream.of(tables)
                                              .map(this::expectedTable)
                                              .toArray(String[]::new),
                                        this.wraps(supplier));
        return ret;
    }

    public <T> T runTest(final A action,
                         final String... tables) throws Throwable {
        @SuppressWarnings("unchecked")
        val ret =
                (T) this.dbUnit.runTest(Stream.of(tables)
                                              .map(this::vtd)
                                              .toArray(VerifyTableDefinition[]::new),
                                        Stream.of(tables)
                                              .map(this::prepTable)
                                              .toArray(String[]::new),
                                        Stream.of(tables)
                                              .map(this::expectedTable)
                                              .toArray(String[]::new),
                                        this.wraps(action));
        return ret;
    }

    @FunctionalInterface
    public static interface S<T> {
        T get() throws Throwable;
    }

    @FunctionalInterface
    public static interface A {
        void doit() throws Throwable;
    }

    <T>
     PrepAndExpectedTestCaseSteps
     wraps(final S<T> s) throws RuntimeException {
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
        for (val ste : Thread.currentThread().getStackTrace()) {
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
}
