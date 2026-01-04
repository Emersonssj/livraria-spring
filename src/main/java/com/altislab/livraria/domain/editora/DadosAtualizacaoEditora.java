package com.altislab.livraria.domain.editora;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoEditora(
        @NotNull Long id,
        String nome,
        String email,
        String site
) {}