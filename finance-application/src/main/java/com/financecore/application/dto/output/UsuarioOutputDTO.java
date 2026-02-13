package com.financecore.application.dto.output;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de saída para representação de usuário.
 * Vai da camada de aplicação para a camada de interface.
 */
public record UsuarioOutputDTO(
    UUID id,
    String nome,
    LocalDateTime dataCriacao,
    String tema,
    String idioma,
    boolean notificacoesAtivas
) {}