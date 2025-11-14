package br.gov.caixa.caixaverso.services;

import java.util.ArrayList;
import java.util.List;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.ProdutoRepository;
import br.gov.caixa.caixaverso.repository.UsuariosRepository;
import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import br.gov.caixa.caixaverso.rest.dto.SimulacaoRequestDTO;
import br.gov.caixa.caixaverso.services.dto.SimulacaoDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class CriarSimulacaoService {
    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    UsuariosRepository usuariosRepository;

    public SimulacaoDTO executar(SimulacaoRequestDTO dados) throws RegraInvalidaException {
        ProdutoModel produto = produtoRepository.findByTipo(dados.tipoProduto());
        if (produto == null) {
            throw new RegraInvalidaException("Produto do tipo '" + dados.tipoProduto() + "' não existe na base");
        }

        UsuarioModel usuario = usuariosRepository.findById(dados.clienteId());
        if (usuario == null) {
            throw new RegraInvalidaException("Cliente '" + dados.clienteId() + "' não existe na base");
        }

        var jurosAoMes = produto.getNu_rentabilidade() / 12;
        var ultimoValor = dados.valor();
        List<Double> progressao = new ArrayList<>();

        for (int i = 1; i <= dados.prazoMeses(); i++) {
            Double novoValor = Math.round(ultimoValor * (1.0 + jurosAoMes) * 100.0) / 100.0;
            progressao.add(novoValor);
            ultimoValor = novoValor;
        }

        String dataAtual = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));

        return new SimulacaoDTO(produto, dataAtual, progressao, dados.prazoMeses());
    }
}
