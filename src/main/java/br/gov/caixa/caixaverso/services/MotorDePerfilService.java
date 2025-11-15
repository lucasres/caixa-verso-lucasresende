package br.gov.caixa.caixaverso.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.ProdutoRepository;
import br.gov.caixa.caixaverso.repository.SimulacaoRepository;
import br.gov.caixa.caixaverso.repository.UsuariosRepository;
import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import br.gov.caixa.caixaverso.repository.model.SimulacoesModel;
import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import br.gov.caixa.caixaverso.services.dto.PerfilClienteDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MotorDePerfilService {
    @Inject
    SimulacaoRepository simulacaoRepository;

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    UsuariosRepository usuariosRepository;

    private Map<String, Integer> riscoValor = Map.of("Baixo", 1, "Medio", 2, "Alto", 3);
    private Map<String, String> perfilDescricao = Map.of(
        "Conservador",
        "Baixa movimentação, foco em liquidez",
        "Moderado",
        "Equilíbrio entre liquidez e rentabilidade",
        "Agressivo",
        "Busca por alta rentabilidade, maior risco"
    );

    public PerfilClienteDTO executar(Long clienteId) throws RegraInvalidaException {
        UsuarioModel has = usuariosRepository.findById(clienteId);
        if (has == null) {
            throw new RegraInvalidaException("Usuário com o id '" + clienteId + "' não foi encontrado na base");
        }

        var simulacoes = simulacaoRepository.listarByClienteId(clienteId);
        var produtos = getProdutoRisco();

        Integer somaRiscos = 0;
        Double volumeInvestido = 0.0;
        Integer frequenciaMovimentacao = simulacoes.size();

        for (SimulacoesModel simulacao : simulacoes) {
            var riscoProduto = produtos.get(simulacao.getDe_produto());
            somaRiscos += riscoValor.get(riscoProduto);
            volumeInvestido += simulacao.getNu_valorInvestido();
        }

        Integer pontuacao = calculaPontuacaoCliente(somaRiscos, volumeInvestido, frequenciaMovimentacao);
        String perfil = pontuacaoParaPerfil(pontuacao); 

        return new PerfilClienteDTO(
            clienteId,
            perfil,
            pontuacao,
            perfilDescricao.get(perfil)
        );
    }

    private Map<String, String> getProdutoRisco() {
        var produtos = produtoRepository.listAll();
        return produtos.stream()
            .collect(Collectors.toMap(ProdutoModel::getCo_nome, ProdutoModel::getIc_risco));
    }

    private Integer calculaPontuacaoCliente(
        Integer somaRiscos,
        Double volumeInvestido,
        Integer frequenciaMovimentacao

    ) {
        Integer parteDoValorInvestido = 0;
        Integer parteFrequenciaInvestimento = 0;
        Integer parteDaMedia = 0;
        // calcula o valor referente ao risco
        Double mediaRiscos = Math.round((somaRiscos / frequenciaMovimentacao) * 100.0) / 100.0;
        parteDaMedia = (int) (mediaRiscos * 10);
        // calcula o valor referente ao volumes investido
        if (volumeInvestido < 10_000) {
            parteDoValorInvestido = (int) ((volumeInvestido/10_000) * 10.0);
        } else if (volumeInvestido < 20_000) {
            parteDoValorInvestido = (int) ((volumeInvestido/20_000) * 10.0 + 10.0);
        } else {
            parteDoValorInvestido = 30;
        }
        // calculo da frequencia
        if (frequenciaMovimentacao < 3) {
            parteFrequenciaInvestimento = (int) ((frequenciaMovimentacao/3) * 10.0);
        } else if (frequenciaMovimentacao < 6) {
            parteFrequenciaInvestimento = (int) ((frequenciaMovimentacao/6) * 10.0 + 10.0);
        } else {
            parteFrequenciaInvestimento = 30;
        }
        
        return parteDaMedia + parteDoValorInvestido + parteFrequenciaInvestimento;
    }

    private String pontuacaoParaPerfil(Integer pontuacao) {
        if (pontuacao < 33) {
            return "Conservador";
        }

        if (pontuacao < 66) {
            return "Moderado";
        }

        return "Agressivo";
    }
}
