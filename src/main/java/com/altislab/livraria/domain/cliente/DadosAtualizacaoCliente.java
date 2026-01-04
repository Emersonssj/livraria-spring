package com.altislab.livraria.domain.cliente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoCliente(
        @NotNull Long id,
        String nome,
        String email,
        String celular,
        String cpf,
        @Valid DadosEndereco endereco
) {}