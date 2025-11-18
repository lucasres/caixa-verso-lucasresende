package br.gov.caixa.caixaverso.integracao;

import org.junit.jupiter.api.Test;

import br.gov.caixa.caixaverso.profile.TesteProfile;
import br.gov.caixa.caixaverso.rest.dto.LoginRequestDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@TestProfile(TesteProfile.class)
class ClienteRestTest extends BaseTeste {
    @Test
    @TestSecurity(user = "a", roles = {"User"})
    void test_Conseguiu_Calcular_Perfil() {
        setupPerfil();
        RestAssured.given()
            .body(new LoginRequestDTO("11223456789", "12345678"))
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .get("/perfil-risco/" + usuarioModel.getCo_id())
            .then()
            .statusCode(200);
        tearDownPerfil();
    }

    @Test
    @TestSecurity(user = "b", roles = {"User"})
    void test_Falha_Calcular_Perfil() {
        RestAssured.given()
            .body(new LoginRequestDTO("11223456789", "12345678"))
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .get("/perfil-risco/2000000")
            .then()
            .statusCode(400);
    }
}
