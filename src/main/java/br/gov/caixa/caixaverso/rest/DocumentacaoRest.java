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
    @Location(value = "some-page")
    Template documentacao;

    @Path("/documentacao")
    @GET
    public TemplateInstance  getDocumentacao() {
        return documentacao.instance();
    }
}
