# Exercício 1.2 - Rastreamento Manual: Encadeamento Separado

## Enunciado
Considere uma tabela hash com capacidade M = 7 usando encadeamento separado. A função hash é h(k) = k % 7. Execute manualmente as operações abaixo na ordem indicada, desenhando o estado da tabela após cada inserção.

## Sequência de Inserções
- inserir(10)
- inserir(22)
- inserir(31)
- inserir(4)
- inserir(15)
- inserir(28)
- inserir(17)

## Análise Detalhada

| Chave | h(k) = k % 7 | Bucket | Colisão? | Estado da Tabela |
|-------|--------------|--------|----------|------------------|
| 10 | 10 % 7 = 3 | 3 | Não | Bucket 3: [10] |
| 22 | 22 % 7 = 1 | 1 | Não | Bucket 1: [22], Bucket 3: [10] |
| 31 | 31 % 7 = 3 | 3 | Sim — encadeia com 10 | Bucket 1: [22], Bucket 3: [10→31] |
| 4 | 4 % 7 = 4 | 4 | Não | Bucket 1: [22], Bucket 3: [10→31], Bucket 4: [4] |
| 15 | 15 % 7 = 1 | 1 | Sim — encadeia com 22 | Bucket 1: [22→15], Bucket 3: [10→31], Bucket 4: [4] |
| 28 | 28 % 7 = 0 | 0 | Não | Bucket 0: [28], Bucket 1: [22→15], Bucket 3: [10→31], Bucket 4: [4] |
| 17 | 17 % 7 = 3 | 3 | Sim — encadeia com 10→31 | Bucket 0: [28], Bucket 1: [22→15], Bucket 3: [10→31→17], Bucket 4: [4] |

## Estado Final da Tabela

```
Bucket 0: [28]
Bucket 1: [22] → [15]
Bucket 2: (vazio)
Bucket 3: [10] → [31] → [17]
Bucket 4: [4]
Bucket 5: (vazio)
Bucket 6: (vazio)
```

## Respostas às Perguntas

### 1. Qual é o fator de carga λ = n/M após todas as inserções?

**Resposta: λ = 7/7 = 1.0**

O fator de carga é 1, o que significa que há em média 1 elemento por bucket. Isso está acima do limiar recomendado de 0.75 para encadeamento separado, indicando que haveria degradação de desempenho.

### 2. Qual é o comprimento da cadeia mais longa? Qual operação tem custo O(n) no pior caso quando isso acontece?

**Resposta:**
- **Comprimento da cadeia mais longa: 3 elementos** (Bucket 3: [10 → 31 → 17])
- **Operações com custo O(n):** `get()`, `put()` e `remove()`

Quando uma cadeia tem comprimento 3:
- **`get()` no pior caso:** O(3) = O(n) se procurar pelo 3º elemento
- **`put()` no pior caso:** O(3) = O(n) para verificar se chave existe ou para atualizar
- **`remove()` no pior caso:** O(3) = O(n) para localizar e remover

Se todas as 7 chaves caíssem no mesmo bucket, teríamos complexidade O(7) = O(n).

### 3. Se inseríssemos a chave 38, em qual bucket ela seria alocada? Haveria colisão?

**Resposta:**
- h(38) = 38 % 7 = 3
- **Sim, haveria colisão** com as chaves 10, 31 e 17 que já estão no bucket 3
- A chave 38 seria encadeada no final: [10 → 31 → 17 → 38]

### 4. A função h(k) = k % 7 distribui bem as chaves desta sequência? Proponha uma sequência de 7 chaves que cause a pior distribuição possível para essa função.

**Resposta:**

#### Avaliação da distribuição atual:
Para a sequência [10, 22, 31, 4, 15, 28, 17]:
- **Bucket 0:** 1 elemento (28)
- **Bucket 1:** 2 elementos (22, 15)
- **Bucket 3:** 3 elementos (10, 31, 17)
- **Bucket 4:** 1 elemento (4)
- **Buckets 2, 5, 6:** vazios

A distribuição é **razoável, mas não ótima** – existe clustering com concentração em bucket 3.

#### Pior caso possível:
Uma sequência que coloca TODAS as 7 chaves no mesmo bucket:

**Exemplo: [7, 14, 21, 28, 35, 42, 49]**

Análise:
- 7 % 7 = 0
- 14 % 7 = 0
- 21 % 7 = 0
- 28 % 7 = 0
- 35 % 7 = 0
- 42 % 7 = 0
- 49 % 7 = 0

**Resultado:** Todas as 7 chaves caem em Bucket 0, formando uma lista linear com 7 elementos.

**Impacto no desempenho:**
- Fator de carga: λ = 7/7 = 1.0
- Comprimento de cadeia: 7 (máximo possível)
- Complexidade de get/put/remove: O(7) = O(n) (pior caso linear!)
- Tabela hash degrada para comportamento de lista encadeada

**Lição:** Múltiplos de M causam distribuição péssima. A função h(k) = k % M falha completamente quando as chaves são múltiplas de M.

## Conclusão

Este exercício ilustra a importância de:
1. Função hash bem distribuída
2. Manutenção de fator de carga abaixo de 0.75
3. Rehashing quando λ excede o limiar
4. Detecção de clustering primário

---

**Complexidade de operações com encadeamento separado:**
- Caso médio (λ ≤ 0.75): O(1)
- Pior caso (todos em um bucket): O(n)
- Rehashing (quando necessário): O(n)
