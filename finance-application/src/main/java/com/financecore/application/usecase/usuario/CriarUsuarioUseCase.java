package com.financecore.application.usecase.usuario;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financecore.domain.model.entity.PreferenciasSistema;
import com.financecore.domain.model.entity.TemaVisual;
import com.financecore.domain.model.entity.Usuario;
import com.financecore.domain.model.repository.UsuarioRepository;

/**
 * Caso de uso: Criar Usuário (UC-01, RF01).
 * 
 * Responsabilidades:
 * 1. Receber dados do usuário
 * 2. Criar entidades de domínio (Usuario e PreferenciasSistema)
 * 3. Persistir através do repositório
 * 4. Retornar resultado com ID do usuário criado
 * 
 * Alinhado com:
 * - UC-01: Cadastro de Usuário
 * - RF01: Cadastro de Usuário
 * - RN-11: Persistência de Preferências do Sistema
 */
@Service
public class CriarUsuarioUseCase {
    
    private final UsuarioRepository usuarioRepository;
    
    public CriarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    @Transactional
    public CriarUsuarioResult executar(CriarUsuarioCommand command) {
        // 1. Validar e criar PreferenciasSistema
        PreferenciasSistema preferencias = new PreferenciasSistema(
            command.tema(),
            command.idioma(),
            command.notificacoesAtivas()
        );
        
        // 2. Criar Usuário (usando factory method do domínio)
        Usuario usuario = Usuario.novo(command.nome(), preferencias);
        
        // 3. Persistir (o repositório deve salvar o usuário e suas preferencias)
        Usuario usuarioSalvo = usuarioRepository.salvar(usuario);
        
        // 4. Retornar resultado
        return new CriarUsuarioResult(usuarioSalvo.getId());
    }
}