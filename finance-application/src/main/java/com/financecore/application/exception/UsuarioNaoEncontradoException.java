package com.financecore.application.exception;

public class UsuarioNaoEncontradoException extends ApplicationException {
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }
    
    public static UsuarioNaoEncontradoException comId(String id) {
        return new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id);
    }
}