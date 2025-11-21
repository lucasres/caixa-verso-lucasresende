package br.gov.caixa.caixaverso.unitarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.gov.caixa.caixaverso.contracts.TelemetriaPersistance;
import br.gov.caixa.caixaverso.repository.dto.TelemetriaAgrupadasPorPath;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class TelemetriaServiceTest {
    @InjectMock
    TelemetriaPersistance telemetriaPersistance;

    @Test
    void test_Listar_Dados_Telemetria() {
        TelemetriaAgrupadasPorPath dadosTelemetria = new TelemetriaAgrupadasPorPath(
            12L,
            "/teste",
            20.0
        );
        
        Mockito.when(telemetriaPersistance.agruparPorPath(any(), any()))
            .thenReturn(List.of(dadosTelemetria, dadosTelemetria, dadosTelemetria));

        assertEquals(3, telemetriaPersistance.agruparPorPath(LocalDate.now(), LocalDate.now()).size());
    }
}
