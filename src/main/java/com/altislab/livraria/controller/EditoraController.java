package com.altislab.livraria.controller;

import com.altislab.livraria.domain.editora.*;
import com.altislab.livraria.repository.EditoraRepository;
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
@RequestMapping("/editoras")
public class EditoraController {

    @Autowired
    private EditoraRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroEditora dados, UriComponentsBuilder uriBuilder) {
        var editora = new Editora(dados);
        repository.save(editora);

        var uri = uriBuilder.path("/editoras/{id}").buildAndExpand(editora.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemEditora(editora));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemEditora>> listar(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao,
            @RequestParam(required = false) String nome) {

        Page<Editora> page;
        if (nome == null) {
            page = repository.findAllByAtivoTrue(paginacao);
        } else {
            page = repository.findByAtivoTrueAndNomeContainingIgnoreCase(nome, paginacao);
        }

        return ResponseEntity.ok(page.map(DadosListagemEditora::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoEditora dados) {
        var editora = repository.getReferenceById(dados.id());
        editora.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosListagemEditora(editora));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var editora = repository.getReferenceById(id);

        // Em um cenário real, você verificaria aqui se existem Livros vinculados a esta editora
        // antes de permitir a exclusão.

        editora.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var editora = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosListagemEditora(editora));
    }
}