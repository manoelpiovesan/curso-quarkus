package com.github.manoelpiovesan.service;


import com.github.manoelpiovesan.entity.Chave;
import com.github.manoelpiovesan.entity.LinhaDigitavel;
import com.github.manoelpiovesan.entity.qrcode.DadosEnvio;
import com.github.manoelpiovesan.entity.qrcode.QrCode;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@ApplicationScoped
public class PixService {

    public static final String TEMP_FOLDER_PATH = System.getProperty("java.io.tmpdir");

    public BufferedInputStream gerarQrCode(final String uuid) throws IOException {

        // TODO(Manoel): Implement redis cache.
        var imagePath = TEMP_FOLDER_PATH + uuid + ".png";


        try{
            return new BufferedInputStream(new FileInputStream(imagePath));
        } finally {
            // TODO(Manoel): Fix this. Image cant be accessed because has another process using it.
            // Files.delete(Paths.get(imagePath));
        }

    }



    public LinhaDigitavel gerarLinhaDigitavel(final Chave chave, BigDecimal valor, String cidadeRemetente) {

        var qrCode = new QrCode(new DadosEnvio(chave, valor, cidadeRemetente));
        var uuid = UUID.randomUUID().toString();
        var imagePath = TEMP_FOLDER_PATH + uuid + ".png";
        System.out.println(imagePath);
        qrCode.save(Path.of(imagePath));

        // TODO(Manoel): Implement redis cache.

        String qrCodeString = qrCode.toString();
        return new LinhaDigitavel(qrCodeString, uuid);
    }
}
