package br.gov.caixa.caixaverso.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.logging.Logger;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.ProdutoRepository;
import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import br.gov.caixa.caixaverso.services.Enum.PerfilEnum;
import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
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

    public Set<ProdutoModel> executar(
        String perfilSolicitado,
        Long clienteId,
        Boolean flagMaiorRendimento,
        Boolean flagMaisNovo
    ) throws RegraInvalidaException {
        if (flagMaiorRendimento == null) {
            flagMaiorRendimento = false;
        }

        if (flagMaisNovo == null) {
            flagMaisNovo = false;
        }

        var perfilCliente = motorDePerfilService.executar(clienteId);
        logger.infof("perfil do client %s", perfilCliente.perfil());
        logger.infof("Flag maior rendimento: %s", flagMaiorRendimento ? "sim" : "nao");
        logger.infof("Flag mais novo: %s", flagMaisNovo ? "sim" : "nao");

        String risco = perfilRisco.get(perfilSolicitado);
        List<String> riscosAceitados = new ArrayList<>();
        riscosAceitados.add(risco);

        if (!perfilCliente.perfil().toLowerCase().equals(perfilSolicitado.toLowerCase())) {
            riscosAceitados.add(perfilRisco.get(perfilCliente.perfil()));
        }

        Set<ProdutoModel> recomendacoes = produtoRepository.findByRiscos(riscosAceitados);

        if (flagMaiorRendimento) {
            addProdutoMaiorRendimento(recomendacoes);
        }

        if (flagMaisNovo) {
            addProdutoNovo(recomendacoes);
        }

        return recomendacoes;
    }

    private void addProdutoMaiorRendimento(Set<ProdutoModel> recomendacoes) {
        var maior = getProdutoMaiorRendimento();
        if (maior != null) {
            recomendacoes.add(maior);
        }
    }

    private void addProdutoNovo(Set<ProdutoModel> recomendacoes) {
        var novo = getProdutoMaisNovo();
        if (novo != null) {
            recomendacoes.add(novo);
        }
    }

    @CacheResult(cacheName = "allProdutos")
    public PanacheQuery<ProdutoModel> getAllProdutos() {
        return produtoRepository.findAll();
    }

    @CacheResult(cacheName = "maiorRendimento")
    public ProdutoModel getProdutoMaiorRendimento() {
        return getAllProdutos()
            .stream()
            .max(Comparator.comparing(ProdutoModel::getNu_rentabilidade))
            .orElse(null);
    }

    @CacheResult(cacheName = "produtoMaisNovo")
    public ProdutoModel getProdutoMaisNovo() {
        return getAllProdutos()
            .stream()
            .max(Comparator.comparing(ProdutoModel::getDt_criacao))
            .orElse(null);
    }
}
