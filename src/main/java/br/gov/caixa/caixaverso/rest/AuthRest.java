package br.gov.caixa.caixaverso.rest;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.gov.caixa.caixaverso.exceptions.RegraInvalidaException;
import br.gov.caixa.caixaverso.rest.dto.LoginRequestDTO;
import br.gov.caixa.caixaverso.rest.dto.RegistroRequestDTO;
import br.gov.caixa.caixaverso.services.LoginService;
import br.gov.caixa.caixaverso.services.RegistroService;
import br.gov.caixa.caixaverso.services.dto.LoginDTO;
import br.gov.caixa.caixaverso.utils.examples.LoginExemplo;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/auth")
@Tag(name = "Auth")
public class AuthRest {

    @Inject
    LoginService loginService;

    @Inject
    RegistroService registroService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Operation(description = "Login de usuário.")
    @RequestBody(
        required = true,
        content = @Content(
            schema = @Schema(implementation = LoginRequestDTO.class, required = true),
            examples = @ExampleObject(
              name = "Payload de login",
              description = "Gera um token JWT.",
              value = LoginExemplo.LOGIN_OK
            )
        )
    )
    @APIResponses(
        value = {
            @APIResponse(
                name = "Sucesso no login",
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = LoginDTO.class)
                )
            ),
            @APIResponse(
                name = "Regra inválida",
                responseCode = "400",
                description = "Algum dado fornecido não estava válido",
                content = @Content(
                    schema = @Schema(implementation = RegraInvalidaException.class)
                )
            )
        }
    )
    public Response login(
        @Valid
        LoginRequestDTO request
    ) throws RegraInvalidaException {
        Response.Status status = Response.Status.OK;

        Object retornoApi = loginService.executar(request.cpf(), request.password());

        return Response.status(status)
            .entity(retornoApi)
            .build();
    }

    @POST
    @Path("/cadastro")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Operation(description = "Criar novo usuário.")
    @RequestBody(
        required = true,
        content = @Content(
            schema = @Schema(implementation = RegistroRequestDTO.class, required = true),
            examples = @ExampleObject(
              name = "Payload de login",
              description = "Gera um token JWT.",
              value = LoginExemplo.CADASTRO_OK
            )
        )
    )
    @APIResponses(
        value = {
            @APIResponse(
                name = "Sucesso no login",
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = LoginDTO.class)
                )
            ),
            @APIResponse(
                name = "Regra inválida",
                responseCode = "400",
                description = "Algum dado fornecido não estava válido",
                content = @Content(
                    schema = @Schema(implementation = RegraInvalidaException.class)
                )
            )
        }
    )
    public Response cadastro(
        @Valid
        RegistroRequestDTO request
    ) throws RegraInvalidaException {
        Response.Status status = Response.Status.OK;

        var usuario = registroService.executar(request.cpf(), request.password(), request.nome(), request.perfil());

        return Response.status(status)
            .entity(usuario)
            .build();
    }
}
