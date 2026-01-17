package vip.geekclub.initialize;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@ConditionalOnProperty(name = "spring.liquibase.enabled", havingValue = "false", matchIfMissing = true)
@AllArgsConstructor
@Async
public class LazyLiquibaseRunner implements ApplicationRunner {

    private DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) {
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

            liquibase.update(new Contexts(), new LabelExpression());
        } catch (Exception e) {
            throw new RuntimeException("Liquibase migration failed", e);
        }
    }
}