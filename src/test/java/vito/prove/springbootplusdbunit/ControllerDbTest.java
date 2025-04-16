package vito.prove.springbootplusdbunit;

import static java.time.Instant.parse;
import static java.util.Date.from;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.RequiredArgsConstructor;
import vito.prove.springbootplusdbunit.entity.MyTable;
import vito.prove.springbootplusdbunit.testsupport.DbUnitHelper;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@TestMethodOrder(OrderAnnotation.class)
class ControllerDbTest {
    final DbUnitHelper helper;
    final MyRestController controller;

    @Test
    @Order(1)
    void alwaysStartWithEmptyDb1() throws Throwable {
        assertFalse(this.controller.findAll().iterator().hasNext());
    }

    @Test
    @Order(2)
    void updateOrCopyUpdate() throws Throwable {
        final var myTable = MyTable.of(Long.valueOf(123),
                                       "newName",
                                       from(parse("2020-11-05T05:58:13Z")));

        this.helper.runTest(() -> {
            this.controller.updateOrCopy(myTable);
            return null;
        });
    }

    @Test
    @Order(3)
    void alwaysStartWithEmptyDb2() throws Throwable {
        assertFalse(this.controller.findAll().iterator().hasNext());
    }

    @Test
    @Order(4)
    void updateOrCopyCopy() throws Throwable {
        final var myTable = MyTable.of(null,
                                       "name",
                                       from(parse("1982-11-05T05:58:13Z")));

        this.helper.runTest(() -> {
            this.controller.updateOrCopy(myTable);
            return null;
        });
    }

    @Test
    @Order(5)
    void alwaysStartWithEmptyDb3() throws Throwable {
        assertFalse(this.controller.findAll().iterator().hasNext());
    }
}
