package br.gov.caixa.caixaverso.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.ProdutoRepository;
import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import br.gov.caixa.caixaverso.services.Enum.PerfilEnum;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MotorDeRecomendacaoService {
    @Inject
    Logger logger = Logger.getLogger(MotorDeRecomendacaoService.class);

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    MotorDePerfilService motorDePerfilService;

    private Map<String, String> perfilRisco = Map.of(
        PerfilEnum.CONSERVADOR,
        "Baixo",
        PerfilEnum.MODERADO,
        "Medio",
        PerfilEnum.AGRESSIVO,
        "Alto"
    );

    public List<ProdutoModel> executar(String perfilSolicitado, Long clienteId) throws RegraInvalidaException {
        var perfilCliente = motorDePerfilService.executar(clienteId);
        logger.infof("perfil do client %s", perfilCliente.perfil());

        String risco = perfilRisco.get(perfilSolicitado);
        List<String> riscosAceitados = new ArrayList<>();
        riscosAceitados.add(risco);

        if (!perfilCliente.perfil().toLowerCase().equals(perfilSolicitado.toLowerCase())) {
            riscosAceitados.add(perfilRisco.get(perfilCliente.perfil()));
        }

        List<ProdutoModel> produtosAceitos = produtoRepository.findByRiscos(riscosAceitados);
        return produtosAceitos;
    }
}
