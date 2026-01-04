package com.altislab.livraria.domain.aluguel;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosDetalhamentoAluguel(
        Long id,
        Long idLivro,
        String nomeLivro,
        Long idCliente,
        String nomeCliente,
        LocalDate dataAluguel,
        LocalDate dataPrevisao,
        LocalDate dataDevolucao,
        BigDecimal multa
) {
    public DadosDetalhamentoAluguel(Aluguel aluguel) {
        this(
                aluguel.getId(),
                aluguel.getLivro().getId(),
                aluguel.getLivro().getNome(),
                aluguel.getCliente().getId(),
                aluguel.getCliente().getNome(),
                aluguel.getDataAluguel(),
                aluguel.getDataPrevisaoDevolucao(),
                aluguel.getDataDevolucaoReal(),
                aluguel.getValorMulta()
        );
    }
}