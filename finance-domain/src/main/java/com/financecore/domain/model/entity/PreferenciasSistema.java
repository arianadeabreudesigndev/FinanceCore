package com.financecore.domain.model.entity;

import java.util.Objects;

/**
 * Entidade de domínio que representa as preferências de sistema de um usuário.
 *
 * Alinhado com o modelo de domínio e com RN-11.
 */
public class PreferenciasSistema {

    private final TemaVisual tema;
    private final String idioma;
    private final boolean notificacoesAtivas;

    public PreferenciasSistema(TemaVisual tema, String idioma, boolean notificacoesAtivas) {
        this.tema = Objects.requireNonNull(tema, "tema não pode ser nulo");
        this.idioma = validarIdioma(idioma);
        this.notificacoesAtivas = notificacoesAtivas;
    }

    private static String validarIdioma(String idioma) {
        Objects.requireNonNull(idioma, "idioma não pode ser nulo");
        if (idioma.isBlank()) {
            throw new IllegalArgumentException("idioma não pode ser vazio");
        }
        return idioma;
    }

    public TemaVisual getTema() {
        return tema;
    }

    public String getIdioma() {
        return idioma;
    }

    public boolean isNotificacoesAtivas() {
        return notificacoesAtivas;
    }
}

