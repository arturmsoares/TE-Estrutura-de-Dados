# RESULTADOS.md - Atividade 2.1 (Trilha 2)

**Aluno**: ___________________________  
**Data**: ___________________________  
**Tempo Gasto**: ___________________________  

---

## Atividade 2.1 - Análise de Planos de Execução SQL com Índices B+

### Tarefa 1: Executar Query COM Índice

**Comando**:
```
GET http://localhost:8080/api/transacoes/buscar-com-indice?contaId=5
```

**Output da API**:
```
Resultados: _____ registros
Tempo: _____ ms
Tipo de plano: _____ (INDEX SCAN ou TABLE SCAN)
```

**EXPLAIN no H2 Console**:
```sql
EXPLAIN SELECT * FROM transacoes WHERE conta_id = 5;
```

**Resultado EXPLAIN**:
```
_____________________________________________________________________
_____________________________________________________________________
_____________________________________________________________________
```

**Tipo de operação**: ☐ INDEX SCAN   ☐ TABLE SCAN

---

### Tarefa 2: Executar Query SEM Índice

**Comando**:
```
GET http://localhost:8080/api/transacoes/buscar-sem-indice?valor=200
```

**Output da API**:
```
Resultados: _____ registros
Tempo: _____ ms
Tipo de plano: _____ (INDEX SCAN ou TABLE SCAN)
```

**EXPLAIN no H2 Console**:
```sql
EXPLAIN SELECT * FROM transacoes WHERE valor > 200;
```

**Resultado EXPLAIN**:
```
_____________________________________________________________________
_____________________________________________________________________
_____________________________________________________________________
```

**Tipo de operação**: ☐ INDEX SCAN   ☐ TABLE SCAN

---

### Tarefa 3: Comparação e Análise

**Pergunta 1**: Por que a query COM índice (`conta_id = 5`) é mais rápida?

Resposta:
```
_____________________________________________________________________
_____________________________________________________________________
_____________________________________________________________________
```

---

**Pergunta 2**: Qual é a complexidade teórica de cada operação?

```
Query com índice (conta_id):
  - Tipo de plano: _____________________
  - Complexidade: O(___)
  - Justificativa: _____________________

Query sem índice (valor > 200):
  - Tipo de plano: _____________________
  - Complexidade: O(___)
  - Justificativa: _____________________
```

---

**Pergunta 3**: Quantos registros foram analisados em cada query?

```
Com índice:
  - Registros analisados: _____
  - Registros retornados: _____
  - Taxa de seletividade: _____ %

Sem índice:
  - Registros analisados: _____
  - Registros retornados: _____
  - Taxa de seletividade: _____ %
```

---

**Pergunta 4**: Como você criaria um índice na coluna `valor` para otimizar a segunda query?

Resposta (código SQL ou JPA):
```
_____________________________________________________________________
_____________________________________________________________________
_____________________________________________________________________
```

---

### Tarefa 4: Conceitos de Árvores B+

**Pergunta 5**: Descreva a estrutura física de uma árvore B+ indexando a coluna `conta_id`:

```
Altura aproximada (com 100 registros):
  h = ceil(log_m(n)) = ceil(log_m(100))
  
  Assumindo m=100 (branching factor):
  h = _____

Nós da árvore:
  - Nó raiz contém: _____________________
  - Nós internos contêm: _____________________
  - Nós folha contêm: _____________________
```

---

**Pergunta 6**: Por que databases utilizam árvores B+ em vez de árvores binárias para índices?

Resposta:
```
_____________________________________________________________________
_____________________________________________________________________
_____________________________________________________________________
```

---

## Atividade 2.2 - Detectando o Problema N+1

### Tarefa 1: Executar Query COM Problema N+1

**Comando**:
```
GET http://localhost:8080/api/atividade2-2/problema-n-mais-1
```

**Número de queries executadas**: _______ 

**Esperado**: 1 (lista) + N (usuários) = _______ queries

**Tempo de execução**: _______ ms

---

### Tarefa 2: Executar Query CORRIGIDA com JOIN FETCH

**Comando**:
```
GET http://localhost:8080/api/atividade2-2/solucao-join-fetch
```

**Número de queries executadas**: _______ (esperado: 1)

**Tempo de execução**: _______ ms

---

### Tarefa 3: Comparação de Performance

Execute o endpoint de comparação:
```
GET http://localhost:8080/api/atividade2-2/comparacao
```

**Pergunta 1**: Qual é a diferença entre as duas abordagens?

```
Problema N+1:
  - Queries: _____
  - Tempo: _____ ms
  - Causa: Lazy loading dispara query para cada usuário

Solução JOIN FETCH:
  - Queries: _____
  - Tempo: _____ ms
  - Benefício: Uma única query com LEFT JOIN
```

---

**Pergunta 2**: Por que o JOIN FETCH é mais eficiente?

Resposta:
```
_____________________________________________________________________
_____________________________________________________________________
_____________________________________________________________________
```

---

**Pergunta 3**: Em qual cenário o problema N+1 seria CRÍTICO?

Resposta (cenário com 1.000.000 de transações):
```
Problema N+1: 1 + 1.000.000 = 1.000.001 queries
Impacto: _______________________________________

Solução JOIN FETCH: 1 query
Impacto: _______________________________________
```

---

**Pergunta 4**: Qual é a alternativa ao JOIN FETCH no Spring JPA?

Resposta:
```
@EntityGraph(attributePaths = "usuario")
@Query("SELECT t FROM Transacao t")
List<Transacao> findAllComUsuario();
```

Explique quando usar cada uma:
```
JOIN FETCH: ______________________________________________________
@EntityGraph: _____________________________________________________
Eager Loading: ____________________________________________________
```

---

## Checkpoint - Trilha 2.1 (Auto-Avaliação)

### Compreensão Conceitual

| Conceito | Entendi | Parcial | Não entendi |
|----------|---------|---------|------------|
| Índices B+ | ☐ | ☐ | ☐ |
| INDEX SCAN vs TABLE SCAN | ☐ | ☐ | ☐ |
| Complexidade O(log n) | ☐ | ☐ | ☐ |
| H2 Console e EXPLAIN | ☐ | ☐ | ☐ |
| Spring JPA com @Index | ☐ | ☐ | ☐ |
| Problema N+1 | ☐ | ☐ | ☐ |
| JOIN FETCH | ☐ | ☐ | ☐ |

### Dificuldades Encontradas

```
_____________________________________________________________________
_____________________________________________________________________
_____________________________________________________________________
```

### Dúvidas Pendentes

```
_____________________________________________________________________
_____________________________________________________________________
_____________________________________________________________________
```

---

**Próxima Atividade**: 2.3 - Análise de Altura da Árvore B+

Tempo estimado: 15 minutos
