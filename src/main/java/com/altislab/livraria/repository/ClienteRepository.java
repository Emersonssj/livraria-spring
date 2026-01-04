package com.altislab.livraria.repository;

import com.altislab.livraria.domain.cliente.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Page<Cliente> findAllByAtivoTrue(Pageable paginacao);

    @Query("SELECT c FROM Cliente c WHERE c.ativo = true AND " +
            "(:nome IS NULL OR c.nome LIKE %:nome%) AND " +
            "(:email IS NULL OR c.email LIKE %:email%) AND " +
            "(:celular IS NULL OR c.celular LIKE %:celular%) AND " +
            "(:cpf IS NULL OR c.cpf LIKE %:cpf%)")
    Page<Cliente> pesquisar(String nome, String email, String celular, String cpf, Pageable paginacao);
}