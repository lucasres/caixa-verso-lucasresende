package br.gov.caixa.caixaverso.integracao;

import java.time.LocalDate;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.gov.caixa.caixaverso.profile.TesteProfile;
import br.gov.caixa.caixaverso.repository.SimulacaoRepository;
import br.gov.caixa.caixaverso.repository.UsuariosRepository;
import br.gov.caixa.caixaverso.repository.model.SimulacoesModel;
import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import br.gov.caixa.caixaverso.rest.dto.LoginRequestDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import io.vertx.ext.auth.impl.jose.JWT;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@TestProfile(TesteProfile.class)
public class ProdutoRestTest {
    @Inject
    UsuariosRepository usuariosRepository;

    @Inject
    SimulacaoRepository simulacaoRepository;

    @InjectMock
    JsonWebToken jwt;
    
    UsuarioModel usuarioModel;

    @Transactional
    void setup() {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setCo_cpf("11992299");
        usuarioModel.setNo_nome("Lucas");
        usuarioModel.setNo_password("$2a$12$Rh7EcQ3p4OVwc38g7joQse.1wF/e5LDRa6yWXDIKmUykiRn/6Wjfy");

        // Mockito.when(usuariosRepository.findUsuarioByCpf("123")).thenReturn(usuarioModel);
        usuariosRepository.inserir(usuarioModel);
        this.usuarioModel = usuarioModel;

        Mockito.when(jwt.getClaim("clienteId")).thenReturn(usuarioModel.getCo_id());

        SimulacoesModel simulacoesModel = new SimulacoesModel();
        simulacoesModel.setCo_usuario_id((long) usuarioModel.getCo_id());
        simulacoesModel.setDe_produto("Produto Teste");
        simulacoesModel.setNu_valorInvestido(1000.0);
        simulacoesModel.setNu_valor_final(1200.0);
        simulacoesModel.setNu_prazo_meses(12);
        simulacoesModel.setDt_criacao(LocalDate.now());
        simulacaoRepository.inserir(simulacoesModel);
    }

    @Test
    @TestSecurity(user = "123", roles = {"User"})
    void test_Conseguiu_Recuperar_Perfil() {
        setup();

        RestAssured.given()
            .body(new LoginRequestDTO("11223456789", "12345678"))
            .header("Content-Type",MediaType.APPLICATION_JSON)
            .when()
            .get("/produtos-recomendados/Conservador")
            .then()
            .statusCode(200);
    }
}
