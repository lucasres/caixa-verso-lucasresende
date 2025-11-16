package br.gov.caixa.caixaverso.rest;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.repository.ProdutoRepository;
import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import br.gov.caixa.caixaverso.services.MotorDeRecomendacaoService;
import br.gov.caixa.caixaverso.services.dto.RecomendacaoDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("")
public class ProdutoRest {
    @Inject
    JsonWebToken jwt;

    @Inject
    Logger logger = Logger.getLogger(ProdutoRest.class);

    @Inject
    MotorDeRecomendacaoService motorDeRecomendacaoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/produtos-recomendados/{perfil}")
    public Response getDeProduto(
        @PathParam("perfil") String perfil,
        @Context SecurityContext ctx
    ) throws RegraInvalidaException {
        var clienteId = Long.parseLong(jwt.getClaim("clienteId").toString());
        logger.info("perfil passado: " + perfil);
        logger.info("client id: " +  clienteId);
        Response.Status status = Response.Status.OK;
        var produtos = motorDeRecomendacaoService.executar(perfil, clienteId);

        return Response.status(status)
            .entity(produtos)
            .build();

    }
}
