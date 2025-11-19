package br.gov.caixa.caixaverso.repository.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import lombok.Setter;

@Entity
@Table(name = "simulacoes")
@Getter
@Setter
@NamedQuery(name = "findSimulacoesDoCliente", query = "SELECT s FROM SimulacoesModel s WHERE co_usuario_id = :clientId")
@NamedQuery(
    name = "agrupaSimulacoesPorDia",
    query = "SELECT de_produto as nome, COUNT(1) as quantidadeSimulacoes, dt_criacao as data, AVG(nu_valor_final) as mediaValorFinal FROM SimulacoesModel s GROUP BY de_produto, dt_criacao"
)
@NamedQuery(
    name = "listarSimulacoesByClienteId",
    query = "SELECT s FROM SimulacoesModel s WHERE co_usuario_id = :cliente_id ORDER BY co_id ASC"
)
@NamedQuery(
    name = "quantidadeDeSimulacoesCliente",
    query = "SELECT count(1) FROM SimulacoesModel s WHERE co_usuario_id = :cliente_id"
)
public class SimulacoesModel extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_id")
    @JsonProperty("id")
    Long co_id;

    @Column(name = "co_usuario_id", nullable = false)
    @JsonProperty("clienteId")
    Long co_usuario_id;

    @Column(name = "de_produto", nullable = false)
    @JsonProperty("produto")
    String de_produto;

    @Column(name = "nu_valor_investido", nullable = false)
    @JsonProperty("valorInvestido")
    Double nu_valorInvestido;

    @Column(name = "nu_valor_final", nullable = false)
    @JsonProperty("valorFinal")
    Double nu_valor_final;

    @Column(name = "nu_prazo_meses", nullable = false)
    @JsonProperty("prazoMeses")
    Integer nu_prazo_meses;

    @Column(name = "dt_criacao", nullable = false)
    @JsonProperty("dataCriacao")
    LocalDate dt_criacao;
}
