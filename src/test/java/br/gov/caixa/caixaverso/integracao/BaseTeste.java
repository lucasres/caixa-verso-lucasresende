package br.gov.caixa.caixaverso.integracao;

import java.time.LocalDate;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mockito.Mockito;

import br.gov.caixa.caixaverso.repository.SimulacaoRepository;
import br.gov.caixa.caixaverso.repository.UsuariosRepository;
import br.gov.caixa.caixaverso.repository.model.SimulacoesModel;
import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public class BaseTeste {
    @Inject
    UsuariosRepository usuariosRepository;
    
    @Inject
    SimulacaoRepository simulacaoRepository;

    @InjectMock
    JsonWebToken jwt;

    UsuarioModel usuarioModel;

    @Transactional
    void setupPerfil() {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setCo_cpf("12345678111");
        usuarioModel.setNo_nome("AAAABBB");
        usuarioModel.setNo_password("$2a$12$Rh7EcQ3p4OVwc38g7joQse.231/e5LDRa6yWXDIKmUykiRn/6Wjfy");

        usuariosRepository.inserir(usuarioModel);
        this.usuarioModel = usuarioModel;

        Mockito.when(jwt.getClaim("clienteId")).thenReturn(usuarioModel.getCo_id());

        SimulacoesModel simulacoesModel = criarSimulacao((long) usuarioModel.getCo_id());
        simulacaoRepository.inserir(simulacoesModel);


        simulacaoRepository.inserir(simulacoesModel);
    }

    @Transactional
    void tearDownPerfil() {
        usuarioModel.delete();
    }

    public SimulacoesModel criarSimulacao(Long cliente) {
        SimulacoesModel simulacoesModel = new SimulacoesModel();
        simulacoesModel.setCo_usuario_id(cliente);
        simulacoesModel.setDe_produto("RendaFixa Caixa 2026");
        simulacoesModel.setNu_valorInvestido(1000.0);
        simulacoesModel.setNu_valor_final(1200.0);
        simulacoesModel.setNu_prazo_meses(12);
        simulacoesModel.setDt_criacao(LocalDate.now());
        return simulacoesModel;
    }
}
