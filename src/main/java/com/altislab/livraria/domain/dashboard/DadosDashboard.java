package com.altislab.livraria.domain.dashboard;

import java.util.List;

public record DadosDashboard(
        DadosTotais totais,
        DadosGraficoAlugueis graficoAlugueis,
        List<DadosRankingLivros> rankingLivros
) {}

