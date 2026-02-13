package com.financecore.domain.model.exception;

/**
 * Exceção base para violações de regras de domínio.
 *
 * Não conhece infraestrutura, UI ou códigos HTTP.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}

