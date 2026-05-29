# Atividade 2.1 - Estruturas em Bancos de Dados (Árvores B+)

**Instituto Federal do Triângulo Mineiro - Campus Uberlândia Centro**  
**Disciplina**: Aplicações em Sistemas Web com Java  
**Trilha 2**: Estruturas em Bancos de Dados  
**Tempo**: 15 minutos

## Objetivo

Entender como **índices B+** funcionam em bancos de dados relacionais e como eles impactam a performance de queries SQL através da análise de planos de execução.

## Conceitos-Chave

### Árvores B+ (B-Plus Trees)

Uma **árvore B+** é uma estrutura de dados balanceada usada para indexação em SGBDs:

- **Nós internos**: Contêm apenas chaves de busca
- **Folhas**: Contêm pares (chave, ponteiro para registro) e referências para próximas folhas
- **Busca**: O(log n) — reduz drasticamente comparado a table scan O(n)
- **Range scan**: Eficiente pois folhas são ligadas sequencialmente

### Planos de Execução SQL

O otimizador de queries decide entre:

| Tipo | Complexidade | Quando | Custos |
|------|-------------|--------|---------|
| **INDEX SCAN** | O(log n) | Quando há índice na coluna | Acessa árvore B+ |
| **TABLE SCAN** | O(n) | Sem índice ou grande proporção de dados | Lê todas as linhas |

## Projeto Spring Boot

### Estrutura

```
Atividade2.1-ArvoreBPlus/
├── pom.xml                          # Maven com Spring Boot, JPA, H2
├── src/main/java/br/edu/iftm/
│   └── estruturas/
│       ├── Atividade2Application.java   # Classe main
│       └── bd/
│           ├── Transacao.java       # Entidade com @Index
│           ├── TransacaoRepository.java # JPA Repository
│           └── TransacaoController.java # Endpoints
└── src/main/resources/
    └── application.properties        # Config H2, JPA, logging

```

### Configuração

**application.properties**:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true           # H2 Console em /h2-console
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true                # Log todas as queries
```

### Entidade Transacao

```java
@Entity
@Table(indexes = {
    @Index(name = "idx_conta_id", columnList = "conta_id"),
    @Index(name = "idx_conta_data", columnList = "conta_id, data_transacao")
})
public class Transacao {
    Long id;
    Long contaId;        // INDEXADO ✓
    LocalDateTime dataTransacao;
    BigDecimal valor;    // SEM índice ✗
    String descricao;
    String tipo;
}
```

## Execução

### 1. Compilar e Executar

```bash
cd Atividade2.1-ArvoreBPlus
mvn clean spring-boot:run
```

Saída esperada:
```
Started Atividade2Application in 2.3 seconds
Server running at http://localhost:8080
```

### 2. Popular o Banco de Dados

```bash
curl http://localhost:8080/api/transacoes/popular
```

Resposta:
```json
{
  "status": "OK",
  "mensagem": "100 transações inseridas com sucesso",
  "detalhes": "20 contas com 5 transações cada"
}
```

### 3. Query COM Índice (INDEX SCAN)

```bash
curl "http://localhost:8080/api/transacoes/buscar-com-indice?contaId=5"
```

Resposta (truncada):
```json
{
  "query": "SELECT * FROM transacoes WHERE conta_id = 5",
  "tipo_plano": "INDEX SCAN (usa índice idx_conta_id)",
  "complexidade": "O(log n) — percorre árvore B+",
  "resultados": 5,
  "tempo_ms": 3,
  "instrucao_h2": "EXPLAIN SELECT * FROM transacoes WHERE conta_id = 5",
  "transacoes": [...]
}
```

**Verificar Plano de Execução**:
1. Acessar H2 Console: `http://localhost:8080/h2-console`
2. JDBC URL: `jdbc:h2:mem:testdb`
3. Executar:
   ```sql
   EXPLAIN SELECT * FROM transacoes WHERE conta_id = 5;
   ```
4. **Resultado esperado**: Contém `INDEX SCAN` ou `INDEX LOOKUP`

### 4. Query SEM Índice (TABLE SCAN)

```bash
curl "http://localhost:8080/api/transacoes/buscar-sem-indice?valor=200"
```

Resposta (truncada):
```json
{
  "query": "SELECT * FROM transacoes WHERE valor > 200",
  "tipo_plano": "TABLE SCAN (sem índice)",
  "complexidade": "O(n) — percorre todas as linhas",
  "resultados": 25,
  "tempo_ms": 1,
  "instrucao_h2": "EXPLAIN SELECT * FROM transacoes WHERE valor > 200",
  "transacoes": [...]
}
```

**Verificar Plano de Execução**:
1. H2 Console: `http://localhost:8080/h2-console`
2. Executar:
   ```sql
   EXPLAIN SELECT * FROM transacoes WHERE valor > 200;
   ```
3. **Resultado esperado**: Contém `SCAN` (sem INDEX)

## Análise Comparativa

### Plano INDEX SCAN
```
com.h2database
Explain analyze SELECT * FROM transacoes WHERE conta_id = 5

RESULT
┌─────────────────────────────────┐
│ PLAN                            │
├─────────────────────────────────┤
│ SELECT * FROM transacoes WHERE  │
│ conta_id=5                      │
│                                 │
│ Index scan                      │  ← Usa árvore B+
│ IDX_CONTA_ID: conta_id = 5      │
│ (5 rows)                        │
└─────────────────────────────────┘

Tempo: ~0-3ms
Registros analisados: ~7 (altura da árvore log n)
Registros retornados: 5
```

### Plano TABLE SCAN
```
com.h2database
Explain analyze SELECT * FROM transacoes WHERE valor > 200

RESULT
┌─────────────────────────────────┐
│ PLAN                            │
├─────────────────────────────────┤
│ SELECT * FROM transacoes WHERE  │
│ valor > 200                     │
│                                 │
│ Table scan                      │  ← Percorre todas as linhas
│ TRANSACOES                      │
│ (100 rows examined)             │
└─────────────────────────────────┘

Tempo: ~1-5ms
Registros analisados: 100 (todas as linhas)
Registros retornados: 25
```

## Conclusões

### Por que INDEX SCAN é mais rápido?

| Aspecto | INDEX SCAN | TABLE SCAN |
|---------|-----------|-----------|
| **Complexidade** | O(log n) | O(n) |
| **Para n=100** | ~7 acessos | 100 acessos |
| **Para n=1M** | ~20 acessos | 1M acessos |
| **Escalabilidade** | Excelente | Degrada exponencialmente |

### Quando usar Índices?

✅ **USE índices em**:
- Colunas frequentemente usadas em WHERE
- Colunas de foreign keys
- Colunas usadas em JOINs
- Colunas de range queries

❌ **EVITE índices em**:
- Colunas com poucos valores distintos
- Colunas raramente consultadas
- Colunas que mudam frequentemente
- Índices desnecessários (custo de manutenção)

## Referências

- **H2 Console Documentation**: http://localhost:8080/h2-console (em execução)
- **Section 3.2.3 - Apostila**: Estruturas de Dados em Bancos de Dados
- **SQL EXPLAIN**: https://h2database.com/html/commands.html#explain

---

**Próxima Atividade**: 2.2 - Detectando N+1 Query Problem com Spring Data JPA
