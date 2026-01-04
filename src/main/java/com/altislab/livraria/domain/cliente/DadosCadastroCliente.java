package com.altislab.livraria.domain.cliente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroCliente(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String celular,
        String cpf, // Não tem @NotBlank pois é facultativo
        @NotNull @Valid DadosEndereco endereco
) {}