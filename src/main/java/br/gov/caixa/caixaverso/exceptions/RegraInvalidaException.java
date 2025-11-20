package br.gov.caixa.caixaverso.exceptions;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegraInvalidaException extends Throwable {
    List<String> detalhes;
    String mensgem;

    public RegraInvalidaException(String mensagem, List<String> detalhes) {
        super(mensagem);
        this.detalhes = detalhes != null ? detalhes : List.of();
    }

    public RegraInvalidaException(String mensagem) {
        super(mensagem);
        this.detalhes = List.of();
    }
}
