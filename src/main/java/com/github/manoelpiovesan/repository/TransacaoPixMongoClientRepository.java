package com.github.manoelpiovesan.repository;

import com.github.manoelpiovesan.domain.TransactionConverterApply;
import com.github.manoelpiovesan.entity.Chave;
import com.github.manoelpiovesan.entity.LinhaDigitavel;
import com.github.manoelpiovesan.entity.Transaction;
import com.github.manoelpiovesan.enums.StatusPix;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
public class TransacaoPixMongoClientRepository implements TransactionRepository{

    @Inject
    MongoClient mongoClient;

    @Override
    public void adicionar(LinhaDigitavel linhaDigitavel,
                          BigDecimal valor, Chave chave) {
        var document = new Document();

        document.append(TransactionConverterApply.ID, linhaDigitavel.uuid());
        document.append(TransactionConverterApply.VALOR, valor);
        document.append(TransactionConverterApply.TIPO_CHAVE, chave.tipoChave());
        document.append(TransactionConverterApply.CHAVE, chave.chave());
        document.append(TransactionConverterApply.LINHA, linhaDigitavel.linha());
        document.append(TransactionConverterApply.STATUS, StatusPix.CREATED);
        document.append(TransactionConverterApply.DATA, LocalDateTime.now(
                ZoneId.of("America/Sao_Paulo")));

        var response = getCollection().insertOne(document);
        System.out.println("Transação adicionada com sucesso");
        System.out.println(response);


    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("pix").getCollection("transacao_pix");
    }

    @Override
    public Optional<Transaction> alterarStatusTransacao(String uuid,
                                                        StatusPix statusPix) {
        Optional<Document> optionalDocument = findOne(uuid);
        if (optionalDocument.isPresent()){
            var document = optionalDocument.get();
            var opts = new FindOneAndReplaceOptions().upsert(false).returnDocument(
                    ReturnDocument.AFTER);

            document.merge(TransactionConverterApply.STATUS, statusPix, (oldValue, newValue) -> newValue);

            var replace = getCollection().findOneAndReplace(eq(TransactionConverterApply.ID, uuid), document, opts);

            assert replace != null;
            return Optional.of(TransactionConverterApply.apply(replace));

        }

        return Optional.empty();
    }

    @Override
    public Optional<Document> findOne(String uuid) {
        var filter = eq(TransactionConverterApply.ID, uuid);

        FindIterable<Document> documents = getCollection().find(filter);

        return StreamSupport.stream(documents.spliterator(), false)
                .findFirst();
    }

}
