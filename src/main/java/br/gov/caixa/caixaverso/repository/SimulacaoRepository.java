package br.gov.caixa.caixaverso.repository;

import br.gov.caixa.caixaverso.repository.dto.ResultadoPaginadoDTO;
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
}
