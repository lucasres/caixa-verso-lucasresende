package br.gov.caixa.caixaverso.utils.enums;

public enum PerfilUsuario {
    USUARIO("User"),
    ADMIN("Admin");

    private final String valor;

    PerfilUsuario(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}