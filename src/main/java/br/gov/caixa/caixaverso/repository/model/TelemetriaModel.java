package br.gov.caixa.caixaverso.repository.model;

import java.time.LocalDate;

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
@Table(name = "telemetria")
@Getter
@Setter
@NamedQuery(
    name = "agruparPorPath",
    query = "SELECT COUNT(1) as quantidadeChamadas, co_path as nome, AVG(nu_tempo) as mediaTempoRespostaMs FROM TelemetriaModel WHERE dt_criacao BETWEEN :inicio AND :fim GROUP BY co_path"
)
public class TelemetriaModel extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_id")
    Long id;

    @Column(name = "co_path", nullable = false)
    String co_path;

    @Column(name = "nu_tempo", nullable = false)
    Integer nu_tempo;

    @Column(name = "dt_criacao")
    LocalDate dt_criacao;
}
