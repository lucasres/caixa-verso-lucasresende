package br.gov.caixa.caixaverso.profile;

import java.util.Map;

import io.quarkus.test.junit.QuarkusTestProfile;

public class TesteProfile implements QuarkusTestProfile {
    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
            "quarkus.datasource.db-kind", "h2",
            "quarkus.datasource.username", "sa",
            "quarkus.datasource.password", "sa",
            "quarkus.datasource.jdbc.url", "jdbc:h2:mem:testdb",
            "quarkus.hibernate-orm.schema-management.strategy", "drop-and-create"
        );
    }
}
