package vip.geekclub.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

@Configuration
//@EnableJpaAuditing  // 启用审计
@EnableJpaRepositories(
        basePackages = "vip.geekclub.**.domain.repository",
        bootstrapMode = BootstrapMode.LAZY
)
@EntityScan("vip.geekclub.**.domain")
public class JPAConfig {
}