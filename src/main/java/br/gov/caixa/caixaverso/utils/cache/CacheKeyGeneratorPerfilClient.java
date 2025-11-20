package br.gov.caixa.caixaverso.utils.cache;

import java.lang.reflect.Method;
import java.security.InvalidParameterException;

import br.gov.caixa.caixaverso.rest.dto.SimulacaoRequestDTO;
import io.quarkus.cache.CacheKeyGenerator;

public class CacheKeyGeneratorPerfilClient implements CacheKeyGenerator {
    @Override
    public Object generate(Method method, Object... methodParams) {
        String cacheKey = "perfil-cliente-";

        if (methodParams[0] instanceof Long casted) {
            cacheKey += casted.toString();
        } else if (methodParams[0] instanceof SimulacaoRequestDTO dados) {
            cacheKey += dados.clienteId().toString();
        } else {
            throw new InvalidParameterException("não foi possível criar a chave do cache para perfil de cliente");
        }

        return cacheKey;
    }
}
