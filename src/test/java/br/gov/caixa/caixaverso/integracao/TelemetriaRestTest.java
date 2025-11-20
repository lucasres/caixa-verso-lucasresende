package br.gov.caixa.caixaverso.integracao;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import br.gov.caixa.caixaverso.profile.TesteProfile;
import br.gov.caixa.caixaverso.repository.TelemetriaRepository;
import br.gov.caixa.caixaverso.repository.model.TelemetriaModel;
import br.gov.caixa.caixaverso.rest.dto.RegistroRequestDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@TestProfile(TesteProfile.class)
public class TelemetriaRestTest {
    @Inject
    TelemetriaRepository telemetriaRepository;

    @Transactional
    void setup() {
        TelemetriaModel model = new TelemetriaModel();
        model.setCo_path("/api/test");
        model.setNu_tempo(200);
        model.setDt_criacao(LocalDate.now());
        telemetriaRepository.persist(model);
    }

    @Test
    @TestSecurity(roles = {"Admin"}, user = "teste")
    void test_Conseguiu_Listar_Telemetria() {
        setup();
         RestAssured.given()
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .get("/telemetria")
            .then()
            .statusCode(200);
        
        RestAssured.given()
            .queryParam("inicio", "2025-11-01")
            .queryParam("fim", "2025-11-30")
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .get("/telemetria")
            .then()
            .statusCode(200);
    }

    @Test
    @TestSecurity(roles = {"Admin"}, user = "teste")
    void test_Nao_Conseguiu_Listar_Telemetria() {
        RestAssured.given()
        .queryParam("inicio", "ABC")
        .header("Content-Type",MediaType.APPLICATION_JSON)
        .when()
        .get("/telemetria")
        .then()
        .statusCode(400);
    }
}
