package br.gov.caixa.caixaverso.rest;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("")
public class DocumentacaoRest {
    private static class DocumentacaoDados {
        @ConfigProperty(name = "dochost")
        public String URL;
    }

    @Inject
    @Location(value = "documentacao")
    Template documentacao;

    @Inject
    @Location(value = "simulacao")
    Template simulacao;

    @Path("/documentacao")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getDocumentacao() {
        return documentacao.render(new DocumentacaoDados());
    }

    @Path("/simulacao")
    @GET
    public TemplateInstance simulacao() {
        return simulacao.instance();
    }
}
