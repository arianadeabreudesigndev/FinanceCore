package com.financecore.domain.model.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.financecore.domain.model.exception.DomainException;

/**
 * Aggregate Root: MêsFinanceiro.
 *
 * Núcleo do domínio, responsável por:
 * - agregar receitas, despesas e parcelas;
 * - controlar estado (ABERTO/FECHADO);
 * - garantir cálculo determinístico de saldo (RN-07);
 * - impedir alterações após fechamento (RN-02);
 * - associação obrigatória de dados financeiros ao mês (RN-03).
 */
public class MesFinanceiro {

    private final UUID id;
    private final UUID usuarioId;
    private final int mes;
    private final int ano;
    private MesFinanceiroStatus status;
    private final BigDecimal saldoInicial;
    private BigDecimal saldoFinal;

    private final List<Receita> receitas;
    private final List<Despesa> despesas;
    private final List<Parcela> parcelas;

    private MesFinanceiro(UUID id,
                          UUID usuarioId,
                          int mes,
                          int ano,
                          MesFinanceiroStatus status,
                          BigDecimal saldoInicial,
                          BigDecimal saldoFinal,
                          List<Receita> receitas,
                          List<Despesa> despesas,
                          List<Parcela> parcelas) {
        this.id = Objects.requireNonNull(id, "id não pode ser nulo");
        this.usuarioId = Objects.requireNonNull(usuarioId, "usuarioId não pode ser nulo");
        this.mes = validarMes(mes);
        this.ano = validarAno(ano);
        this.status = Objects.requireNonNull(status, "status não pode ser nulo");
        this.saldoInicial = validarValorPositivo(saldoInicial, "saldoInicial");
        this.saldoFinal = saldoFinal;
        this.receitas = new ArrayList<>(Objects.requireNonNullElseGet(receitas, ArrayList::new));
        this.despesas = new ArrayList<>(Objects.requireNonNullElseGet(despesas, ArrayList::new));
        this.parcelas = new ArrayList<>(Objects.requireNonNullElseGet(parcelas, ArrayList::new));
    }

    public static MesFinanceiro novo(UUID usuarioId, int mes, int ano, BigDecimal saldoInicial) {
        return new MesFinanceiro(
                UUID.randomUUID(),
                usuarioId,
                mes,
                ano,
                MesFinanceiroStatus.ABERTO,
                saldoInicial,
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    private static int validarMes(int mes) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("mes deve estar entre 1 e 12");
        }
        return mes;
    }

    private static int validarAno(int ano) {
        if (ano < 2000) {
            throw new IllegalArgumentException("ano deve ser >= 2000");
        }
        return ano;
    }

    private static BigDecimal validarValorPositivo(BigDecimal valor, String campo) {
        Objects.requireNonNull(valor, campo + " não pode ser nulo");
        if (valor.signum() < 0) {
            throw new IllegalArgumentException(campo + " não pode ser negativo");
        }
        return valor;
    }

    private void garantirAberto() {
        if (this.status == MesFinanceiroStatus.FECHADO) {
            throw new DomainException("Mês financeiro está FECHADO e não pode ser alterado.");
        }
    }

    public void registrarReceita(Receita receita) {
        Objects.requireNonNull(receita, "receita não pode ser nula");
        garantirAberto();
        this.receitas.add(receita);
    }

    public void registrarDespesa(Despesa despesa) {
        Objects.requireNonNull(despesa, "despesa não pode ser nula");
        garantirAberto();
        this.despesas.add(despesa);
    }

    public void registrarParcela(Parcela parcela) {
        Objects.requireNonNull(parcela, "parcela não pode ser nula");
        garantirAberto();
        this.parcelas.add(parcela);
    }

    /**
     * Aplica RN-07: saldo final é calculado de forma determinística
     * com base em receitas, despesas e parcelas associadas.
     *
     * SaldoFinal = SaldoInicial + TotalReceitas − TotalDespesas − TotalParcelas
     */
    public void fechar() {
        garantirAberto();

        BigDecimal totalReceitas = receitas.stream()
                .map(Receita::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDespesas = despesas.stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalParcelas = parcelas.stream()
                .map(Parcela::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.saldoFinal = saldoInicial
                .add(totalReceitas)
                .subtract(totalDespesas)
                .subtract(totalParcelas);

        this.status = MesFinanceiroStatus.FECHADO;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public MesFinanceiroStatus getStatus() {
        return status;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public List<Receita> getReceitas() {
        return List.copyOf(receitas);
    }

    public List<Despesa> getDespesas() {
        return List.copyOf(despesas);
    }

    public List<Parcela> getParcelas() {
        return List.copyOf(parcelas);
    }
}

