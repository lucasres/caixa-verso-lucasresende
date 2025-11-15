package br.gov.caixa.caixaverso.rest;

import java.util.List;

import org.jboss.logging.Logger;

import br.gov.caixa.caixaverso.repository.ProdutoRepository;
import br.gov.caixa.caixaverso.repository.model.ProdutoModel;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
public class ProdutoRest {
    @Inject
    Logger logger = Logger.getLogger(ProdutoRest.class);

    @Inject
    ProdutoRepository produtoRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/produtos-recomendados/{perfil}")
    public Response getDeProduto(
        @PathParam("perfil") String perfil
    ) {
        Response.Status status = Response.Status.OK;
        List<ProdutoModel> produtos = produtoRepository.findAllByRisco(perfil);
        logger.info("perfil passado: " + perfil);

        if (produtos.isEmpty()) {
            status = Response.Status.NOT_FOUND;
        }

        return Response.status(status)
            .entity(produtos)
            .build();

    }
}
