package com.altislab.livraria.domain.livro;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record DadosAtualizacaoLivro(
        @NotNull Long id,
        String nome,
        String autor,
        Long idEditora, // Opcional para atualizar
        LocalDate dataLancamento,
        Integer estoque
) {}