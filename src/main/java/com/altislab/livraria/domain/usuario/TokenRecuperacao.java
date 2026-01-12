package com.altislab.livraria.domain.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "tokens_recuperacao")
@Entity(name = "TokenRecuperacao")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRecuperacao {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "usuario_id")
    private Usuario usuario;

    private LocalDateTime dataExpiracao;

    public TokenRecuperacao(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
        this.dataExpiracao = LocalDateTime.now().plusMinutes(30); // Token vale por 30 min
    }
}