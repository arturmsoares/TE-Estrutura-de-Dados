package br.edu.iftm.estruturas.bd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para a entidade Transacao
 * Demonstra o uso de índices B+ em queries JPA
 */
@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    /**
     * Usa o índice idx_conta_id — percorre a árvore B+
     * Complexidade: O(log n)
     */
    List<Transacao> findByContaId(Long contaId);

    /**
     * Usa o índice idx_conta_data — range scan na árvore B+
     * Complexidade: O(log n + k) onde k é o número de resultados
     */
    @Query("SELECT t FROM Transacao t WHERE t.contaId = :id " +
           "AND t.dataTransacao BETWEEN :inicio AND :fim")
    List<Transacao> buscarPorContaEPeriodo(
        @Param("id") Long contaId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    /**
     * SEM índice em 'valor' — table scan completo
     * Complexidade: O(n) — percorre todas as linhas
     */
    List<Transacao> findByValorGreaterThan(BigDecimal valor);

    /**
     * Busca transações COM o problema N+1
     * Sem otimização: 1 query para transações + N queries para cada usuário
     * Complexidade: O(n+1) ❌ PROBLEMA
     */
    List<Transacao> findAll();

    /**
     * Busca transações CORRIGIDA com @EntityGraph
     * Usa LEFT JOIN para carregar usuários em 1 query
     * Complexidade: O(1) ✅ OTIMIZADA
     */
    @Query("SELECT DISTINCT t FROM Transacao t " +
           "LEFT JOIN FETCH t.usuario " +
           "ORDER BY t.id")
    List<Transacao> findAllComUsuarioJoinFetch();
}
