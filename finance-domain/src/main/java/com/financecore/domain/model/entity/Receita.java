package com.financecore.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade de domínio que representa uma Receita.
 *
 * Responsável por impactar positivamente o saldo mensal.
 */
public class Receita {

    private final UUID id;
    private final UUID mesFinanceiroId;
    private final String descricao;
    private final BigDecimal valor;
    private final TipoReceita tipo;
    private final LocalDate dataReferencia;

    public Receita(UUID id,
                   UUID mesFinanceiroId,
                   String descricao,
                   BigDecimal valor,
                   TipoReceita tipo,
                   LocalDate dataReferencia) {
        this.id = Objects.requireNonNull(id, "id não pode ser nulo");
        this.mesFinanceiroId = Objects.requireNonNull(mesFinanceiroId, "mesFinanceiroId não pode ser nulo");
        this.descricao = validarDescricao(descricao);
        this.valor = validarValorPositivo(valor);
        this.tipo = Objects.requireNonNull(tipo, "tipo não pode ser nulo");
        this.dataReferencia = Objects.requireNonNull(dataReferencia, "dataReferencia não pode ser nula");
    }

    public static Receita nova(UUID mesFinanceiroId,
                               String descricao,
                               BigDecimal valor,
                               TipoReceita tipo,
                               LocalDate dataReferencia) {
        return new Receita(
                UUID.randomUUID(),
                mesFinanceiroId,
                descricao,
                valor,
                tipo,
                dataReferencia
        );
    }

    private static String validarDescricao(String descricao) {
        Objects.requireNonNull(descricao, "descricao não pode ser nula");
        if (descricao.isBlank()) {
            throw new IllegalArgumentException("descricao não pode ser vazia");
        }
        return descricao;
    }

    private static BigDecimal validarValorPositivo(BigDecimal valor) {
        Objects.requireNonNull(valor, "valor não pode ser nulo");
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException("valor deve ser maior que zero");
        }
        return valor;
    }

    public UUID getId() {
        return id;
    }

    public UUID getMesFinanceiroId() {
        return mesFinanceiroId;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public TipoReceita getTipo() {
        return tipo;
    }

    public LocalDate getDataReferencia() {
        return dataReferencia;
    }
}

