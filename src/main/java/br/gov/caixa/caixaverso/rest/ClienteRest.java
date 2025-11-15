package br.gov.caixa.caixaverso.rest;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.services.MotorDePerfilService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
public class ClienteRest {
    @Inject
    MotorDePerfilService motorDePerfilService;

    @GET
    @Path("/perfil-risco/{clienteId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"User"})
    public Response getRiscoDoClient(
        @PathParam("clienteId") Long clienteId
    ) throws RegraInvalidaException {
        var perfil = motorDePerfilService.executar(clienteId);
        return Response.status(Response.Status.OK)
            .entity(perfil)
            .build();
    }
}
