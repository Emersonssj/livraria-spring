package com.altislab.livraria.domain.aluguel;

import com.altislab.livraria.repository.AluguelRepository;
import com.altislab.livraria.repository.ClienteRepository;
import com.altislab.livraria.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional // Garante que o Update do Livro e o Insert do Aluguel ocorram juntos
    public DadosDetalhamentoAluguel alugar(DadosCadastroAluguel dados) {
        // 1. Validar existências
        if (!livroRepository.existsById(dados.idLivro())) throw new RuntimeException("Livro não encontrado");
        if (!clienteRepository.existsById(dados.idCliente())) throw new RuntimeException("Cliente não encontrado");

        var livro = livroRepository.getReferenceById(dados.idLivro());

        // 2. Validar Estoque (Nova Lógica: Mais simples e direta)
        if (livro.getEstoque() <= 0) {
            throw new RuntimeException("Livro sem estoque disponível para aluguel no momento.");
        }

        // 3. ATUALIZAR ESTOQUE (AQUI ESTAVA FALTANDO)
        livro.baixarEstoque();
        // O JPA detecta a mudança no objeto 'livro' e faz o UPDATE automático no banco ao fim da transação

        // 4. Salvar Aluguel
        var cliente = clienteRepository.getReferenceById(dados.idCliente());
        var aluguel = new Aluguel(livro, cliente, LocalDate.now());
        aluguelRepository.save(aluguel);

        return new DadosDetalhamentoAluguel(aluguel);
    }

    @Transactional // Importante para salvar a devolução e o aumento de estoque
    public DadosDetalhamentoAluguel devolver(Long idAluguel) {
        var aluguel = aluguelRepository.findById(idAluguel)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));

        if (aluguel.getDataDevolucaoReal() != null) {
            throw new RuntimeException("Este aluguel já foi devolvido!");
        }

        // 1. Registrar data e multa
        aluguel.registrarDevolucao(LocalDate.now());

        // 2. REPOR ESTOQUE (AQUI ESTAVA FALTANDO)
        var livro = aluguel.getLivro();
        livro.reporEstoque();

        return new DadosDetalhamentoAluguel(aluguel);
    }
}