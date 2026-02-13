package com.financecore.domain.model.entity;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidade de domínio que representa uma categoria financeira.
 *
 * Utilizada para classificação de despesas (RN-04).
 */
public class Categoria {

    private final UUID id;
    private final String nome;
    private final boolean essencial;

    public Categoria(UUID id, String nome, boolean essencial) {
        this.id = Objects.requireNonNull(id, "id não pode ser nulo");
        this.nome = validarNome(nome);
        this.essencial = essencial;
    }

    public static Categoria nova(String nome, boolean essencial) {
        return new Categoria(UUID.randomUUID(), nome, essencial);
    }

    private static String validarNome(String nome) {
        Objects.requireNonNull(nome, "nome não pode ser nulo");
        if (nome.isBlank()) {
            throw new IllegalArgumentException("nome não pode ser vazio");
        }
        return nome;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public boolean isEssencial() {
        return essencial;
    }
}

