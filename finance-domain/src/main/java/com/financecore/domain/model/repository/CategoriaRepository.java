package com.financecore.domain.model.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.financecore.domain.model.entity.Categoria;

/**
 * Contrato de repositório do domínio para Categoria (Aggregate Root).
 * 
 * Categorias são entidades de referência com identidade própria.
 * Implementações ficam na camada de infraestrutura.
 */
public interface CategoriaRepository {

    /**
     * Salva ou atualiza uma categoria.
     */
    Categoria salvar(Categoria categoria);

    /**
     * Busca uma categoria pelo seu identificador.
     */
    Optional<Categoria> buscarPorId(UUID id);

    /**
     * Busca uma categoria pelo nome (exato).
     * Útil para evitar duplicações e para busca durante classificação.
     */
    Optional<Categoria> buscarPorNome(String nome);

    /**
     * Lista todas as categorias cadastradas.
     * Útil para exibir opções ao usuário na UI.
     */
    List<Categoria> listarTodas();

    /**
     * Remove uma categoria.
     * Deve validar se não existem despesas associadas (RN-04).
     */
    void remover(Categoria categoria);
}