package com.financecore.domain.model.entity;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade de domínio que representa uma parcela individual de um parcelamento.
 */
public class Parcela {

    private final UUID id;
    private final UUID parcelamentoId;
    private final UUID mesFinanceiroId;
    private final int numero;
    private final BigDecimal valor;
    private ParcelaStatus status;

    public Parcela(UUID id,
                   UUID parcelamentoId,
                   UUID mesFinanceiroId,
                   int numero,
                   BigDecimal valor,
                   ParcelaStatus status) {
        this.id = Objects.requireNonNull(id, "id não pode ser nulo");
        this.parcelamentoId = Objects.requireNonNull(parcelamentoId, "parcelamentoId não pode ser nulo");
        this.mesFinanceiroId = Objects.requireNonNull(mesFinanceiroId, "mesFinanceiroId não pode ser nulo");
        this.numero = validarNumero(numero);
        this.valor = validarValorPositivo(valor);
        this.status = Objects.requireNonNull(status, "status não pode ser nulo");
    }

    public static Parcela nova(UUID parcelamentoId,
                               UUID mesFinanceiroId,
                               int numero,
                               BigDecimal valor) {
        return new Parcela(
                UUID.randomUUID(),
                parcelamentoId,
                mesFinanceiroId,
                numero,
                valor,
                ParcelaStatus.PENDENTE
        );
    }

    private static int validarNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("numero da parcela deve ser > 0");
        }
        return numero;
    }

    private static BigDecimal validarValorPositivo(BigDecimal valor) {
        Objects.requireNonNull(valor, "valor não pode ser nulo");
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException("valor deve ser maior que zero");
        }
        return valor;
    }

    public void marcarPaga() {
        this.status = ParcelaStatus.PAGA;
    }

    public UUID getId() {
        return id;
    }

    public UUID getParcelamentoId() {
        return parcelamentoId;
    }

    public UUID getMesFinanceiroId() {
        return mesFinanceiroId;
    }

    public int getNumero() {
        return numero;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public ParcelaStatus getStatus() {
        return status;
    }
}

