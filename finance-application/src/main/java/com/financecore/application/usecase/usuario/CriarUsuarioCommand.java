package com.financecore.application.usecase.usuario;

import com.financecore.domain.model.entity.TemaVisual;

/**
 * Command para o caso de uso CriarUsuario.
 * Contém todos os dados necessários para executar a operação.
 */
public record CriarUsuarioCommand(
    String nome,
    TemaVisual tema,
    String idioma,
    boolean notificacoesAtivas
) {
    public CriarUsuarioCommand {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (tema == null) {
            throw new IllegalArgumentException("Tema não pode ser nulo");
        }
        if (idioma == null || idioma.isBlank()) {
            throw new IllegalArgumentException("Idioma não pode ser vazio");
        }
    }
}