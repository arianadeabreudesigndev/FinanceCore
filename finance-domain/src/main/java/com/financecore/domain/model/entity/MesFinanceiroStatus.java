package com.financecore.domain.model.entity;

/**
 * Status de um Mês Financeiro, conforme modelo de domínio.
 *
 * - ABERTO: aceita lançamentos e alterações.
 * - FECHADO: imutável; somente leitura e consolidação.
 */
public enum MesFinanceiroStatus {
    ABERTO,
    FECHADO
}

