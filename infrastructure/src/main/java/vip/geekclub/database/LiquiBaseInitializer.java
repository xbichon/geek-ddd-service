package vip.geekclub.database;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.initialize.Initializer;

import javax.sql.DataSource;

@Slf4j
@Component
@ConditionalOnProperty(name = "spring.liquibase.enabled", havingValue = "false", matchIfMissing = true)
@AllArgsConstructor
@Order(Integer.MIN_VALUE)
public class LiquiBaseInitializer implements Initializer {

    private DataSource dataSource;

    @Override
    public void initialize() {
        // 手动执行Liquibase迁移
        try {
            Liquibase liquibase = new Liquibase(
                    "db/migration/master.xml",
                    new ClassLoaderResourceAccessor(),
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                            new JdbcConnection(dataSource.getConnection())
                    )
            );

            log.info("执行Liquibase数据库迁移...");
            liquibase.update(new Contexts(), new LabelExpression());
            log.info("Liquibase数据库迁移完成");

        } catch (Exception e) {
            log.error("Liquibase数据库迁移失败", e);
            throw new RuntimeException("Liquibase migration failed", e);
        }
    }
}