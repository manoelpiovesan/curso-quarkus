package com.github.manoelpiovesan.entity;

import com.github.manoelpiovesan.enums.TipoChave;
import com.github.manoelpiovesan.enums.TipoPessoa;

import java.time.LocalDateTime;

public record Chave(TipoChave tipoChave, String chave, String ispb, TipoPessoa tipoPessoa,
                    String cpfCnpj,
                    String nome,
                    LocalDateTime dataHoraCriacao
) {
}
