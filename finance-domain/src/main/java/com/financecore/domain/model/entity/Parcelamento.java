// Arquivo: finance-domain/src/main/java/com/financecore/domain/model/entity/Parcelamento.java
package com.financecore.domain.model.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.financecore.domain.model.exception.DomainException;

/**
 * Aggregate Root: Parcelamento.
 *
 * Representa um compromisso financeiro distribuído no tempo (RN-06).
 */
public class Parcelamento {

    private final UUID id;
    private final UUID despesaId;
    private final BigDecimal valorTotal;
    private final int numeroParcelas;
    private final List<Parcela> parcelas;

    public Parcelamento(UUID id,
                        UUID despesaId,
                        BigDecimal valorTotal,
                        int numeroParcelas,
                        List<Parcela> parcelas) {
        this.id = Objects.requireNonNull(id, "id não pode ser nulo");
        this.despesaId = Objects.requireNonNull(despesaId, "despesaId não pode ser nulo");
        this.valorTotal = validarValorPositivo(valorTotal);
        this.numeroParcelas = validarNumeroParcelas(numeroParcelas);
        this.parcelas = new ArrayList<>(Objects.requireNonNullElseGet(parcelas, ArrayList::new));
    }

    public static Parcelamento novo(UUID despesaId,
                                    BigDecimal valorTotal,
                                    int numeroParcelas) {
        return new Parcelamento(
                UUID.randomUUID(),
                despesaId,
                valorTotal,
                numeroParcelas,
                new ArrayList<>()
        );
    }

    private static BigDecimal validarValorPositivo(BigDecimal valor) {
        Objects.requireNonNull(valor, "valor não pode ser nulo");
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException("valor deve ser maior que zero");
        }
        return valor;
    }

    private static int validarNumeroParcelas(int numeroParcelas) {
        if (numeroParcelas <= 1) {
            throw new IllegalArgumentException("numeroParcelas deve ser > 1");
        }
        return numeroParcelas;
    }

    /**
     * Garante invariantes do parcelamento:
     * - soma das parcelas deve ser igual ao valorTotal;
     * - quantidade de parcelas deve ser igual a numeroParcelas.
     */
    public void validarConsistencia() {
        if (parcelas.size() != numeroParcelas) {
            throw new DomainException("Quantidade de parcelas inválida para o parcelamento.");
        }
        BigDecimal soma = parcelas.stream()
                .map(Parcela::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (soma.compareTo(valorTotal) != 0) {
            throw new DomainException("Soma das parcelas diferente do valor total do parcelamento.");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getDespesaId() {
        return despesaId;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public int getNumeroParcelas() {
        return numeroParcelas;
    }

    public List<Parcela> getParcelas() {
        return List.copyOf(parcelas);
    }
}