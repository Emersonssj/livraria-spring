package com.altislab.livraria.domain.aluguel;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record DadosCadastroAluguel(
        @NotNull Long idLivro,
        @NotNull Long idCliente
) {}