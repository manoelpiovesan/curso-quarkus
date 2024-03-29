package com.github.manoelpiovesan.api;

import com.github.manoelpiovesan.entity.Pix;
import com.github.manoelpiovesan.service.DictService;
import com.github.manoelpiovesan.service.PixService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

@Path("/v1/pix")
public class PixResource {

    @Inject
    DictService dictService;

    @Inject
    PixService pixService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/linha")
    public Response gerarLinhaDigitavel(final Pix pix) {
        var chave = dictService.buscarChave(pix.chave());

        if (Objects.nonNull(chave)) {
            return Response.ok(pixService.gerarLinhaDigitavel(chave, pix.valor(), pix.cidadeRemetente())).build()   ;
        }

        return null;
    }

    @GET
    @Produces("image/png")
    @Path("/{uuid}/qrcode")
    public Response gerarQrCode(@PathParam("uuid") final String uuid) {
        try {
            System.out.println(uuid);
            return Response.ok(pixService.gerarQrCode(uuid)).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{uuid}/aprovar")
    @PATCH
    public Response aprovarPix(@PathParam("uuid") String uuid) {

        return Response.ok(pixService.aprovarTransacao(uuid).get()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{uuid}/reprovar")
    @DELETE
    public Response reprovarPix(@PathParam("uuid") String uuid) {

        return Response.ok(pixService.reprovarTransacao(uuid).get()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{uuid}")
    @GET

    public Response buscarPix(@PathParam("uuid") String uuid) {

        return Response.ok(pixService.findById(uuid)).build();
    }







}
