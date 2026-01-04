package com.altislab.livraria.domain.livro;

import java.time.LocalDate;

public record DadosListagemLivro(
        Long id,
        String nome,
        String autor,
        DadosEditoraResumida editora, // Objeto aninhado para ficar bonito no JSON
        LocalDate dataLancamento,
        Integer estoque
) {
    public DadosListagemLivro(Livro livro) {
        this(
                livro.getId(),
                livro.getNome(),
                livro.getAutor(),
                new DadosEditoraResumida(livro.getEditora().getId(), livro.getEditora().getNome()),
                livro.getDataLancamento(),
                livro.getEstoque()
        );
    }
}

