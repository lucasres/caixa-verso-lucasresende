package br.gov.caixa.caixaverso.integracao;

import org.junit.jupiter.api.Test;

import br.gov.caixa.caixaverso.contracts.UsuarioPersistance;
import br.gov.caixa.caixaverso.profile.TesteProfile;
import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import br.gov.caixa.caixaverso.rest.dto.LoginRequestDTO;
import br.gov.caixa.caixaverso.rest.dto.RegistroRequestDTO;
import br.gov.caixa.caixaverso.utils.enums.PerfilUsuario;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@TestProfile(TesteProfile.class)
public class AuthRestTest {
    @Inject
    UsuarioPersistance usuarioPersistance;

    UsuarioModel usuarioModel;

    @Transactional
    void setup() {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setCo_cpf("11223456789");
        usuarioModel.setNo_nome("Lucas");
        usuarioModel.setNo_password("$2a$12$Rh7EcQ3p4OVwc38g7joQse.1wF/e5LDRa6yWXDIKmUykiRn/6Wjfy");

        usuarioPersistance.inserir(usuarioModel);
        this.usuarioModel = usuarioModel;
    }

    @Transactional
    void down() {
        usuarioModel.delete();
    }

    @Test
    void test_Conseguiu_Logar() {
        setup();

        RestAssured.given()
            .body(new LoginRequestDTO("11223456789", "12345678"))
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .post("/v1/auth")
            .then()
            .statusCode(200);
        down();
    }

    @Test
    void test_Conseguiu_Cadastrar() {
        RestAssured.given()
            .body(new RegistroRequestDTO("11111111", "12345678", "123", PerfilUsuario.USUARIO.getValor()))
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .post("/v1/auth/cadastro")
            .then()
            .statusCode(200);
    }

        @Test
    void test_Falhar_Logar() {
        setup();

        RestAssured.given()
            .body(new LoginRequestDTO("11223456789", "123456781111"))
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .post("/v1/auth")
            .then()
            .statusCode(400);
        down();
    }
}
