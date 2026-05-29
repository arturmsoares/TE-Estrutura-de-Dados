# Atividade 2.2 - Detectando o Problema N+1 em Hibernate/JPA

**Instituto Federal do Triângulo Mineiro - Campus Uberlândia Centro**  
**Disciplina**: Aplicações em Sistemas Web com Java  
**Trilha 2**: Estruturas em Bancos de Dados  
**Tempo**: 15 minutos

## Objetivo

Compreender e detectar o problema **N+1 Query Problem** em operações com JPA/Hibernate e aprender a corrigi-lo usando **JOIN FETCH**.

## Conceitos-Chave

### O Problema N+1

Ocorre quando você executa:

```
1 query para buscar a lista principal
+ N queries para buscar cada dado relacionado
= N+1 queries no total
```

**Exemplo**:
```java
List<Transacao> transacoes = repository.findAll();  // Query 1

for (Transacao t : transacoes) {
    System.out.println(t.getUsuario().getNome());   // Queries 2 a N+1
    // Cada acesso ao usuario dispara uma query!
}
```

### Lazy Loading vs Eager Loading

| Tipo | Quando carrega | Problema |
|------|----------------|----------|
| **LAZY** (padrão) | Sob demanda (quando acessa) | N+1 problem ❌ |
| **EAGER** | Na mesma query inicial | Carrega dados desnecessários |
| **JOIN FETCH** | Na query inicial com JOIN | Otimizado ✅ |

## Solução: JOIN FETCH

```java
@Query("SELECT DISTINCT t FROM Transacao t " +
       "LEFT JOIN FETCH t.usuario " +
       "ORDER BY t.id")
List<Transacao> findAllComUsuarioJoinFetch();
```

**Resultado**: 1 query com JOIN que carrega tudo.

---

## Execução Prática

### 1. Popular o Banco

```bash
curl http://localhost:8080/api/atividade2-2/popular | jq
```

Resposta esperada:
```json
{
  "status": "OK",
  "usuarios": 5,
  "transacoes": 20,
  "mensagem": "Banco populado com sucesso"
}
```

### 2. Demonstrar o Problema N+1

```bash
curl http://localhost:8080/api/atividade2-2/problema-n-mais-1 | jq
```

**Observe os logs** (terminal onde rodou `java -jar`):

```sql
-- Veja as queries SQL:
Hibernate: SELECT ... FROM transacoes ...          -- Query 1 (lista)
Hibernate: SELECT ... FROM usuarios WHERE id = 1  -- Query 2 (usuário 1)
Hibernate: SELECT ... FROM usuarios WHERE id = 2  -- Query 3 (usuário 2)
...
Hibernate: SELECT ... FROM usuarios WHERE id = 5  -- Query 21 (usuário 5)

Total: 21 queries (1 + 20)
```

**Contagem de queries**:
```bash
# No terminal, grep as queries:
curl http://localhost:8080/api/atividade2-2/problema-n-mais-1 > /dev/null
# Contar as linhas com "Hibernate: SELECT"
```

### 3. Demonstrar a Solução

```bash
curl http://localhost:8080/api/atividade2-2/solucao-join-fetch | jq
```

**Observe os logs**:

```sql
-- Apenas UMA query com JOIN:
Hibernate: SELECT DISTINCT t.* FROM transacoes t 
           LEFT JOIN usuarios u ON t.usuario_id = u.id
           ORDER BY t.id

Total: 1 query
```

### 4. Comparar Performance

```bash
curl http://localhost:8080/api/atividade2-2/comparacao | jq
```

**Resposta esperada**:
```json
{
  "problema_n_mais_1": "Problema N+1",
  "tempo_problema_ms": 45.2,
  "solucao_join_fetch": "Solução com JOIN FETCH",
  "tempo_solucao_ms": 2.1,
  "speedup": "21.5x mais rápido"
}
```

---

## Análise para RESULTADOS.md

### Pergunta 1: Quantas queries foram executadas?

**Sem otimização (problema N+1)**:
```
Número de queries: 21 (1 + 20)
- 1 query para listar transações
- 20 queries para cada usuário (lazy loading)
```

**Com otimização (JOIN FETCH)**:
```
Número de queries: 1
- 1 query com LEFT JOIN que carrega tudo
```

### Pergunta 2: Qual é a diferença observada?

```
Com problema: 21 queries → ~45ms
Com solução: 1 query → ~2ms
Speedup: 21.5x mais rápido

Economizou 20 queries desnecessárias!
```

### Pergunta 3: Por que isso é um problema?

- **I/O**: Cada query = acesso a disco (lento)
- **Latência**: 20 pequenas queries << 1 grande query
- **Escalabilidade**: Com 1000 transações = 1001 queries!
- **Produção**: Pode derrubar a aplicação

### Pergunta 4: Alternativas para corrigir

| Técnica | Código | Quando usar |
|---------|--------|------------|
| **JOIN FETCH** | `@Query + LEFT JOIN FETCH` | JPQL queries |
| **@EntityGraph** | `@EntityGraph(attributePaths="usuario")` | Anotação simples |
| **Eager Loading** | `@ManyToOne(fetch = EAGER)` | Sempre carregar |

---

## Código da Solução

### Entidade com Lazy Loading (padrão)

```java
@Entity
public class Transacao {
    @ManyToOne(fetch = FetchType.LAZY)  // Lazy = problema N+1
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
```

### Repository com JOIN FETCH

```java
@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    
    @Query("SELECT DISTINCT t FROM Transacao t " +
           "LEFT JOIN FETCH t.usuario " +
           "ORDER BY t.id")
    List<Transacao> findAllComUsuarioJoinFetch();
}
```

### Alternativa com @EntityGraph

```java
@EntityGraph(attributePaths = "usuario")
@Query("SELECT t FROM Transacao t")
List<Transacao> findAllComUsuario();
```

---

## Resumo

| Aspecto | Problema N+1 | Solução |
|---------|-------------|--------|
| **Queries** | N+1 | 1 |
| **Tempo** | ~45ms | ~2ms |
| **Escalabilidade** | ❌ Péssima | ✅ Excelente |
| **Código** | Simples | Precisa de @Query |

**Regra de Ouro**: Se você acessa um relacionamento em um loop, use **JOIN FETCH** ou **@EntityGraph**.

---

## Próxima Atividade

**2.3** - Análise de Altura da Árvore B+ com fórmula logarítmica.

Tempo estimado: 15 minutos
