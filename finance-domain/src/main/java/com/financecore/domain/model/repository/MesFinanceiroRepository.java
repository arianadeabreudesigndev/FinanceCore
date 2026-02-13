package com.financecore.domain.model.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.financecore.domain.model.entity.MesFinanceiro;

/**
 * Contrato de repositório do domínio para MêsFinanceiro.
 */
public interface MesFinanceiroRepository {

    MesFinanceiro salvar(MesFinanceiro mesFinanceiro);

    Optional<MesFinanceiro> buscarPorId(UUID id);

    List<MesFinanceiro> buscarPorUsuarioEStatus(UUID usuarioId, String status);
}

