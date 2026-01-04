package com.altislab.livraria.repository;

import com.altislab.livraria.domain.livro.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    Page<Livro> findAllByAtivoTrue(Pageable paginacao);

    @Query("SELECT l FROM Livro l WHERE l.ativo = true AND " +
            "(:nome IS NULL OR l.nome LIKE %:nome%) AND " +
            "(:autor IS NULL OR l.autor LIKE %:autor%) AND " +
            "(:editora IS NULL OR l.editora.nome LIKE %:editora%)") // Ajuste aqui: .nome
    Page<Livro> pesquisar(String nome, String autor, String editora, Pageable paginacao);
}