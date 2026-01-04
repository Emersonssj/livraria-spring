package com.altislab.livraria.controller;

import com.altislab.livraria.domain.aluguel.AluguelService;
import com.altislab.livraria.domain.aluguel.DadosCadastroAluguel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    @Autowired
    private AluguelService service;

    @PostMapping
    @Transactional
    public ResponseEntity alugar(@RequestBody @Valid DadosCadastroAluguel dados) {
        var detalhamento = service.alugar(dados);
        return ResponseEntity.ok(detalhamento);
    }

    @PutMapping("/devolucao/{id}")
    @Transactional
    public ResponseEntity devolver(@PathVariable Long id) {
        var detalhamento = service.devolver(id);
        return ResponseEntity.ok(detalhamento);
    }
}