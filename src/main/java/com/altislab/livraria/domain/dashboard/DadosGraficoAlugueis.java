package com.altislab.livraria.domain.dashboard;

public record DadosGraficoAlugueis(
        long pendentes,
        long devolvidosNoPrazo,
        long devolvidosComAtraso
) {}
