package com.github.manoelpiovesan.service;


import com.github.manoelpiovesan.entity.Chave;
import com.github.manoelpiovesan.entity.LinhaDigitavel;
import com.github.manoelpiovesan.entity.qrcode.DadosEnvio;
import com.github.manoelpiovesan.entity.qrcode.QrCode;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.UUID;

@ApplicationScoped
public class PixService {

    public static final String QRCODE_PATH = "/tmp/qrcode";


    public LinhaDigitavel gerarLinhaDigitavel(final Chave chave, BigDecimal valor, String cidadeRemetente) {

        var qrCode = new QrCode(new DadosEnvio(chave, valor, cidadeRemetente));
        var uuid = UUID.randomUUID().toString();
        var imagePath = QRCODE_PATH + uuid + ".png";
        qrCode.save(Path.of(imagePath));
        // TODO IMPLEMENTAR CACHE
        String qrCodeString = qrCode.toString();
        return new LinhaDigitavel(qrCodeString, uuid);
    }
}
