package com.altislab.livraria.domain.editora;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroEditora(
        @NotBlank String nome,
        @Email String email,
        String site
) {}