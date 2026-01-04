package com.altislab.livraria.controller;

import com.altislab.livraria.domain.cliente.*;
import com.altislab.livraria.repository.AluguelRepository;
import com.altislab.livraria.repository.ClienteRepository;
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
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;
    @Autowired
    private AluguelRepository aluguelRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroCliente dados, UriComponentsBuilder uriBuilder) {
        var cliente = new Cliente(dados);
        repository.save(cliente);

        var uri = uriBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemCliente(cliente));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemCliente>> listar(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String celular,
            @RequestParam(required = false) String cpf) {

        Page<Cliente> page;
        if (nome == null && email == null && celular == null && cpf == null) {
            page = repository.findAllByAtivoTrue(paginacao);
        } else {
            page = repository.pesquisar(nome, email, celular, cpf, paginacao);
        }

        return ResponseEntity.ok(page.map(DadosListagemCliente::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoCliente dados) {
        var cliente = repository.getReferenceById(dados.id());
        cliente.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosListagemCliente(cliente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var cliente = repository.getReferenceById(id);

        if (aluguelRepository.existsByClienteIdAndDataDevolucaoRealIsNull(id)) {
            return ResponseEntity.badRequest().body("Não é possível excluir: Cliente possui devoluções pendentes.");
        }
        boolean possuiEmprestimoAtivo = false; // Placeholder

        if (possuiEmprestimoAtivo) {
            // Retornar um 409 Conflict ou 400 Bad Request é uma boa prática aqui
            return ResponseEntity.badRequest().build();
        }

        cliente.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var cliente = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosListagemCliente(cliente));
    }
}