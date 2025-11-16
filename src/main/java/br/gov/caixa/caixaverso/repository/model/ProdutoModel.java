package br.gov.caixa.caixaverso.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "produtos")
@Getter
@NamedQuery(name = "findByTipo", query = "SELECT p FROM ProdutoModel p WHERE ic_tipo = :tipo")
@NamedQuery(name = "findAllByRiscos", query = "SELECT p FROM ProdutoModel p WHERE ic_risco IN :riscos")
public class ProdutoModel extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_id")
    @JsonProperty("id")
    Integer co_id;

    @Column(name = "co_nome", nullable = false)
    @JsonProperty("nome")
    String co_nome;

    @Column(name = "ic_risco", nullable = false)
    @JsonProperty("risco")
    String ic_risco;

    @Column(name = "ic_tipo", nullable = false)
    @JsonProperty("tipo")
    String ic_tipo;

    @Column(name = "nu_rentabilidade", nullable = false)
    @JsonProperty("rentabilidade")
    Float nu_rentabilidade;
}
