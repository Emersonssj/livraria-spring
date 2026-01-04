package com.altislab.livraria.domain.dashboard;

// Records auxiliares (podem ser no mesmo arquivo se preferir)
public record DadosTotais(
        long totalLivros,
        long totalClientes,
        long totalEditoras
) {}
