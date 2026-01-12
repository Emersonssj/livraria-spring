package com.altislab.livraria.repository;

import com.altislab.livraria.domain.usuario.TokenRecuperacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRecuperacaoRepository extends JpaRepository<TokenRecuperacao, Long> {
    Optional<TokenRecuperacao> findByToken(String token);
    void deleteByToken(String token); // Para limpar após o uso
}