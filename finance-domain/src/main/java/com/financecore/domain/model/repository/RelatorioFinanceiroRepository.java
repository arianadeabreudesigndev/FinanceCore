package com.financecore.domain.model.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.financecore.domain.model.entity.RelatorioFinanceiro;

/**
 * Contrato de repositório do domínio para RelatorioFinanceiro (Aggregate Root).
 * 
 * Relatórios são agregados independentes que consomem dados consolidados.
 */
public interface RelatorioFinanceiroRepository {

    /**
     * Salva um relatório gerado.
     */
    RelatorioFinanceiro salvar(RelatorioFinanceiro relatorio);

    /**
     * Busca um relatório pelo seu identificador.
     */
    Optional<RelatorioFinanceiro> buscarPorId(UUID id);

    /**
     * Lista relatórios dentro de um período.
     */
    List<RelatorioFinanceiro> listarPorPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Lista relatórios de um usuário específico.
     */
    List<RelatorioFinanceiro> listarPorUsuarioId(UUID usuarioId);
}