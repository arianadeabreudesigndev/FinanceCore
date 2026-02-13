package com.financecore.domain.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade de domínio que representa um relatório financeiro consolidado.
 *
 * Não altera estado do domínio; apenas consolida dados.
 */
public class RelatorioFinanceiro {

    private final UUID id;
    private final LocalDate periodoInicio;
    private final LocalDate periodoFim;
    private final LocalDateTime dataGeracao;

    public RelatorioFinanceiro(UUID id,
                               LocalDate periodoInicio,
                               LocalDate periodoFim,
                               LocalDateTime dataGeracao) {
        this.id = Objects.requireNonNull(id, "id não pode ser nulo");
        this.periodoInicio = Objects.requireNonNull(periodoInicio, "periodoInicio não pode ser nulo");
        this.periodoFim = Objects.requireNonNull(periodoFim, "periodoFim não pode ser nulo");
        this.dataGeracao = Objects.requireNonNull(dataGeracao, "dataGeracao não pode ser nula");
        if (this.periodoFim.isBefore(this.periodoInicio)) {
            throw new IllegalArgumentException("periodoFim não pode ser anterior a periodoInicio");
        }
    }

    public static RelatorioFinanceiro novo(LocalDate periodoInicio, LocalDate periodoFim) {
        return new RelatorioFinanceiro(
                UUID.randomUUID(),
                periodoInicio,
                periodoFim,
                LocalDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getPeriodoInicio() {
        return periodoInicio;
    }

    public LocalDate getPeriodoFim() {
        return periodoFim;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }
}

