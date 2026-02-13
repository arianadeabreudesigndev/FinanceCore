package com.financecore.domain.model.repository;

import java.util.Optional;
import java.util.UUID;

import com.financecore.domain.model.entity.Usuario;

/**
 * Contrato de repositório do domínio para Usuário.
 *
 * Implementações vivem na camada de infraestrutura.
 */
public interface UsuarioRepository {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(UUID id);
}

