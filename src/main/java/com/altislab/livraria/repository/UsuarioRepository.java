package com.altislab.livraria.repository;

import com.altislab.livraria.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByLogin(String login);
    Usuario findByEmail(String email);
}