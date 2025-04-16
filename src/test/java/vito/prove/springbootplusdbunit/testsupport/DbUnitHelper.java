package vito.prove.springbootplusdbunit.testsupport;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Path.of;

import java.util.stream.Stream;

import org.dbunit.PrepAndExpectedTestCase;
import org.dbunit.PrepAndExpectedTestCaseSteps;
import org.dbunit.VerifyTableDefinition;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DbUnitHelper {
    final PrepAndExpectedTestCase dbUnit;

    /**
     * run the tests, do the db checks, return the supplier value
     */
    public Object
           runTest(final PrepAndExpectedTestCaseSteps steps) throws Throwable {
        /*
         * really ugly workaround: CsvURLDataSet WANT a single .csv file, but
         * loads ALL THE DIRECTORY CONTENT! the trick is to keep the expected
         * tables from table-ordering.txt (witch MUST be set), and pass to
         * runTest a single "random" csv for the preps and the expected
         */
        final var cls = this.getClass();
        final var fmt = "%s/table-ordering.txt";

        final var e = testName("expected");
        final var eTables = readAllLines(of(cls.getResource(format(fmt, e))
                                               .toURI()));
        final var eTable = format("%s/%s.csv", e, eTables.get(0));

        final var p = testName("prep");
        final var pTables = readAllLines(of(cls.getResource(format(fmt, p))
                                               .toURI()));
        final var pTable = format("%s/%s.csv", p, pTables.get(0));

        return this.dbUnit.runTest(eTables.stream()
                                          .map(t -> new VerifyTableDefinition(t,
                                                                              null))
                                          .toArray(VerifyTableDefinition[]::new),
                                   new String[] {pTable},
                                   new String[] {eTable},
                                   steps);
    }

    private static String testName(final String stem) {
        for (final var ste : currentThread().getStackTrace()) {
            final var cls = ste.getClassName();
            if (cls.equals(Thread.class.getName()) ||
                cls.equals(DbUnitHelper.class.getName()) ||
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
}
