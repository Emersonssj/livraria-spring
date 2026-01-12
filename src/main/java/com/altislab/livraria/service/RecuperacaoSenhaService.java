package com.altislab.livraria.service;

import com.altislab.livraria.domain.usuario.TokenRecuperacao;
import com.altislab.livraria.domain.usuario.Usuario;
import com.altislab.livraria.infra.email.EmailService;
import com.altislab.livraria.repository.TokenRecuperacaoRepository;
import com.altislab.livraria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RecuperacaoSenhaService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private TokenRecuperacaoRepository tokenRepository;
    @Autowired private EmailService emailService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Transactional
    public void solicitarRecuperacao(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            // Por segurança, não dizemos se o email existe ou não, apenas retornamos OK
            return;
        }

        // Gera token aleatório
        String token = UUID.randomUUID().toString();

        // Salva no banco
        TokenRecuperacao tokenRecuperacao = new TokenRecuperacao(token, usuario);
        tokenRepository.save(tokenRecuperacao);

        // Envia email
        emailService.enviarEmailRecuperacao(email, token);
    }

    @Transactional
    public void redefinirSenha(String token, String novaSenha) {
        TokenRecuperacao tokenRecuperacao = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (tokenRecuperacao.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado. Solicite uma nova recuperação.");
        }

        // Atualiza senha
        Usuario usuario = tokenRecuperacao.getUsuario();
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);

        // Queima o token (deleta para não usar de novo)
        tokenRepository.delete(tokenRecuperacao);
    }
}