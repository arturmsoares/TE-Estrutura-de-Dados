package br.edu.iftm.estruturas.bd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Controller para demonstração do problema N+1 e sua solução
 * 
 * Atividade 2.2 - Trilha 2: Detectando N+1 Query Problem
 */
@RestController
@RequestMapping("/api/atividade2-2")
public class Atividade22Controller {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * GET /api/atividade2-2/popular
     * Popula banco com usuarios e transações
     */
    @GetMapping("/popular")
    public ResponseEntity<?> popularBanco() {
        try {
            // Criar 5 usuários
            List<Usuario> usuarios = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                Usuario u = new Usuario(
                    "Usuario " + i,
                    "user" + i + "@example.com",
                    String.format("%011d", i * 1000000000L)
                );
                usuarios.add(u);
            }
            usuarioRepository.saveAll(usuarios);

            // Criar 20 transações associadas aos usuários
            List<Transacao> transacoes = new ArrayList<>();
            for (int i = 1; i <= 20; i++) {
                Usuario usuario = usuarios.get((i - 1) % 5);
                Transacao t = new Transacao(
                    (long)(i % 3 + 1),
                    LocalDateTime.now().minusHours(i),
                    BigDecimal.valueOf(50 + (i * 2.5)),
                    "Transação teste #" + i,
                    i % 2 == 0 ? "CRÉDITO" : "DÉBITO"
                );
                t.setUsuario(usuario);
                transacoes.add(t);
            }
            transacaoRepository.saveAll(transacoes);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "OK");
            response.put("usuarios", usuarios.size());
            response.put("transacoes", transacoes.size());
            response.put("mensagem", "Banco populado com sucesso");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }

    /**
     * GET /api/atividade2-2/problema-n-mais-1
     * 
     * ❌ DEMONSTRA O PROBLEMA N+1:
     * 1 query para listar transações
     * + N queries para buscar cada usuário (lazy loading)
     * = N+1 queries no total
     * 
     * Veja os logs: spring.jpa.show-sql=true
     */
    @GetMapping("/problema-n-mais-1")
    public ResponseEntity<?> demonstrarProblema() {
        long inicio = System.currentTimeMillis();
        
        // Query 1: Busca todas as transações
        List<Transacao> transacoes = transacaoRepository.findAll();
        
        // Aqui começa o problema: cada acesso a t.getUsuario() dispara uma query!
        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Transacao t : transacoes) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("transacao_id", t.getId());
            item.put("conta_id", t.getContaId());
            item.put("valor", t.getValor());
            // ⚠️ Esta linha dispara uma query para cada transação (N queries adicionais)
            item.put("usuario_nome", t.getUsuario().getNome());
            item.put("usuario_email", t.getUsuario().getEmail());
            resultado.add(item);
        }
        
        long duracao = System.currentTimeMillis() - inicio;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("endpoint", "/problema-n-mais-1");
        response.put("tipo", "❌ PROBLEMA N+1");
        response.put("explicacao", "1 query para transações + N queries para usuários = N+1");
        response.put("total_transacoes", resultado.size());
        response.put("tempo_ms", duracao);
        response.put("aviso", "Veja os logs para contar as queries SQL executadas!");
        response.put("transacoes", resultado);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/atividade2-2/solucao-join-fetch
     * 
     * ✅ SOLUÇÃO COM JOIN FETCH:
     * Uma única query com LEFT JOIN carrega transações + usuários
     * = 1 query total
     * 
     * Implementado em TransacaoRepository.findAllComUsuarioJoinFetch()
     */
    @GetMapping("/solucao-join-fetch")
    public ResponseEntity<?> demonstrarSolucao() {
        long inicio = System.currentTimeMillis();

        // Query ÚNICA com JOIN FETCH
        List<Transacao> transacoes = transacaoRepository.findAllComUsuarioJoinFetch();

        // Agora é seguro acessar usuario — já está carregado
        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Transacao t : transacoes) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("transacao_id", t.getId());
            item.put("conta_id", t.getContaId());
            item.put("valor", t.getValor());
            item.put("usuario_nome", t.getUsuario().getNome());
            item.put("usuario_email", t.getUsuario().getEmail());
            resultado.add(item);
        }

        long duracao = System.currentTimeMillis() - inicio;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("endpoint", "/solucao-join-fetch");
        response.put("tipo", "✅ OTIMIZADA COM JOIN FETCH");
        response.put("explicacao", "1 query com LEFT JOIN carrega tudo");
        response.put("total_transacoes", resultado.size());
        response.put("tempo_ms", duracao);
        response.put("vantagem", "Muito mais rápido! Veja os logs — apenas 1 query SQL");
        response.put("transacoes", resultado);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/atividade2-2/comparacao
     * Compara tempo de execução entre problema e solução
     */
    @GetMapping("/comparacao")
    public ResponseEntity<?> compararPerformance() {
        // Teste com problema N+1
        long inicio1 = System.nanoTime();
        List<Transacao> problematico = transacaoRepository.findAll();
        for (Transacao t : problematico) {
            @SuppressWarnings("unused")
            String nome = t.getUsuario().getNome(); // Dispara N queries
        }
        long tempo1 = System.nanoTime() - inicio1;

        // Teste com solução
        long inicio2 = System.nanoTime();
        List<Transacao> otimizado = transacaoRepository.findAllComUsuarioJoinFetch();
        for (Transacao t : otimizado) {
            @SuppressWarnings("unused")
            String nome = t.getUsuario().getNome(); // Sem queries adicionais
        }
        long tempo2 = System.nanoTime() - inicio2;

        double speedup = (double) tempo1 / tempo2;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("problema_n_mais_1", "Problema N+1");
        response.put("tempo_problema_ns", tempo1);
        response.put("tempo_problema_ms", tempo1 / 1_000_000.0);
        response.put("", "");
        response.put("solucao_join_fetch", "Solução com JOIN FETCH");
        response.put("tempo_solucao_ns", tempo2);
        response.put("tempo_solucao_ms", tempo2 / 1_000_000.0);
        response.put("speedup", String.format("%.1fx mais rápido", speedup));

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/atividade2-2/info
     * Retorna informações sobre a atividade
     */
    @GetMapping("/info")
    public ResponseEntity<?> obterInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        
        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("1_popular", "GET /api/atividade2-2/popular");
        endpoints.put("2_problema_n_mais_1", "GET /api/atividade2-2/problema-n-mais-1 (❌ Problema)");
        endpoints.put("3_solucao_join_fetch", "GET /api/atividade2-2/solucao-join-fetch (✅ Solução)");
        endpoints.put("4_comparacao", "GET /api/atividade2-2/comparacao");
        info.put("endpoints", endpoints);

        Map<String, String> instrucoes = new LinkedHashMap<>();
        instrucoes.put("passo_1", "GET /api/atividade2-2/popular");
        instrucoes.put("passo_2", "Abrir console com logs (terminal)");
        instrucoes.put("passo_3", "GET /api/atividade2-2/problema-n-mais-1");
        instrucoes.put("passo_4", "Contar quantas queries SQL apareceram nos logs");
        instrucoes.put("passo_5", "GET /api/atividade2-2/solucao-join-fetch");
        instrucoes.put("passo_6", "Contar novamente — veja que agora é apenas 1 query!");
        info.put("como_testar", instrucoes);

        Map<String, String> conceitos = new LinkedHashMap<>();
        conceitos.put("problema_n_mais_1", "1 query inicial + N queries para cada relacionamento = N+1");
        conceitos.put("lazy_loading", "Hibernate carrega dados relacionados sob demanda (causa N+1)");
        conceitos.put("join_fetch", "Carrega dados relacionados em 1 query usando LEFT JOIN");
        conceitos.put("entity_graph", "Alternativa: usar @EntityGraph para especificar relacionamentos");
        info.put("conceitos", conceitos);

        return ResponseEntity.ok(info);
    }
}
