package vip.geekclub.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class TestApplication {

    @Test
    public void test01() {

        for (int i = 0; i < 100; i++) {
            UUID uuid = UUID.randomUUID();
            System.out.println(uuid);
        }


    }

}
