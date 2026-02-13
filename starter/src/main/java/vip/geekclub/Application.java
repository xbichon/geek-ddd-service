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
        System.out.println("=======================同步启动开始=======================");
        SpringApplication.run(Application.class, args);
        System.out.println("=======================同步启动完成=======================");
    }
}
