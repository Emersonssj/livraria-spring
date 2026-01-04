package com.altislab.livraria.domain.aluguel;

import com.altislab.livraria.repository.AluguelRepository;
import com.altislab.livraria.repository.ClienteRepository;
import com.altislab.livraria.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    public DadosDetalhamentoAluguel alugar(DadosCadastroAluguel dados) {
        // 1. Validar se existem
        if (!livroRepository.existsById(dados.idLivro())) throw new RuntimeException("Livro não encontrado");
        if (!clienteRepository.existsById(dados.idCliente())) throw new RuntimeException("Cliente não encontrado");

        var livro = livroRepository.getReferenceById(dados.idLivro());

        // 2. Validar Estoque (Regra Crítica)
        Integer alugados = aluguelRepository.contarAlugueisAtivosPorLivro(dados.idLivro());
        Integer estoqueTotal = livro.getEstoque();

        if (alugados >= estoqueTotal) {
            throw new RuntimeException("Livro sem estoque disponível para aluguel no momento.");
        }

        // 3. Salvar
        var cliente = clienteRepository.getReferenceById(dados.idCliente());
        var aluguel = new Aluguel(livro, cliente, LocalDate.now());
        aluguelRepository.save(aluguel);

        return new DadosDetalhamentoAluguel(aluguel);
    }

    public DadosDetalhamentoAluguel devolver(Long idAluguel) {
        var aluguel = aluguelRepository.findById(idAluguel)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));

        if (aluguel.getDataDevolucaoReal() != null) {
            throw new RuntimeException("Este aluguel já foi devolvido!");
        }

        aluguel.registrarDevolucao(LocalDate.now());
        // O JPA detecta a mudança na entidade e salva automaticamente ao fim da transação

        return new DadosDetalhamentoAluguel(aluguel);
    }
}