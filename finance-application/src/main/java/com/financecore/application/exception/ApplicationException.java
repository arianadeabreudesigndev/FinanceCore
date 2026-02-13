package com.financecore.application.exception;

/**
 * Exceção base para a camada de aplicação.
 * Representa falhas semânticas no fluxo de casos de uso.
 */
public abstract class ApplicationException extends RuntimeException {
    protected ApplicationException(String message) {
        super(message);
    }
    
    protected ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}