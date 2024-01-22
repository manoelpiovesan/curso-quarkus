package com.github.manoelpiovesan.domain;

import com.github.manoelpiovesan.entity.Chave;
import com.github.manoelpiovesan.entity.LinhaDigitavel;
import com.github.manoelpiovesan.entity.Transaction;
import com.github.manoelpiovesan.enums.StatusPix;
import com.github.manoelpiovesan.repository.TransacaoPixMongoClientRepository;
import com.github.manoelpiovesan.repository.TransactionPanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TransactionDomain {
    @Inject
    TransactionPanacheRepository repository;



    public void adicionarTransacao(final LinhaDigitavel linhaDigitavel, final BigDecimal valor, final Chave chave) {
        repository.adicionar(linhaDigitavel, valor, chave);
    }

    public List<Transaction> buscarTransacoes(final Date dataInicio, Date dataFim){
        return repository.buscarTransacoes(dataInicio, dataFim);
    }

    public Optional<Transaction> aprovarTransacao(final String uuid) {
        try {
            return repository.alterarStatusTransacao(uuid, StatusPix.APPROVED);
        } finally {
            //iniciarProcessamento(uuid);
        }
    }

    public Optional<Transaction> reprovarTransacao(final String uuid) {
        try {
            return repository.alterarStatusTransacao(uuid, StatusPix.REPROVED);
        } finally {
            // iniciarProcessamento(uuid);
        }
    }

    public Optional<Transaction> iniciarProcessamento(final String uuid) {
        return repository.alterarStatusTransacao(uuid, StatusPix.IN_PROCESS);
    }


    public Optional<Transaction> findById(final String uuid) {
        return repository.findOne(uuid);
    }




}
