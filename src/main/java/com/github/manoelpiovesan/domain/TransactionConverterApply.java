package com.github.manoelpiovesan.domain;

import com.github.manoelpiovesan.entity.Transaction;
import com.github.manoelpiovesan.enums.StatusPix;
import org.bson.Document;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class TransactionConverterApply {
    public static final String ID = "_id";
    public static final String VALOR = "valor";
    public static final String TIPO_CHAVE = "tipoChave";

    public static final String CHAVE = "chave";

    public static final String LINHA = "linha";

    public static final String STATUS = "status";

    public static final String DATA = "data";


    public static Transaction apply(final Document document) {
        var transaction = new Transaction();
        transaction.setId(document.getString(ID));
        transaction.setValor(BigDecimal.valueOf(document.get(VALOR,
                                                             Decimal128.class).doubleValue()));
        transaction.setStatus(StatusPix.valueOf(document.getString(STATUS)));
        transaction.setLinha(document.getString(LINHA));
        transaction.setChave(document.getString(CHAVE));
        transaction.setTipoChave(document.getString(TIPO_CHAVE));
        transaction.setData(document.get(DATA, Date.class).toInstant()
                                    .atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()
        );
        return transaction;
    }

}
