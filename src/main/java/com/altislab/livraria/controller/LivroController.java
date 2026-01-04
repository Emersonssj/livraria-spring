package com.altislab.livraria.controller;

import com.altislab.livraria.domain.livro.*;
import com.altislab.livraria.domain.editora.Editora; // Importar
import com.altislab.livraria.repository.AluguelRepository;
import com.altislab.livraria.repository.LivroRepository;
import com.altislab.livraria.repository.EditoraRepository; // Importar
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroRepository repository;
    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired // Injeção do repositório de editoras
    private EditoraRepository editoraRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroLivro dados, UriComponentsBuilder uriBuilder) {
        // 1. Busca a editora. Se não existir, lança erro (o Spring trata EntityNotFoundException se configurado, ou usamos RuntimeException)
        if (!editoraRepository.existsById(dados.idEditora())) {
            return ResponseEntity.badRequest().body("Editora não encontrada!");
        }
        var editora = editoraRepository.getReferenceById(dados.idEditora());

        // 2. Passa a editora para o construtor
        var livro = new Livro(dados, editora);
        repository.save(livro);

        var uri = uriBuilder.path("/livros/{id}").buildAndExpand(livro.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemLivro(livro));
    }

    // Listar continua igual (a query JPQL cuida do join)
    @GetMapping
    public ResponseEntity<Page<DadosListagemLivro>> listar(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String editora) { // Filtro por nome da editora continua funcionando

        Page<Livro> page;
        if (nome == null && autor == null && editora == null) {
            page = repository.findAllByAtivoTrue(paginacao);
        } else {
            page = repository.pesquisar(nome, autor, editora, paginacao);
        }

        return ResponseEntity.ok(page.map(DadosListagemLivro::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoLivro dados) {
        var livro = repository.getReferenceById(dados.id());

        Editora editora = null;
        if (dados.idEditora() != null) {
            editora = editoraRepository.getReferenceById(dados.idEditora());
        }

        // Validação de Estoque (Placeholder do Aluguel continua aqui)

        livro.atualizarInformacoes(dados, editora);
        return ResponseEntity.ok(new DadosListagemLivro(livro));
    }

    // Delete e Detalhar continuam praticamente iguais
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        if (aluguelRepository.existsByLivroIdAndDataDevolucaoRealIsNull(id)) {
            return ResponseEntity.badRequest().body("Não é possível excluir: Livro possui exemplares alugados.");
        }
        var livro = repository.getReferenceById(id);
        livro.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var livro = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosListagemLivro(livro));
    }
}