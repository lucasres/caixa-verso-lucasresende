package br.gov.caixa.caixaverso.repository.model;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "simulacoes")
@Getter
@Setter
@NamedQuery(name = "findSimulacoesDoCliente", query = "SELECT s FROM SimulacoesModel s WHERE co_usuario_id = :clientId")
public class SimulacoesModel extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_id")
    Long co_id;

    @Column(name = "co_usuario_id", nullable = false)
    Long co_usuario_id;

    @Column(name = "de_produto", nullable = false)
    String de_produto;

    @Column(name = "nu_valor_investido", nullable = false)
    Double nu_valorInvestido;

    @Column(name = "nu_valor_final", nullable = false)
    Double nu_valor_final;

    @Column(name = "nu_prazo_meses", nullable = false)
    Integer nu_prazo_meses;

    @Column(name = "dt_criacao", nullable = false)
    LocalDateTime dt_criacao;
}
