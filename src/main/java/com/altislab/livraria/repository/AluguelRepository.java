package com.altislab.livraria.repository;

import com.altislab.livraria.domain.aluguel.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {

    // Conta quantos aluguéis desse livro existem onde a Data de Devolução AINDA É NULA
    @Query("SELECT COUNT(a) FROM Aluguel a WHERE a.livro.id = :idLivro AND a.dataDevolucaoReal IS NULL")
    Integer contarAlugueisAtivosPorLivro(Long idLivro);

    // Verifica se existe aluguel ativo (útil para impedir exclusão de livro/cliente)
    boolean existsByLivroIdAndDataDevolucaoRealIsNull(Long idLivro);
    boolean existsByClienteIdAndDataDevolucaoRealIsNull(Long idCliente);
    // (Os métodos antigos continuam aqui...)

    // 1. Contar aluguéis pendentes (não devolvidos)
    @Query("SELECT COUNT(a) FROM Aluguel a WHERE a.dataDevolucaoReal IS NULL")
    Long contarPendentes();

    // 2. Contar devolvidos com atraso
    @Query("SELECT COUNT(a) FROM Aluguel a WHERE a.dataDevolucaoReal > a.dataPrevisaoDevolucao")
    Long contarDevolvidosComAtraso();

    // 3. Contar devolvidos no prazo
    @Query("SELECT COUNT(a) FROM Aluguel a WHERE a.dataDevolucaoReal <= a.dataPrevisaoDevolucao")
    Long contarDevolvidosNoPrazo();

    // 4. Ranking dos mais alugados
    // Agrupa por livro, conta as ocorrências e ordena do maior para o menor
    @Query("SELECT a.livro AS livro, COUNT(a) AS total FROM Aluguel a GROUP BY a.livro ORDER BY total DESC")
    List<LivroMaisAlugadoProjection> encontrarLivrosMaisAlugados(Pageable pageable);
}
