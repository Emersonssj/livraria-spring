package com.altislab.livraria.controller;
import com.altislab.livraria.domain.aluguel.Aluguel;
import com.altislab.livraria.domain.aluguel.DadosDetalhamentoAluguel;
import com.altislab.livraria.repository.AluguelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @Autowired
    private AluguelRepository aluguelRepository;

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

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoAluguel>> listar(
            @PageableDefault(size = 10, sort = {"dataAluguel"}) Pageable paginacao,
            @RequestParam(required = false) boolean pendentes
    ) {
        Page<Aluguel> page;

        if (pendentes) {
            // Busca só os que NÃO foram devolvidos ainda
            page = aluguelRepository.findAllByDataDevolucaoRealIsNull(paginacao);
        } else {
            // Busca o histórico completo
            page = aluguelRepository.findAll(paginacao);
        }

        return ResponseEntity.ok(page.map(DadosDetalhamentoAluguel::new));
    }
}