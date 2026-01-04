package com.altislab.livraria.domain.editora;

public record DadosListagemEditora(Long id, String nome, String email, String site) {
    public DadosListagemEditora(Editora editora) {
        this(editora.getId(), editora.getNome(), editora.getEmail(), editora.getSite());
    }
}