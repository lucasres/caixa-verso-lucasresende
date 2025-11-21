package br.gov.caixa.caixaverso.services;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

import br.gov.caixa.caixaverso.contracts.SimulacaoPersistance;
import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.ProdutoRepository;
import br.gov.caixa.caixaverso.repository.UsuariosRepository;
import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import br.gov.caixa.caixaverso.repository.model.SimulacoesModel;
import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import br.gov.caixa.caixaverso.rest.dto.SimulacaoRequestDTO;
import br.gov.caixa.caixaverso.services.dto.SimulacaoDTO;
import br.gov.caixa.caixaverso.utils.cache.CacheKeyGeneratorPerfilClient;
import io.quarkus.cache.CacheInvalidate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class CriarSimulacaoService {
    @Inject
    Logger logger = Logger.getLogger(CriarSimulacaoService.class);

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    UsuariosRepository usuariosRepository;

    @Inject
    SimulacaoPersistance simulacaoPersistance;

    @CacheInvalidate(cacheName = "cliente-", keyGenerator = CacheKeyGeneratorPerfilClient.class)
    public SimulacaoDTO executar(SimulacaoRequestDTO dados) throws RegraInvalidaException {
        logger.infof("iniciando simulação cliente: %d", dados.clienteId());

        ProdutoModel produto = produtoRepository.findByTipo(dados.tipoProduto());
        if (produto == null) {
            throw new RegraInvalidaException("Produto do tipo '" + dados.tipoProduto() + "' não existe na base");
        }

        UsuarioModel usuario = usuariosRepository.findById(dados.clienteId());
        if (usuario == null) {
            throw new RegraInvalidaException("Cliente '" + dados.clienteId() + "' não existe na base");
        }

        var jurosAoMes = anualParaMensal(produto.getNu_rentabilidade());
        var ultimoValor = dados.valor();
        List<Double> progressao = new ArrayList<>();

        for (int i = 1; i <= dados.prazoMeses(); i++) {
            Double novoValor = Math.round(ultimoValor * (1.0 + jurosAoMes) * 100.0) / 100.0;
            progressao.add(novoValor);
            ultimoValor = novoValor;
        }

        SimulacoesModel model = new SimulacoesModel();
        model.setCo_usuario_id(dados.clienteId());
        model.setDe_produto(produto.getCo_nome());
        model.setDt_criacao(LocalDate.now());
        model.setNu_prazo_meses(dados.prazoMeses());
        model.setNu_valorInvestido(dados.valor());
        model.setNu_valor_final(progressao.getLast());
        inserirAsync(model);

        String dataAtual = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));

        return new SimulacaoDTO(produto, dataAtual, progressao, dados.prazoMeses());
    }

    private double anualParaMensal(double taxaAnual) {
        return Math.pow(1 + taxaAnual, 1.0 / 12.0) - 1;
    }

    private void inserirAsync(SimulacoesModel model) {
        Thread.ofVirtual().start(() -> {
            simulacaoPersistance.inserir(model);
            logger.infof("simulação cliente: %d, persistida com id: %d", model.getCo_usuario_id(), model.getCo_id());
        });
    }
}
