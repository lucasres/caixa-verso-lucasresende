package br.gov.caixa.caixaverso.repository;

import java.util.List;

import br.gov.caixa.caixaverso.repository.dto.ResultadoPaginadoDTO;
import br.gov.caixa.caixaverso.repository.dto.SimulacoesAgrupadasPorDiaDTO;
import br.gov.caixa.caixaverso.repository.model.SimulacoesModel;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SimulacaoRepository implements PanacheRepository<SimulacoesModel> {
    @Transactional
    public SimulacoesModel inserir(SimulacoesModel model) {
        getEntityManager().persist(model);
        return model;
    }

    public ResultadoPaginadoDTO<SimulacoesModel> listar(Integer pagina, Integer quantidade) {
        PanacheQuery<SimulacoesModel> query = findAll(Sort.by("co_id"));
        query.page(pagina, quantidade);
        return new ResultadoPaginadoDTO<SimulacoesModel>(
            count(),
            query.list()
        );
    }

    public List<SimulacoesAgrupadasPorDiaDTO> agruparPorDia() {
        var query = getEntityManager().createNamedQuery(
            "agrupaSimulacoesPorDia",
            SimulacoesAgrupadasPorDiaDTO.class
        );
        return query.getResultList();
    }

    public List<SimulacoesModel> listarByClienteId(Long clienteId) {
        var query = getEntityManager().createNamedQuery(
            "listarSimulacoesByClienteId",
            SimulacoesModel.class
        );
        query.setParameter("cliente_id", clienteId);
        return query.getResultList();
    } 

    public ResultadoPaginadoDTO<SimulacoesModel> listarByClienteIdPaginado(Long clienteId, int pagina, int quantidade) {
        var query = find("co_usuario_id = ?1 ORDER BY co_id ASC", clienteId);
        query.page(pagina, quantidade);

        var queryCount = getEntityManager().createNamedQuery("quantidadeDeSimulacoesCliente", Long.class);
        queryCount.setParameter("cliente_id", clienteId);
        Long total = queryCount.getSingleResult();

        return new ResultadoPaginadoDTO<>(
            total,
            query.list()  
        );
    } 

}
