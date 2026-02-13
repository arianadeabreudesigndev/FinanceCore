package com.financecore.application.usecase.usuario;

import java.util.UUID;

/**
 * Resultado do caso de uso CriarUsuario.
 * Contém o identificador do usuário criado.
 */
public record CriarUsuarioResult(
    UUID usuarioId
) {}