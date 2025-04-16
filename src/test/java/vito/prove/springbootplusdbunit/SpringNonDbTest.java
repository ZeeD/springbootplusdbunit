package vito.prove.springbootplusdbunit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor
class SpringNonDbTest {
    @Test
    void contextLoads() {
        assertTrue(true);
    }
}
