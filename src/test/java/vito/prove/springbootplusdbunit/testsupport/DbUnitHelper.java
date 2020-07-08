package vito.prove.springbootplusdbunit.testsupport;

import org.dbunit.PrepAndExpectedTestCase;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract class needed for the @Autowired field
 */
public abstract class DbUnitHelper implements
                                   DbUnitHelperAction,
                                   DbUnitHelperSupplier {
    @Autowired
    PrepAndExpectedTestCase dbUnit;

    @Override
    public PrepAndExpectedTestCase getDbUnit() {
        return this.dbUnit;
    }
}
