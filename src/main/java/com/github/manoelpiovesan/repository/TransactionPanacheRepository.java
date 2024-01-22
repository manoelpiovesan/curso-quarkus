package com.github.manoelpiovesan.repository;

import com.github.manoelpiovesan.domain.TransactionConverterApply;
import com.github.manoelpiovesan.entity.Chave;
import com.github.manoelpiovesan.entity.LinhaDigitavel;
import com.github.manoelpiovesan.entity.Transaction;
import com.github.manoelpiovesan.enums.StatusPix;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransactionPanacheRepository
        implements PanacheMongoRepository<Transaction> {

    public void adicionar(LinhaDigitavel linhaDigitavel,
                          BigDecimal valor, Chave chave) {
        var transaction = new Transaction();
        transaction.setId(linhaDigitavel.uuid());
        transaction.setValor(valor);
        transaction.setTipoChave(chave.tipoChave().toString());
        transaction.setChave(chave.chave());
        transaction.setLinha(linhaDigitavel.linha());
        transaction.setStatus(StatusPix.CREATED);
        transaction.setData(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));

        transaction.persist();


    }

    public Optional<Transaction> alterarStatusTransacao(String uuid,
                                                        StatusPix statusPix) {
        Optional<Transaction> optionalTransaction = findOne(uuid);

        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            transaction.setStatus(statusPix);
            transaction.update();
            return Optional.of(transaction);
        }
        return Optional.empty();

    }

    public Optional<Transaction> findOne(String uuid) {
        return find(TransactionConverterApply.ID, uuid).stream().findFirst();
    }

    public List<Transaction> buscarTransacoes(final Date dataInicio, final Date dataFim){
       return find("data >= ?1 and data <= ?2 and status", dataInicio, dataFim, StatusPix.APPROVED).stream().collect(
               Collectors.toList());

    }

}
