package br.gov.caixa.caixaverso.rest;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("")
public class DocumentacaoRest {
    @Inject
    @Location(value = "documentacao")
    Template documentacao;

    @Inject
    @Location(value = "simulacao")
    Template simulacao;

    @Path("/documentacao")
    @GET
    public TemplateInstance  getDocumentacao() {
        return documentacao.instance();
    }

    @Path("/simulacao")
    @GET
    public TemplateInstance simulacao() {
        return simulacao.instance();
    }
}
