package br.gov.caixa.caixaverso.repository;

import br.gov.caixa.caixaverso.repository.model.SimulacoesModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SimulacaoRepository implements PanacheRepository<SimulacoesModel> {
    @Transactional
    public SimulacoesModel inserir(SimulacoesModel model) {
        getEntityManager().persist(model);
        return model;
    }
}
