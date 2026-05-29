# Conceitos de Árvores B+ e Indexação em Bancos de Dados

**Referência**: Section 3.2.3 da Apostila - Estruturas de Dados em Bancos de Dados

---

## 1. Árvores B+ (B-Plus Trees)

### Definição

Uma **árvore B+** é uma estrutura de dados multi-nível balanceada otimizada para acesso a disco em SGBDs (Sistemas Gerenciadores de Bancos de Dados).

### Propriedades

1. **Balanceamento**: Todos os caminhos da raiz até folhas têm a mesma altura
2. **Multi-caminho**: Cada nó pode ter m filhos (branching factor)
3. **Eficiência**: Minimiza número de acessos a disco (I/O)
4. **Duplicação de chaves**: Folhas contêm todas as chaves; nós internos contêm cópias

---

## 2. Estrutura Física

### Exemplo: Índice em Coluna conta_id com m=100 (100 apontadores por nó)

```
                         [Raiz]
                    Chaves: 50 | 150 | 250
                    /         |         \
              [Nó Inter]  [Nó Inter]  [Nó Inter]
            Chaves:      Chaves:      Chaves:
            5|25|40      75|125|200   270|300|350
           /  |  |      /   |   |     /   |    |
        [L] [L] [L]   [L]  [L]  [L] [L]  [L]  [L]
        
        Folhas contêm dados reais: (chave, ponteiro para registro)
        Nós internos contêm apenas guias de busca
```

### Nó Folha (Leaf Node)

```
┌─────────────────────────────────────────┐
│ Chave    │ Ponteiro para Registro       │
├──────────┼──────────────────────────────┤
│ conta_id │ Endereço em disco            │
│ 5        │ → 0x1000                     │
│ 8        │ → 0x2000                     │
│ 12       │ → 0x3000                     │
│ ...      │ → ...                        │
│ Próxima folha ────→ (ligação sequencial)│
└─────────────────────────────────────────┘
```

### Nó Interno (Internal Node)

```
┌────────────────────────────────────────────┐
│ Chave_1  │ Chave_2  │ Chave_3  │ ...      │
├──────────┼──────────┼──────────┼──────────┤
│ Ptr_0    │ Ptr_1    │ Ptr_2    │ Ptr_3    │
│ (filhos) │ (filhos) │ (filhos) │ (filhos) │
└────────────────────────────────────────────┘

Invariante: Para cada chave K_i em nó interno:
  - Ptr_i aponta a registros com chave < K_i
  - Ptr_{i+1} aponta a registros com chave >= K_i
```

---

## 3. Cálculo de Altura

### Fórmula

$$h = \lceil \log_m(n) \rceil$$

Onde:
- **h** = altura da árvore
- **m** = branching factor (quantos apontadores por nó)
- **n** = número de registros

### Exemplos

#### Cenário 1: m=100, n=1,000,000

$$h = \lceil \log_{100}(1,000,000) \rceil = \lceil 3 \rceil = 3$$

**Significado**: Para encontrar um registro em 1 milhão, máximo de 3 acessos a disco

Cálculo detalhado:
- Nível 1 (raiz): 1 nó com 100 filhos
- Nível 2: até 100 nós, cada um com 100 filhos = 10.000 capacidade
- Nível 3 (folhas): até 10.000 nós, cada um com ~100 registros = 1.000.000 registros

#### Cenário 2: m=500, n=1,000,000

$$h = \lceil \log_{500}(1,000,000) \rceil = \lceil 2.43 \rceil = 3$$

**Significado**: Com branching factor maior, altura permanece baixa

#### Cenário 3: m=500, n=1,000,000,000 (1 bilhão)

$$h = \lceil \log_{500}(1,000,000,000) \rceil = \lceil 4.29 \rceil = 5$$

**Significado**: Mesmo com 1 bilhão de registros, apenas 5 acessos a disco!

---

## 4. Operações em Árvores B+

### Busca (Search)

```
1. Começar na raiz
2. Comparar chave com as chaves do nó
3. Seguir apontador apropriado
4. Repetir até atingir nó folha
5. Retornar dados

Complexidade: O(log_m n) = O(log n)
Acessos a disco: h = ceil(log_m n)
```

### Range Query (Busca por intervalo)

```
1. Buscar limite inferior (esquerda) usando busca binária
2. Seguir ligações sequenciais entre folhas até limite superior
3. Coletar todos os registros no intervalo

Complexidade: O(log_m n + k)
Onde k = número de registros no intervalo

Exemplo: SELECT * FROM transacoes WHERE conta_id BETWEEN 10 AND 50
```

### Inserção (Insert)

```
1. Localizar nó folha apropriado (busca)
2. Inserir chave em ordem
3. Se nó está cheio (overflow), dividir (split) em dois nós
4. Propagar mudanças para cima (cascata de splits)

Complexidade: O(log_m n) + custos de reorganização
```

### Deleção (Delete)

```
1. Localizar e remover chave
2. Se nó fica vazio ou mínimo, mesclar (merge) com vizinho
3. Propagar mudanças para cima

Complexidade: O(log_m n) + custos de reorganização
```

---

## 5. Comparação: INDEX SCAN vs TABLE SCAN

