package com.financecore.application.service;

import org.springframework.stereotype.Service;

import com.financecore.application.dto.input.CriarUsuarioInputDTO;
import com.financecore.application.dto.output.UsuarioOutputDTO;
import com.financecore.application.mapper.UsuarioMapper;
import com.financecore.application.usecase.usuario.CriarUsuarioCommand;
import com.financecore.application.usecase.usuario.CriarUsuarioResult;
import com.financecore.application.usecase.usuario.CriarUsuarioUseCase;
import com.financecore.domain.model.repository.UsuarioRepository;

/**
 * Serviço de aplicação para orquestração de operações de usuário.
 * 
 * Responsabilidades:
 * 1. Converter DTOs de entrada em Commands
 * 2. Chamar casos de uso apropriados
 * 3. Converter resultados em DTOs de saída
 * 4. Tratar exceções de aplicação
 * 
 * Esta classe atua como uma fachada para a camada de interface.
 */
@Service
public class UsuarioApplicationService {
    
    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final UsuarioRepository usuarioRepository;
    
    public UsuarioApplicationService(
            CriarUsuarioUseCase criarUsuarioUseCase,
            UsuarioRepository usuarioRepository) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.usuarioRepository = usuarioRepository;
    }
    
    public UsuarioOutputDTO criarUsuario(CriarUsuarioInputDTO inputDTO) {
        // 1. Converter DTO em Command
        CriarUsuarioCommand command = new CriarUsuarioCommand(
            inputDTO.nome(),
            inputDTO.tema(),
            inputDTO.idioma(),
            inputDTO.notificacoesAtivas()
        );
        
        // 2. Executar caso de uso
        CriarUsuarioResult result = criarUsuarioUseCase.executar(command);
        
        // 3. Buscar usuário criado (para retornar dados completos)
        var usuario = usuarioRepository.buscarPorId(result.usuarioId())
            .orElseThrow(() -> new IllegalStateException("Usuário recém-criado não encontrado"));
        
        // 4. Converter para DTO de saída
        return UsuarioMapper.toOutputDTO(usuario);
    }
    
    // Outros métodos (buscarUsuario, atualizarPreferencias, etc.) serão adicionados futuramente
}