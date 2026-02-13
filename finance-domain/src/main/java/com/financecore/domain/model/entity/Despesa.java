package com.financecore.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.financecore.domain.model.exception.DespesaSemCategoriaException;
/**
 * Entidade de domínio que representa uma Despesa.
 *
 * Responsável por impactar negativamente o saldo mensal
 * e participar de análises e auditorias.
 */
public class Despesa {

    private final UUID id;
    private final UUID mesFinanceiroId;
    private Categoria categoria; // RN-04: deve ser uma categoria válida
    private final String descricao;
    private final BigDecimal valor;
    private final LocalDate data;
    private final TipoDespesa tipo;
    private final String metodoPagamento;

    public Despesa(UUID id,
                   UUID mesFinanceiroId,
                   Categoria categoria,
                   String descricao,
                   BigDecimal valor,
                   LocalDate data,
                   TipoDespesa tipo,
                   String metodoPagamento) {
        this.id = Objects.requireNonNull(id, "id não pode ser nulo");
        this.mesFinanceiroId = Objects.requireNonNull(mesFinanceiroId, "mesFinanceiroId não pode ser nulo");
        this.categoria = Objects.requireNonNull(categoria, "categoria não pode ser nula");
        this.descricao = validarDescricao(descricao);
        this.valor = validarValorPositivo(valor);
        this.data = Objects.requireNonNull(data, "data não pode ser nula");
        this.tipo = Objects.requireNonNull(tipo, "tipo não pode ser nulo");
        this.metodoPagamento = validarMetodoPagamento(metodoPagamento);
    }

    public static Despesa nova(UUID mesFinanceiroId,
                               Categoria categoria,
                               String descricao,
                               BigDecimal valor,
                               LocalDate data,
                               TipoDespesa tipo,
                               String metodoPagamento) {
        return new Despesa(
                UUID.randomUUID(),
                mesFinanceiroId,
                categoria,
                descricao,
                valor,
                data,
                tipo,
                metodoPagamento
        );
    }

    private static String validarDescricao(String descricao) {
        Objects.requireNonNull(descricao, "descricao não pode ser nula");
        if (descricao.isBlank()) {
            throw new IllegalArgumentException("descricao não pode ser vazia");
        }
        return descricao;
    }

    private static String validarMetodoPagamento(String metodoPagamento) {
        Objects.requireNonNull(metodoPagamento, "metodoPagamento não pode ser nulo");
        if (metodoPagamento.isBlank()) {
            throw new IllegalArgumentException("metodoPagamento não pode ser vazio");
        }
        return metodoPagamento;
    }

    private static BigDecimal validarValorPositivo(BigDecimal valor) {
        Objects.requireNonNull(valor, "valor não pode ser nulo");
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException("valor deve ser maior que zero");
        }
        return valor;
    }

    /**
     * Aplica RN-04: toda despesa deve possuir uma categoria válida.
     */
    public void classificar(Categoria novaCategoria) {
        if (novaCategoria == null) {
            throw new DespesaSemCategoriaException();
        }
        this.categoria = novaCategoria;
    }

    public UUID getId() {
        return id;
    }

    public UUID getMesFinanceiroId() {
        return mesFinanceiroId;
    }

    public Categoria getCategoria() {
        if (categoria == null) {
            throw new DespesaSemCategoriaException();
        }
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getData() {
        return data;
    }

    public TipoDespesa getTipo() {
        return tipo;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }
}

