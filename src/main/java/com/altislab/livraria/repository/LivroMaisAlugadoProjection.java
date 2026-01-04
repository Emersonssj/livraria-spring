package com.altislab.livraria.repository;

import com.altislab.livraria.domain.livro.Livro;

public interface LivroMaisAlugadoProjection {
    Livro getLivro();
    Long getTotal();
}