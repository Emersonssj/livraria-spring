package com.altislab.livraria.domain.cliente;

public record DadosListagemCliente(Long id, String nome, String email, String celular, String cpf) {
    public DadosListagemCliente(Cliente cliente) {
        this(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getCelular(), cliente.getCpf());
    }
}