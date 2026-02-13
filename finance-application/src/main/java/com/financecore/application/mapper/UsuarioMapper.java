package com.financecore.application.mapper;

import com.financecore.application.dto.output.UsuarioOutputDTO;
import com.financecore.domain.model.entity.PreferenciasSistema;
import com.financecore.domain.model.entity.Usuario;

/**
 * Mapper para converter entre entidades de domínio e DTOs.
 * Mantém a separação entre camadas.
 */
public final class UsuarioMapper {
    
    private UsuarioMapper() {
        // Classe utilitária, não instanciável
    }
    
    public static UsuarioOutputDTO toOutputDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        PreferenciasSistema preferencias = usuario.getPreferencias();
        return new UsuarioOutputDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getDataCriacao(),
            preferencias.getTema().name(),
            preferencias.getIdioma(),
            preferencias.isNotificacoesAtivas()
        );
    }
}