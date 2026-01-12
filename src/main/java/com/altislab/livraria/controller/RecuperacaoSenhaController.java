package com.altislab.livraria.controller;

import com.altislab.livraria.domain.usuario.DadosRedefinicaoSenha;
import com.altislab.livraria.domain.usuario.DadosSolicitacaoRecuperacao;
import com.altislab.livraria.service.RecuperacaoSenhaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class RecuperacaoSenhaController {

    @Autowired
    private RecuperacaoSenhaService service;

    @PostMapping("/forgot-password")
    public ResponseEntity solicitarRecuperacao(@RequestBody @Valid DadosSolicitacaoRecuperacao dados) {
        service.solicitarRecuperacao(dados.email());
        return ResponseEntity.ok("Se o e-mail existir, um link foi enviado.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity redefinirSenha(@RequestBody @Valid DadosRedefinicaoSenha dados) {
        service.redefinirSenha(dados.token(), dados.novaSenha());
        return ResponseEntity.ok("Senha alterada com sucesso.");
    }
}