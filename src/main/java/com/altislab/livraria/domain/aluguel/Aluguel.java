package com.altislab.livraria.domain.aluguel;

import com.altislab.livraria.domain.cliente.Cliente;
import com.altislab.livraria.domain.livro.Livro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Table(name = "alugueis")
@Entity(name = "Aluguel")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Aluguel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id")
    private Livro livro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private LocalDate dataAluguel;
    private LocalDate dataPrevisaoDevolucao;
    private LocalDate dataDevolucaoReal;

    private BigDecimal valorMulta; // Nulo se não tiver multa

    public Aluguel(Livro livro, Cliente cliente, LocalDate dataAluguel) {
        this.livro = livro;
        this.cliente = cliente;
        this.dataAluguel = dataAluguel;
        this.dataPrevisaoDevolucao = dataAluguel.plusDays(7); // Regra padrão: 7 dias de prazo
        this.valorMulta = BigDecimal.ZERO;
    }

    public void registrarDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucaoReal = dataDevolucao;

        // Lógica de Multa: R$ 2,00 por dia de atraso (Exemplo)
        if (dataDevolucao.isAfter(dataPrevisaoDevolucao)) {
            long diasAtraso = ChronoUnit.DAYS.between(dataPrevisaoDevolucao, dataDevolucao);
            this.valorMulta = new BigDecimal("2.00").multiply(new BigDecimal(diasAtraso));
        } else {
            this.valorMulta = BigDecimal.ZERO;
        }
    }
}