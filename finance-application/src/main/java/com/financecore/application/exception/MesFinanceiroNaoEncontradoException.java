package com.financecore.application.exception;

public class MesFinanceiroNaoEncontradoException extends ApplicationException {
    public MesFinanceiroNaoEncontradoException(String message) {
        super(message);
    }
    
    public static MesFinanceiroNaoEncontradoException comId(String id) {
        return new MesFinanceiroNaoEncontradoException("Mês financeiro não encontrado com ID: " + id);
    }
}