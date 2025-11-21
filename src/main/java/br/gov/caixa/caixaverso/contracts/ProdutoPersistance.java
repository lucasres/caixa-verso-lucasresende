package br.gov.caixa.caixaverso.contracts;

import java.util.List;
import java.util.Set;

import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

public interface ProdutoPersistance {
    public ProdutoModel findByTipo(String tipo);

    public Set<ProdutoModel> findByRiscos(List<String> riscos);

    public PanacheQuery<ProdutoModel> findAll();

    public List<ProdutoModel> listAll();
}
