package com.github.manoelpiovesan.service;


import com.github.manoelpiovesan.config.LinhaDigitavelCache;
import com.github.manoelpiovesan.domain.TransactionDomain;
import com.github.manoelpiovesan.entity.Chave;
import com.github.manoelpiovesan.entity.LinhaDigitavel;
import com.github.manoelpiovesan.entity.Transaction;
import com.github.manoelpiovesan.entity.qrcode.DadosEnvio;
import com.github.manoelpiovesan.entity.qrcode.QrCode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PixService {

    @Inject
    TransactionDomain transactionDomain;

    @Inject
    LinhaDigitavelCache linhaDigitavelCache;

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
        var linhaDigitavel = new LinhaDigitavel(qrCodeString, uuid);
        salvarLinhaDigitavel(chave, valor, linhaDigitavel);

        return new LinhaDigitavel(qrCodeString, uuid);
    }
    private void salvarLinhaDigitavel(Chave chave, BigDecimal valor, LinhaDigitavel linhaDigitavel) {
        transactionDomain.adicionarTransacao(linhaDigitavel, valor, chave);
        linhaDigitavelCache.set(linhaDigitavel.uuid(), linhaDigitavel);
    }


    public Optional<Transaction> findById(final String uuid) {
        return transactionDomain.findById(uuid);
    }


    public Optional<Transaction> aprovarTransacao(final String uuid) {
        return transactionDomain.aprovarTransacao(uuid);
    }

    private void processarPix() {

    }
    public Optional<Transaction> reprovarTransacao(final String uuid) {
        return transactionDomain.reprovarTransacao(uuid);
    }

    public List<Transaction> buscarTransacoes(final Date dataInicio, Date dataFim) {
        return transactionDomain.buscarTransacoes(dataInicio, dataFim);
    }




}
