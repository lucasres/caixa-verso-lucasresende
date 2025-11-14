package br.gov.caixa.caixaverso.rest.mapping;

import java.util.HashMap;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RegraInvalidaExceptionMapping implements ExceptionMapper<RegraInvalidaException> {
    @Override
    public Response toResponse(RegraInvalidaException exception) {
        HashMap<String, Object> retorno = new HashMap<>();
        retorno.put("menssagem", exception.getMessage());
        retorno.put("detalhes", exception.getDetalhes());

        return Response.status(Response.Status.BAD_REQUEST)
            .entity(retorno)
            .build();
    }
}
