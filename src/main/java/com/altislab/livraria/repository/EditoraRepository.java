package com.altislab.livraria.repository;

import com.altislab.livraria.domain.editora.Editora;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditoraRepository extends JpaRepository<Editora, Long> {

    Page<Editora> findAllByAtivoTrue(Pageable paginacao);

    // Método que o Spring Data cria automaticamente pela assinatura (Derived Query)
    // Busca editoras ativas cujo nome contenha o texto (ignorando maiúsculas/minúsculas)
    Page<Editora> findByAtivoTrueAndNomeContainingIgnoreCase(String nome, Pageable paginacao);
}