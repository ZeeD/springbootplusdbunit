package vito.prove.springbootplusdbunit;

import static java.time.Instant.parse;
import static java.util.Date.from;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.RequiredArgsConstructor;
import vito.prove.springbootplusdbunit.model.MyModel;
import vito.prove.springbootplusdbunit.repository.MyTableRepository;
import vito.prove.springbootplusdbunit.testsupport.DbUnitHelper;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@TestMethodOrder(OrderAnnotation.class)
class ControllerDbTest {
    final DbUnitHelper helper;
    final MyRestController controller;
    final MyTableRepository repository;

    @Test
    @Order(1)
    void alwaysStartWithEmptyDb1() throws Throwable {
        assertEquals(List.of(), this.repository.findAll());
    }

    @Test
    @Order(2)
    void updateOrCopyUpdate() throws Throwable {
        final var request = new MyModel(Long.valueOf(123),
                                        "newName",
                                        from(parse("2020-11-05T05:58:13Z")));

        this.helper.runTest(() -> this.controller.createOrUpdate(request));
    }

    @Test
    @Order(3)
    void alwaysStartWithEmptyDb2() throws Throwable {
        assertEquals(List.of(), this.repository.findAll());
    }

    @Test
    @Order(4)
    void updateOrCopyCopy() throws Throwable {
        final var request = new MyModel(null,
                                        "name",
                                        from(parse("1982-11-05T05:58:13Z")));

        this.helper.runTest(() -> this.controller.createOrUpdate(request));
    }

    @Test
    @Order(5)
    void alwaysStartWithEmptyDb3() throws Throwable {
        assertEquals(List.of(), this.repository.findAll());
    }
}
