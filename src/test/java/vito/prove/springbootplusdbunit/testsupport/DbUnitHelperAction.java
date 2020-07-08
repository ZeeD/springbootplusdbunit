package vito.prove.springbootplusdbunit.testsupport;

import org.dbunit.PrepAndExpectedTestCaseSteps;

public interface DbUnitHelperAction extends DbUnitHelperBase {
    /**
     * run the tests, do the db checks, do not use any return value
     */
    default void runTest(final Action action) throws Throwable {
        this.runTest(this.wraps(action));
    }

    @FunctionalInterface
    public static interface Action {
        void doit() throws Throwable;
    }

    default PrepAndExpectedTestCaseSteps
            wraps(final Action a) throws RuntimeException {
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
}
