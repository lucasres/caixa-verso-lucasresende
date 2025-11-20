package br.gov.caixa.caixaverso.unitarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.InvalidParameterException;

import org.junit.jupiter.api.Test;

import br.gov.caixa.caixaverso.rest.dto.SimulacaoRequestDTO;
import br.gov.caixa.caixaverso.utils.cache.CacheKeyGeneratorPerfilClient;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class CacheKeyGeneratorPerfilClienteTest {
    @Test
    void test_Gerar_Cache_Co_mSucesso() {
        CacheKeyGeneratorPerfilClient gerador = new CacheKeyGeneratorPerfilClient();

        Object retorno = gerador.generate(null, new Object[]{1L});
        assertEquals("perfil-cliente-1", retorno.toString());

        Object retorno2 = gerador.generate(null, new Object[]{new SimulacaoRequestDTO(1L, 0, 0, null)});
        assertEquals("perfil-cliente-1", retorno2.toString());
    }

    @Test
    void test_Gerar_Cache_Com_Error() {
        CacheKeyGeneratorPerfilClient gerador = new CacheKeyGeneratorPerfilClient();

        assertThrows(InvalidParameterException.class, () -> {
            gerador.generate(null, new Object[]{"error"});
        });

    }
}
