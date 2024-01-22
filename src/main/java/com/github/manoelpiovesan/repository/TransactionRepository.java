package com.github.manoelpiovesan.repository;

import com.github.manoelpiovesan.entity.Chave;
import com.github.manoelpiovesan.entity.LinhaDigitavel;
import com.github.manoelpiovesan.entity.Transaction;
import com.github.manoelpiovesan.enums.StatusPix;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.Optional;

public interface TransactionRepository {
    void adicionar(final LinhaDigitavel linhaDigitavel, final BigDecimal valor, final Chave chave);
    Optional<Transaction> alterarStatusTransacao(final String uuid, final StatusPix statusPix);
    Optional<Document> findOne(final String uuid);


}
