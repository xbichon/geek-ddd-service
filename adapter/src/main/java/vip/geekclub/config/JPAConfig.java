package vip.geekclub.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
//@EnableJpaAuditing  // 启用审计
@EntityScan("vip.geekclub.**.domain")
public class JPAConfig {
}
