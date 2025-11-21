package br.gov.caixa.caixaverso.unitarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.gov.caixa.caixaverso.contracts.SimulacaoPersistance;
import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.integracao.BaseTeste;
import br.gov.caixa.caixaverso.profile.TesteProfile;
import br.gov.caixa.caixaverso.repository.ProdutoRepository;
import br.gov.caixa.caixaverso.repository.UsuariosRepository;
import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import br.gov.caixa.caixaverso.services.MotorDePerfilService;
import br.gov.caixa.caixaverso.services.Enum.PerfilEnum;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;

@QuarkusTest
@TestProfile(TesteProfile.class)
public class MotorPerfilServiceTest extends BaseTeste {
    @Inject
    MotorDePerfilService motorDePerfilService;

    @InjectMock
    SimulacaoPersistance simulacaoPersistance;

    @InjectMock
    ProdutoRepository produtoRepository;

    @InjectMock
    UsuariosRepository usuariosRepository;

    @Test
    void test_Perfil_Agressivo() throws RegraInvalidaException {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setCo_id(1);

        Mockito.when(usuariosRepository.findById(1L)).thenReturn(usuarioModel);
        
        Mockito.when(simulacaoPersistance.listarByClienteId(1L)).thenReturn(
            List.of(
                criarSimulacao(1L),
                criarSimulacao(1L),
                criarSimulacao(1L),
                criarSimulacao(1L)
            )
        );

        ProdutoModel produtoModel = new ProdutoModel();
        produtoModel.setCo_id(1);
        produtoModel.setCo_nome("RendaFixa Caixa 2026");
        produtoModel.setIc_risco("Alto");
        produtoModel.setIc_tipo("Investimento");
        produtoModel.setNu_rentabilidade(0.5f);

        Mockito.when(produtoRepository.listAll()).thenReturn(
            List.of(produtoModel)
        );

        var retorno = motorDePerfilService.executar(1L);
        assertNotNull(retorno);
        assertEquals(PerfilEnum.AGRESSIVO, retorno.perfil());

    }
}
