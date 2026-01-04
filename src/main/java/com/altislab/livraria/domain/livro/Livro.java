package com.altislab.livraria.domain.livro;

import com.altislab.livraria.domain.editora.Editora; // Import novo
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "livros")
@Entity(name = "Livro")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Livro {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String autor;

    @ManyToOne // Relacionamento Muitos Livros para Uma Editora
    @JoinColumn(name = "editora_id") // Nome da coluna no banco (FK)
    private Editora editora;

    private LocalDate dataLancamento;
    private Integer estoque;
    private Boolean ativo;

    // Construtor alterado para receber o Objeto Editora
    public Livro(DadosCadastroLivro dados, Editora editora) {
        this.ativo = true;
        this.nome = dados.nome();
        this.autor = dados.autor();
        this.editora = editora; // Recebe o objeto carregado do banco
        this.dataLancamento = dados.dataLancamento();
        this.estoque = dados.estoque();
    }

    public void atualizarInformacoes(DadosAtualizacaoLivro dados, Editora novaEditora) {
        if (dados.nome() != null) this.nome = dados.nome();
        if (dados.autor() != null) this.autor = dados.autor();
        if (novaEditora != null) this.editora = novaEditora; // Atualiza se vier uma nova
        if (dados.dataLancamento() != null) this.dataLancamento = dados.dataLancamento();
        if (dados.estoque() != null) this.estoque = dados.estoque();
    }

    public void excluir() {
        this.ativo = false;
    }
}