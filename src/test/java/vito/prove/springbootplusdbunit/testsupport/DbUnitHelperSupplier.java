package vito.prove.springbootplusdbunit.testsupport;

import org.dbunit.PrepAndExpectedTestCaseSteps;

public interface DbUnitHelperSupplier extends DbUnitHelperBase {
    /**
     * run the tests, do the db checks, return the supplier value
     */
    @SuppressWarnings("unchecked")
    default <T> T runTest(final S<T> supplier) throws Throwable {
        return (T) this.runTestSteps(this.wraps(supplier));
    }

    @FunctionalInterface
    public static interface S<T> {
        T get() throws Throwable;
    }

    default PrepAndExpectedTestCaseSteps
            wraps(final S<?> s) throws RuntimeException {
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
}
