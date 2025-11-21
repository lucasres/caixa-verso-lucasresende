package br.gov.caixa.caixaverso.contracts;

import java.util.List;

import br.gov.caixa.caixaverso.repository.dto.ResultadoPaginadoDTO;
import br.gov.caixa.caixaverso.repository.dto.SimulacoesAgrupadasPorDiaDTO;
import br.gov.caixa.caixaverso.repository.model.SimulacoesModel;

public interface SimulacaoPersistance {
    public SimulacoesModel inserir(SimulacoesModel model);

    public ResultadoPaginadoDTO<SimulacoesModel> listar(Integer pagina, Integer quantidade);

    public List<SimulacoesAgrupadasPorDiaDTO> agruparPorDia();

    public List<SimulacoesModel> listarByClienteId(Long clienteId);

    public ResultadoPaginadoDTO<SimulacoesModel> listarByClienteIdPaginado(Long clienteId, int pagina, int quantidade);
}
