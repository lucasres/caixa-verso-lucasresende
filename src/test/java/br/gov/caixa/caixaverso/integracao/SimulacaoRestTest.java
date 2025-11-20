package br.gov.caixa.caixaverso.integracao;

import org.junit.jupiter.api.Test;

import br.gov.caixa.caixaverso.profile.TesteProfile;
import br.gov.caixa.caixaverso.repository.ProdutoRepository;
import br.gov.caixa.caixaverso.repository.UsuariosRepository;
import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import br.gov.caixa.caixaverso.rest.dto.SimulacaoRequestDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@TestProfile(TesteProfile.class)
class SimulacaoRestTest {
    @Inject
    UsuariosRepository usuariosRepository;

    @Inject
    ProdutoRepository produtoRepository;

    UsuarioModel usuarioModel;

    private String tipo = "CDB";

    @Transactional
    void setupUsuario() {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setCo_cpf("aaa123");
        usuarioModel.setNo_nome("LucasBB");
        usuarioModel.setNo_password("$2a$12$Rh7E23fds4OVwc38g7joQse.1wF/e5LDRa6yWXDIKmUykiRn/6Wjfy");

        usuariosRepository.inserir(usuarioModel);
        this.usuarioModel = usuarioModel;
    }

    @Transactional
    void setupSimular() {
        setupUsuario();

        var prod = produtoRepository.findByTipo(tipo);
        if (prod == null) {
            ProdutoModel produtoModel = new ProdutoModel();
            produtoModel.setCo_nome("Teste");;
            produtoModel.setIc_risco("baixo");
            produtoModel.setIc_tipo(tipo);
            produtoModel.setNu_rentabilidade((float) 0.12);
            produtoRepository.persist(produtoModel);
        }
    }

    @Transactional
    void tearDownSimular() {
        usuarioModel.delete();
    }

    @Test
    @TestSecurity(user = "123", roles = {"User"})
    void test_Conseguiu_Simular() {
        setupSimular();
        RestAssured.given()
            .body(new SimulacaoRequestDTO(
                (long) usuarioModel.getCo_id(),
                1000,
                12,
                tipo
            ))
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .post("/simular-investimento")
            .then()
            .statusCode(201);
        tearDownSimular();
    }
    
    @Test
    @TestSecurity(user = "123", roles = {"User"})
    void test_Conseguiu_Listar_Simulacoes() {
        RestAssured.given()
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .get("/simulacoes")
            .then()
            .statusCode(200);
    }

    @Test
    @TestSecurity(user = "123", roles = {"User"})
    void test_Conseguiu_Listar_Simulacoes_Por_Dia() {
        RestAssured.given()
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .get("/simulacoes/por-produto-dia")
            .then()
            .statusCode(200);
    }

    @Test
    @TestSecurity(user = "123", roles = {"User"})
    void test_Conseguiu_Listar_Simulacoes_Por_Cliente() {
        setupUsuario();
        RestAssured.given()
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .get("/investimentos/" + usuarioModel.getCo_id())
            .then()
            .statusCode(200);
        tearDownSimular();
    }

    @Test
    @TestSecurity(user = "123", roles = {"User"})
    void test_Nao_Conseguiu_Listar_Simulacoes_Por_Cliente() {
        setupUsuario();
        RestAssured.given()
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .queryParam("pagina", "-1")
            .when()
            .get("/investimentos/" + usuarioModel.getCo_id())
            .then()
            .statusCode(400);

        RestAssured.given()
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .queryParam("quantidade", "-1")
            .when()
            .get("/investimentos/" + usuarioModel.getCo_id())
            .then()
            .statusCode(400);
        tearDownSimular();
    }

    @Test
    @TestSecurity(user = "123", roles = {"User"})
    void test_Falha_Listar_Simulacoes_Por_Cliente() {
        RestAssured.given()
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .get("/investimentos/aaa")
            .then()
            .statusCode(400);
    }
}
