package com.github.manoelpiovesan.service;

import com.github.manoelpiovesan.config.RedisCache;
import com.github.manoelpiovesan.entity.Chave;
import com.github.manoelpiovesan.enums.TipoChave;
import com.github.manoelpiovesan.enums.TipoPessoa;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.util.Objects;

@ApplicationScoped
public class DictService {

    @ConfigProperty(name = "pix.ispb")
    String ispb;
    @ConfigProperty(name = "pix.cnpj")
    String cnpj;
    @ConfigProperty(name = "pix.nome")
    String nome;
    @Inject
    RedisCache redisCache;

    public Chave  buscarDetalhesChave(String key) {
        var chave = buscarChaveNoCache(key);

        if(Objects.isNull(chave)) {
            var chaveFake = buscarChave(key);
            redisCache.set(key, chaveFake);
            return chaveFake;
        }

        return chave;
    }

    private Chave buscarChaveNoCache(String key) {
        var chave =  redisCache.get(key);
        Log.infof("Chave %s encontrada no cache", chave);
        return chave;
    }

    public Chave buscarChave(String chave) {
        return new Chave(TipoChave.EMAIL,
                chave,
                ispb,
                TipoPessoa.JURIDICA,
                cnpj,
                nome,
                LocalDateTime.now());
    }
}
