package com.altislab.livraria.domain.livro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

public record DadosCadastroLivro(
        @NotBlank String nome,
        @NotBlank String autor,
        @NotNull Long idEditora, // Mudou de String editora para ID
        @NotNull LocalDate dataLancamento,
        @NotNull @Min(0) Integer estoque
) {}