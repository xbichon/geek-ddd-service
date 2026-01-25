package vip.geekclub.initialize;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Slf4j
@Component
@ConditionalOnProperty(name = "spring.liquibase.enabled", havingValue = "false", matchIfMissing = true)
@AllArgsConstructor
public class LazyLiquibaseRunner implements ApplicationRunner {

    private DataSource dataSource;

    @Async
    @Override
    public void run(@NonNull ApplicationArguments args) {
        runLiquibaseMigration();
    }

    private void runLiquibaseMigration() {
        // 手动执行Liquibase迁移
        try {
            Liquibase liquibase = new Liquibase(
                    "database/migrations/master.xml",
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