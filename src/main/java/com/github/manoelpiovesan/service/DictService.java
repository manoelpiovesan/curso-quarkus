package com.github.manoelpiovesan.service;

import com.github.manoelpiovesan.entity.Chave;
import com.github.manoelpiovesan.enums.TipoChave;
import com.github.manoelpiovesan.enums.TipoPessoa;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;

@ApplicationScoped
public class DictService {

    @ConfigProperty(name = "pix.ispb")
    private String ispb;
    @ConfigProperty(name = "pix.cnpj")
    private String cnpj;
    @ConfigProperty(name = "pix.nome")
    private String nome;
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
