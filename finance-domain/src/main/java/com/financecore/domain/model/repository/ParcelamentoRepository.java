package com.financecore.domain.model.repository;

import java.util.Optional;
import java.util.UUID;

import com.financecore.domain.model.entity.Parcelamento;

/**
 * Contrato de repositório do domínio para Parcelamento (Aggregate Root).
 * 
 * Segue o padrão DDD: repositórios são definidos apenas para agregados raiz.
 * Implementações ficam na camada de infraestrutura.
 */
public interface ParcelamentoRepository {

    /**
     * Salva ou atualiza um parcelamento.
     */
    Parcelamento salvar(Parcelamento parcelamento);

    /**
     * Busca um parcelamento pelo seu identificador.
     */
    Optional<Parcelamento> buscarPorId(UUID id);

    /**
     * Busca o parcelamento associado a uma despesa específica.
     * Uma despesa do tipo PARCELADA deve ter exatamente um parcelamento (RN-06).
     */
    Optional<Parcelamento> buscarPorDespesaId(UUID despesaId);
}