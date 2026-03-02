package io.github.yuokada.npb;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Map;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSqlTestResource implements QuarkusTestResourceLifecycleManager {

    private PostgreSQLContainer<?> postgres;

    @Override
    public Map<String, String> start() {
        postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("npb")
            .withUsername("quarkus")
            .withPassword("quarkus");
        postgres.start();

        return Map.of(
            "quarkus.datasource.jdbc.url", postgres.getJdbcUrl(),
            "quarkus.datasource.username", postgres.getUsername(),
            "quarkus.datasource.password", postgres.getPassword(),
            "quarkus.datasource.devservices.enabled", "false",
            "quarkus.hibernate-orm.schema-management.strategy", "drop-and-create",
            "quarkus.hibernate-orm.sql-load-script", "import.sql"
        );
    }

    @Override
    public void stop() {
        if (postgres != null) {
            postgres.stop();
        }
    }
}
