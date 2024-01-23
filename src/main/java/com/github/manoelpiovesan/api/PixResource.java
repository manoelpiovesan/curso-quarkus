package com.github.manoelpiovesan.api;

import com.github.manoelpiovesan.entity.Pix;
import com.github.manoelpiovesan.entity.Transaction;
import com.github.manoelpiovesan.service.DictService;
import com.github.manoelpiovesan.service.PixService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Path("/v1/pix")
public class PixResource {

    @Inject
    DictService dictService;

    @Inject
    PixService pixService;

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @POST
    @Path("/linha")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response gerarLinhaDigitavel(final Pix pix) {
        var chave = dictService.buscarChave(pix.chave());

        if (Objects.nonNull(chave)) {
            return Response.ok(
                                   pixService.gerarLinhaDigitavel(chave, pix.valor(),
                                                                  pix.cidadeRemetente()))
                           .build();
        }

        return null;
    }

    @GET
    @Path("/{uuid}/qrcode")
    @Produces("image/png")
    public Response gerarQrCode(@PathParam("uuid") final String uuid) {
        try {
            System.out.println(uuid);
            return Response.ok(pixService.gerarQrCode(uuid)).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PATCH
    @Path("/{uuid}/aprovar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response aprovarPix(@PathParam("uuid") String uuid) {

        return Response.ok(pixService.aprovarTransacao(uuid).get()).build();
    }

    @DELETE
    @Path("/{uuid}/reprovar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reprovarPix(@PathParam("uuid") String uuid) {

        return Response.ok(pixService.reprovarTransacao(uuid).get()).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/transacoes")
    @GET
    @Operation(description = "API responsável por buscar pagamentos PIX")
    @APIResponseSchema(Transaction.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK"),
            @APIResponse(responseCode = "201", description = "Retorno OK com a transação criada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado")
    })
    @Parameter(
            name = "dataInicio",
            in = ParameterIn.QUERY,
            description = "Data de Inicio no formato yyyy-MM-dd"
    )
    @Parameter(
            name = "dataFim",
            in = ParameterIn.QUERY,
            description = "Data de Fim no formato yyyy-MM-dd"
    )
    public Response buscarTransacoes(@QueryParam(value = "dataInicio") String dataInicio, @QueryParam(value = "dataFim") String dataFim) throws ParseException {
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");

        Date dataInicioFormatada = formatoData.parse(dataInicio);
        Date dataFimFormatada = formatoData.parse(dataFim);

        return Response.ok(pixService.buscarTransacoes(dataInicioFormatada,dataFimFormatada)).build();
    }


    @GET
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response buscarPix(@PathParam("uuid") String uuid) {

        return Response.ok(pixService.findById(uuid)).build();
    }

}
