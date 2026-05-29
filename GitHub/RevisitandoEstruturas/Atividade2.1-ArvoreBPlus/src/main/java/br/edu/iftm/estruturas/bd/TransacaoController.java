package br.edu.iftm.estruturas.bd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Controller para demonstração de planos de execução SQL
 * e análise de índices B+ (Atividade 2.1)
 */
@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoRepository repository;

    /**
     * GET /api/transacoes/popular
     * Popula o banco com dados de teste (100 transações em 20 contas)
     */
    @GetMapping("/popular")
    public ResponseEntity<?> popularBanco() {
        try {
            List<Transacao> transacoes = new ArrayList<>();
            
            // Cria 100 transações distribuídas em 20 contas
            for (int i = 1; i <= 100; i++) {
                long contaId = ((i - 1) % 20) + 1; // Alterna entre 20 contas
                BigDecimal valor = BigDecimal.valueOf(50 + (i * 2.5));
                LocalDateTime data = LocalDateTime.now().minusHours(i);
                String tipo = i % 2 == 0 ? "CRÉDITO" : "DÉBITO";
                
                Transacao t = new Transacao(
                    contaId,
                    data,
                    valor,
                    "Transação teste #" + i,
                    tipo
                );
                transacoes.add(t);
            }
            
            repository.saveAll(transacoes);
            
            Map<String, String> response = new LinkedHashMap<>();
            response.put("status", "OK");
            response.put("mensagem", "100 transações inseridas com sucesso");
            response.put("detalhes", "20 contas com 5 transações cada");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }

    /**
     * GET /api/transacoes/buscar-com-indice?contaId=5
     * 
     * DEMONSTRAÇÃO: Esta query USA o índice idx_conta_id
     * Executa: SELECT * FROM transacoes WHERE conta_id = 5
     * Plano: INDEX SCAN (árvore B+) → O(log n)
     * 
     * Acessar H2 Console: http://localhost:8080/h2-console
     * Executar: EXPLAIN SELECT * FROM transacoes WHERE conta_id = 5;
     */
    @GetMapping("/buscar-com-indice")
    public ResponseEntity<?> buscarComIndice(@RequestParam Long contaId) {
        long inicio = System.currentTimeMillis();
        List<Transacao> resultado = repository.findByContaId(contaId);
        long duracao = System.currentTimeMillis() - inicio;
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("query", "SELECT * FROM transacoes WHERE conta_id = " + contaId);
        response.put("tipo_plano", "INDEX SCAN (usa índice idx_conta_id)");
        response.put("complexidade", "O(log n) — percorre árvore B+");
        response.put("resultados", resultado.size());
        response.put("tempo_ms", duracao);
        response.put("instrucao_h2", "EXPLAIN SELECT * FROM transacoes WHERE conta_id = " + contaId);
        response.put("transacoes", resultado);
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/transacoes/buscar-sem-indice?valor=200
     * 
     * DEMONSTRAÇÃO: Esta query NÃO tem índice em 'valor'
     * Executa: SELECT * FROM transacoes WHERE valor > 200
     * Plano: TABLE SCAN → O(n)
     * 
     * Acessar H2 Console: http://localhost:8080/h2-console
     * Executar: EXPLAIN SELECT * FROM transacoes WHERE valor > 200;
     */
    @GetMapping("/buscar-sem-indice")
    public ResponseEntity<?> buscarSemIndice(@RequestParam BigDecimal valor) {
        long inicio = System.currentTimeMillis();
        List<Transacao> resultado = repository.findByValorGreaterThan(valor);
        long duracao = System.currentTimeMillis() - inicio;
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("query", "SELECT * FROM transacoes WHERE valor > " + valor);
        response.put("tipo_plano", "TABLE SCAN (sem índice)");
        response.put("complexidade", "O(n) — percorre todas as linhas");
        response.put("resultados", resultado.size());
        response.put("tempo_ms", duracao);
        response.put("instrucao_h2", "EXPLAIN SELECT * FROM transacoes WHERE valor > " + valor);
        response.put("transacoes", resultado);
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/transacoes/listar-tudo
     * Lista todas as transações
     */
    @GetMapping("/listar-tudo")
    public ResponseEntity<?> listarTudo() {
        return ResponseEntity.ok(repository.findAll());
    }

    /**
     * GET /api/transacoes/info
     * Retorna informações sobre as estruturas de dados
     */
    @GetMapping("/info")
    public ResponseEntity<?> obterInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("total_transacoes", repository.count());
        
        Map<String, String> indices = new LinkedHashMap<>();
        indices.put("idx_conta_id", "Árvore B+ na coluna conta_id — O(log n)");
        indices.put("idx_conta_data", "Árvore B+ composta — otimiza range scans");
        info.put("indices_criados", indices);
        
        Map<String, String> instrucoes = new LinkedHashMap<>();
        instrucoes.put("1_popular", "GET /api/transacoes/popular");
        instrucoes.put("2_com_indice", "GET /api/transacoes/buscar-com-indice?contaId=5");
        instrucoes.put("3_sem_indice", "GET /api/transacoes/buscar-sem-indice?valor=200");
        instrucoes.put("4_h2_console", "http://localhost:8080/h2-console");
        info.put("endpoints", instrucoes);
        
        return ResponseEntity.ok(info);
    }
}
