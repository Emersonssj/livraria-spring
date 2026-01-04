package com.altislab.livraria.domain.editora;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "editoras")
@Entity(name = "Editora")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Editora {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String site;

    private Boolean ativo;

    public Editora(DadosCadastroEditora dados) {
        this.ativo = true;
        this.nome = dados.nome();
        this.email = dados.email();
        this.site = dados.site();
    }

    public void atualizarInformacoes(DadosAtualizacaoEditora dados) {
        if (dados.nome() != null) this.nome = dados.nome();
        if (dados.email() != null) this.email = dados.email();
        if (dados.site() != null) this.site = dados.site();
    }

    public void excluir() {
        this.ativo = false;
    }
}