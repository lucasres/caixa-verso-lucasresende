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
public class ProdutoRestTest extends BaseTeste {

    @Test
    @TestSecurity(user = "123", roles = {"User"})
    void test_Conseguiu_Recuperar_Perfil() {
        setupPerfil();

        RestAssured.given()
            .body(new LoginRequestDTO("11223456789", "12345678"))
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .get("/produtos-recomendados/Conservador")
            .then()
            .statusCode(200);
        tearDownPerfil();
    }

    @Test
    @TestSecurity(user = "123", roles = {"User"})
    void test_Conseguiu_Recuperar_Perfil_Com_Flags() {
        setupPerfil();

        RestAssured.given()
            .queryParam("flagProdutoMaisNovo", true)
            .queryParam("flagMaiorRentabilidade", true)
            .body(new LoginRequestDTO("11223456789", "12345678"))
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .get("/produtos-recomendados/Conservador")
            .then()
            .statusCode(200);
        tearDownPerfil();
    }
}
