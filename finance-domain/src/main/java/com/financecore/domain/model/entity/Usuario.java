package com.financecore.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.financecore.domain.model.exception.DomainException;

/**
 * Aggregate Root: Usuário.
 *
 * Responsável por:
 * - ser dono lógico dos dados financeiros;
 * - manter preferências do sistema;
 * - controlar o ciclo de vida de MesFinanceiro (RN-01, RN-10, RN-11).
 *
 * Camada de domínio: não conhece persistência nem frameworks.
 */
public class Usuario {

    private final UUID id;
    private final String nome;
    private final LocalDateTime dataCriacao;
    private final PreferenciasSistema preferencias;
    private final List<MesFinanceiro> mesesFinanceiros;

    public Usuario(UUID id,
                   String nome,
                   LocalDateTime dataCriacao,
                   PreferenciasSistema preferencias,
                   List<MesFinanceiro> mesesFinanceiros) {
        this.id = Objects.requireNonNull(id, "id não pode ser nulo");
        this.nome = validarNome(nome);
        this.dataCriacao = Objects.requireNonNull(dataCriacao, "dataCriacao não pode ser nula");
        this.preferencias = Objects.requireNonNull(preferencias, "preferencias não pode ser nula");
        this.mesesFinanceiros = new ArrayList<>(
                Objects.requireNonNullElseGet(mesesFinanceiros, ArrayList::new)
        );
    }

    public static Usuario novo(String nome, PreferenciasSistema preferencias) {
        return new Usuario(
                UUID.randomUUID(),
                nome,
                LocalDateTime.now(),
                preferencias,
                new ArrayList<>()
        );
    }

    private static String validarNome(String nome) {
        Objects.requireNonNull(nome, "nome não pode ser nulo");
        if (nome.isBlank()) {
            throw new IllegalArgumentException("nome não pode ser vazio");
        }
        return nome;
    }

    /**
     * Cria um novo Mês Financeiro para este usuário, garantindo RN-01:
     * no máximo um mês ABERTO por usuário para um mesmo mês/ano.
     */
    public MesFinanceiro criarMesFinanceiro(int mes, int ano, java.math.BigDecimal saldoInicial) {
        Objects.requireNonNull(saldoInicial, "saldoInicial não pode ser nulo");

        boolean existeAbertoMesmoPeriodo = mesesFinanceiros.stream()
                .anyMatch(m -> m.getMes() == mes
                        && m.getAno() == ano
                        && m.getStatus() == MesFinanceiroStatus.ABERTO);

        if (existeAbertoMesmoPeriodo) {
            throw new DomainException("Já existe mês financeiro ABERTO para " + mes + "/" + ano);
        }

        MesFinanceiro mesFinanceiro = MesFinanceiro.novo(this.id, mes, ano, saldoInicial);
        this.mesesFinanceiros.add(mesFinanceiro);
        return mesFinanceiro;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public PreferenciasSistema getPreferencias() {
        return preferencias;
    }

    public List<MesFinanceiro> getMesesFinanceiros() {
        return List.copyOf(mesesFinanceiros);
    }
}

