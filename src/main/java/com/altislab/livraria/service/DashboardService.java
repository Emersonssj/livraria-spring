package com.altislab.livraria.service;

import com.altislab.livraria.domain.dashboard.*;
import com.altislab.livraria.repository.AluguelRepository;
import com.altislab.livraria.repository.ClienteRepository;
import com.altislab.livraria.repository.EditoraRepository;
import com.altislab.livraria.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired private LivroRepository livroRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private EditoraRepository editoraRepository;
    @Autowired private AluguelRepository aluguelRepository;

    public DadosDashboard carregarDados() {
        // 1. Buscar Totais Gerais
        var totalLivros = livroRepository.count();
        var totalClientes = clienteRepository.count();
        var totalEditoras = editoraRepository.count();
        var dadosTotais = new DadosTotais(totalLivros, totalClientes, totalEditoras);

        // 2. Buscar Dados para o Gráfico
        var pendentes = aluguelRepository.contarPendentes();
        var noPrazo = aluguelRepository.contarDevolvidosNoPrazo();
        var comAtraso = aluguelRepository.contarDevolvidosComAtraso();
        var dadosGrafico = new DadosGraficoAlugueis(pendentes, noPrazo, comAtraso);

        // 3. Buscar Ranking (Top 5 Livros)
        var top5 = aluguelRepository.encontrarLivrosMaisAlugados(PageRequest.of(0, 5));

        // Converter a projeção do banco para o DTO de resposta
        List<DadosRankingLivros> ranking = top5.stream()
                .map(item -> new DadosRankingLivros(
                        item.getLivro().getNome(),
                        item.getLivro().getAutor(),
                        item.getTotal()
                )).toList();

        return new DadosDashboard(dadosTotais, dadosGrafico, ranking);
    }
}