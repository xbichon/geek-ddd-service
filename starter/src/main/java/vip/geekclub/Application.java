package vip.geekclub;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        System.out.println("=======================同步启动完成=======================");

        DruidDataSource dataSource = context.getBean(DruidDataSource.class);
        System.out.println("=================================");
        System.out.println("🔥 Druid initial-size: " + dataSource.getInitialSize());
        System.out.println("🔥 Druid async-init: " + dataSource.isAsyncInit());
        System.out.println("🔥 Druid min-idle: " + dataSource.getMinIdle());
        System.out.println("=================================");
    }
}
