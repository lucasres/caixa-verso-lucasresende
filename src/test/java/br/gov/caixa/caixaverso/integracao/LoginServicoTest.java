package br.gov.caixa.caixaverso.integracao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.profile.TesteProfile;
import br.gov.caixa.caixaverso.repository.UsuariosRepository;
import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import br.gov.caixa.caixaverso.services.LoginService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
@TestProfile(TesteProfile.class)
class LoginServicoTest {
    @Inject
    LoginService loginService;

    @Inject
    UsuariosRepository usuariosRepository;

    @BeforeEach
    @Transactional
    void setup() {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setCo_cpf("123");
        usuarioModel.setNo_nome("Lucas");
        usuarioModel.setNo_password("$2a$12$P4/PmEQEtTP6478SF6Sfa.c7VL/uEb.sad2p1won3178s0gGIeBsC");

        // Mockito.when(usuariosRepository.findUsuarioByCpf("123")).thenReturn(usuarioModel);
        usuariosRepository.inserir(usuarioModel);
    }

    @Test
    void test_Deve_Gerar_Login() throws RegraInvalidaException {
        var login = loginService.executar("123", "123");
        assertNotNull(login.jwt(), "login retornou null");
    }
}
