package com.altislab.livraria.domain.dashboard;

public record DadosRankingLivros(
        String nomeLivro,
        String autor,
        long quantidadeAlugueis
) {}
