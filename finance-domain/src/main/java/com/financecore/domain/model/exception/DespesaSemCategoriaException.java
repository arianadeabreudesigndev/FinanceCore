package com.financecore.domain.model.exception;

/**
 * Exceção lançada quando uma despesa não possui categoria válida (RN-04).
 */
public class DespesaSemCategoriaException extends DomainException {

    public DespesaSemCategoriaException() {
        super("Despesa deve possuir uma categoria válida.");
    }
}

