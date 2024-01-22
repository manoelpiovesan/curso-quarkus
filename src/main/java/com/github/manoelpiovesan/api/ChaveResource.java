package com.github.manoelpiovesan.api;

import com.github.manoelpiovesan.service.DictService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/chave")
public class ChaveResource {
    @Inject
    DictService dictService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{key}")
    public Response buscarChave(@PathParam("key") final String key) {
        return Response.ok(dictService.buscarDetalhesChave(key)).build();
    }
}