### Cenário: Banco com 1 Milhão de Registros

#### TABLE SCAN (Sem Índice)

```
Operação: SELECT * FROM transacoes WHERE conta_id = 5

Passos:
1. Ler página 0 (valor por página: 100 registros)
2. Verificar todos os 100 registros → nenhum match
3. Ler página 1 → nenhum match
4. ... (9.999 páginas)
5. Ler página 9999 → ENCONTRADO!

Total: 10.000 acessos a disco (1M registros / 100 por página)
Tempo: ~10.000 × 10ms = 100 segundos (MUITO LENTO)
```

#### INDEX SCAN (Com Índice B+)

```
Operação: SELECT * FROM transacoes WHERE conta_id = 5

Passos:
1. Acessar raiz da árvore B+ (disco)
2. Comparar conta_id=5 com chaves internas
3. Seguir apontador apropriado (disco)
4. Atingir folha contendo conta_id=5 (disco)
5. Acessar registro via ponteiro (disco)

Total: ~3-5 acessos a disco (altura da árvore)
Tempo: ~3 × 10ms = 30ms (1000x MAIS RÁPIDO!)
```

### Tabela Comparativa

| Aspecto | TABLE SCAN | INDEX SCAN |
|---------|-----------|-----------|
| **Acessos a disco** | 10.000 | 3-5 |
| **Tempo (aprox.)** | 100s | 30ms |
| **Speedup** | 1x | 3333x |
| **Complexidade** | O(n) | O(log n) |

---

## 6. Por que SGBDs Usam Branching Factor Alto (m grande)?

### Razão 1: Minimizar Altura

$$h = \lceil \log_m(n) \rceil$$

Para n = 1.000.000.000:
- m=2 (árvore binária): h ≈ 30 (30 acessos a disco!) 🔴
- m=100 (árvore B+): h ≈ 5 (5 acessos a disco) 🟢
- m=500 (árvore B+): h ≈ 4 (4 acessos a disco) 🟢

### Razão 2: Tamanho da Página = Tamanho do Nó

SGBDs organizações dados em **páginas** (típico: 4KB-8KB):

```
Página de disco = Um Nó B+

Se cada apontador tem 8 bytes e chave tem 8 bytes:
  Espaço por entrada: 16 bytes
  Página 4KB pode conter: 4096 / 16 ≈ 256 entradas
  Assim: m ≈ 256
```

### Razão 3: Balancear I/O com Processamento

| m | Altura | Acessos | Cálculos em memória |
|---|--------|---------|-------------------|
| 2 | 30 | 30 × 10ms = 300ms | Rápido |
| 100 | 5 | 5 × 10ms = 50ms | Rápido |
| 1000 | 3 | 3 × 10ms = 30ms | Maior |

**Ponto ótimo**: m=100-500 (H2, PostgreSQL, MySQL)

---

## 7. Aplicação em Spring JPA

### Criar Índice

```java
@Entity
@Table(indexes = {
    @Index(name = "idx_conta_id", columnList = "conta_id")
})
public class Transacao {
    @Column(name = "conta_id")
    private Long contaId;
    ...
}
```

### SQL Gerado pelo Hibernate

```sql
CREATE TABLE transacoes (
    id BIGINT PRIMARY KEY,
    conta_id BIGINT NOT NULL,
    valor DECIMAL(15,2),
    ...
);

CREATE INDEX idx_conta_id ON transacoes(conta_id);
```

### Query JPA

```java
// Usa índice idx_conta_id
List<Transacao> findByContaId(Long contaId);

// Gera: SELECT * FROM transacoes WHERE conta_id = ?
```

---

## 8. Referências Práticas

### H2 Console - Comandos Úteis

```sql
-- Ver plano de execução com índice
EXPLAIN SELECT * FROM transacoes WHERE conta_id = 5;

-- Ver plano sem índice
EXPLAIN SELECT * FROM transacoes WHERE valor > 100;

-- Listar índices
SELECT * FROM INFORMATION_SCHEMA.INDEXES;

-- Criar índice manualmente
CREATE INDEX idx_valor ON transacoes(valor);

-- Remover índice
DROP INDEX idx_valor;
```

### Análise de Performance

```bash
# Spring Boot logging
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG

# PostgreSQL (EXPLAIN ANALYZE)
EXPLAIN ANALYZE SELECT * FROM transacoes WHERE conta_id = 5;

# MySQL (EXPLAIN)
EXPLAIN SELECT * FROM transacoes WHERE conta_id = 5\G
```

---

## Resumo

| Conceito | Definição |
|----------|-----------|
| **Árvore B+** | Estrutura balanceada multi-nível para indexação em disco |
| **Altura** | h = ⌈log_m(n)⌉ — determina acessos a disco |
| **INDEX SCAN** | O(log n) — percorre árvore B+ até folha |
| **TABLE SCAN** | O(n) — percorre todas as páginas sequencialmente |
| **Branching Factor** | m típico = 100-500; maior m → menor altura |
| **Range Query** | O(log n + k) — busca + percorrer folhas ligadas |

---

**Próximo tópico**: N+1 Query Problem (Atividade 2.2)
