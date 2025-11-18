package br.gov.caixa.caixaverso.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.ProdutoRepository;
import br.gov.caixa.caixaverso.repository.SimulacaoRepository;
import br.gov.caixa.caixaverso.repository.UsuariosRepository;
import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import br.gov.caixa.caixaverso.repository.model.SimulacoesModel;
import br.gov.caixa.caixaverso.repository.model.UsuarioModel;
import br.gov.caixa.caixaverso.services.Enum.PerfilEnum;
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

    @Inject
    Logger logger = Logger.getLogger(MotorDePerfilService.class);

    private Map<String, Integer> riscoValor = Map.of("Baixo", 1, "Medio", 2, "Alto", 3);
    private Map<String, String> perfilDescricao = Map.of(
        PerfilEnum.CONSERVADOR,
        "Baixa movimentação, foco em liquidez",
        PerfilEnum.MODERADO,
        "Equilíbrio entre liquidez e rentabilidade",
        PerfilEnum.AGRESSIVO,
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
        parteDaMedia = (int) (mediaRiscos * 33);
        // calcula o valor referente ao volumes investido
        if (volumeInvestido < 10_000) {
            parteDoValorInvestido = (int) ((volumeInvestido/10_000) * 33.0);
        } else if (volumeInvestido < 100_000) {
            parteDoValorInvestido = (int) ((volumeInvestido/100_000) * 33.0 + 33.0) ;
        } else {
            parteDoValorInvestido = 99;
        }
        // calculo da frequencia
        if (frequenciaMovimentacao < 3) {
            parteFrequenciaInvestimento = (int) ((frequenciaMovimentacao/3) * 33.0);
        } else if (frequenciaMovimentacao < 6) {
            parteFrequenciaInvestimento = (int) ((frequenciaMovimentacao/6) * 33.0 + 33.0);
        } else {
            parteFrequenciaInvestimento = 66;
        }

        int riscoNormalizado = Math.min(parteDaMedia, 100);
        int volumeNormalizado = Math.min(parteDoValorInvestido, 100);
        int freqNormalizado = Math.min(parteFrequenciaInvestimento, 100);

        logger.infof("risco %d", riscoNormalizado * 70 / 100);
        logger.infof("valor %d", volumeNormalizado * 20 / 100);
        logger.infof("frequencia %d", freqNormalizado * 10 / 100);

        // Pesos
        int pesoRisco = 7;
        int pesoVolume = 2;
        int pesoFrequencia = 1;

        int somaPesos = pesoRisco + pesoVolume + pesoFrequencia;

         double score = (
                riscoNormalizado * pesoRisco +
                volumeNormalizado * pesoVolume +
                freqNormalizado * pesoFrequencia
        ) / (double) somaPesos;
        
        return (int) Math.round(score);
    }

    private String pontuacaoParaPerfil(Integer pontuacao) {
        if (pontuacao < 45) {
            return PerfilEnum.CONSERVADOR;
        }

        if (pontuacao < 66) {
            return PerfilEnum.MODERADO;
        }

        return PerfilEnum.AGRESSIVO;
    }
}
