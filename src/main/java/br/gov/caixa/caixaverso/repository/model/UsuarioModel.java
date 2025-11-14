package br.gov.caixa.caixaverso.repository.model;

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
@Table(name = "users")
@Setter
@Getter
@NamedQuery(name = "findUsuarioByCpf", query = "SELECT u FROM UsuarioModel u WHERE co_cpf =:cpf")
public class UsuarioModel extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_id")
    Integer co_id;

    @Column(name = "co_cpf", unique = true)
    String co_cpf;

    @Column(name = "no_nome", nullable = false)
    String no_nome;

    @Column(name = "no_password", nullable = false)
    String no_password;
}
