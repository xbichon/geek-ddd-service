package vip.geekclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {
         UserDetailsServiceAutoConfiguration.class
})
public class Application {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        SpringApplication.run(Application.class, args);
        long endTime = System.currentTimeMillis();
        System.out.println("=======================同步启动完成=======================");
        System.out.println("启动耗时: " + (endTime - startTime) + " ms (" + ((endTime - startTime) / 1000.0) + " 秒)");
        System.out.println("========================================================");
    }
}
