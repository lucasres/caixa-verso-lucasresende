package br.gov.caixa.caixaverso.unitarios;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class RegraInvalidaExceptionTest {
    @Test
    void test_Getters() {
        var msg = "teste";
        var detalhes = List.of("a", "b");

        RegraInvalidaException exception = new RegraInvalidaException(msg, detalhes);
        assertEquals(msg, exception.getMessage());
        assertEquals(detalhes, exception.getDetalhes());
    }
}
