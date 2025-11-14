package br.gov.caixa.caixaverso.repository.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class SimulacoesMode extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_id")
    Integer co_id;

    @Column(name = "co_nome", nullable = false)
    String co_nome;

    @Column(name = "ic_risco", nullable = false)
    String ic_risco;

    @Column(name = "ic_tipo", nullable = false)
    String ic_tipo;

    @Column(name = "nu_rentabilidade", nullable = false)
    Float nu_rentabilidade;
}
