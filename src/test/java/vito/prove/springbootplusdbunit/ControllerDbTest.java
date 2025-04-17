package vito.prove.springbootplusdbunit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static java.time.LocalDateTime.parse;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

import lombok.RequiredArgsConstructor;
import vito.prove.springbootplusdbunit.model.MyModel;
import vito.prove.springbootplusdbunit.repository.MyTableRepository;
import vito.prove.springbootplusdbunit.testsupport.DbUnitHelper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@TestMethodOrder(OrderAnnotation.class)
class ControllerDbTest {
    final DbUnitHelper helper;
    final MyTableRepository repository;
    final TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void alwaysStartWithEmptyDb1() throws Throwable {
        assertEquals(List.of(), this.repository.findAll());
    }

    @Test
    @Order(2)
    void testCreateOrUpdateDoUpdate() throws Throwable {
        final var request = new MyModel(Long.valueOf(7),
                                        "newName",
                                        parse("2020-11-05T05:58:13"));

        final var expected = Long.valueOf(7);
        final var actual = this.helper.runTest(() -> {
            return this.restTemplate.postForObject("/create-or-update",
                                                   request,
                                                   Long.class);
        });

        assertEquals(expected, actual);
    }

    @Test
    @Order(3)
    void alwaysStartWithEmptyDb2() throws Throwable {
        assertEquals(List.of(), this.repository.findAll());
    }

    @Test
    @Order(4)
    void testCreateOrUpdateDoCreate() throws Throwable {
        final var request = new MyModel(null,
                                        "name",
                                        parse("1982-11-05T05:58:13"));

        final var expected = Long.valueOf(1);
        final var actual = this.helper.runTest(() -> {
            return this.restTemplate.postForObject("/create-or-update",
                                                   request,
                                                   Long.class);
        });

        assertEquals(expected, actual);
    }

    @Test
    @Order(5)
    void alwaysStartWithEmptyDb3() throws Throwable {
        assertEquals(List.of(), this.repository.findAll());
    }
}
