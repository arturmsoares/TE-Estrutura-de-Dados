# Exercício 2.1 - Rastreamento Manual: Sondagem Linear e Quadrática

## Enunciado
Considere uma tabela hash com endereçamento aberto, capacidade M = 11, e função hash base h(k) = k % 11. Trace a inserção das chaves abaixo para cada estratégia de sondagem.

## Sequência de Inserções
inserir(20), inserir(31), inserir(54), inserir(43), inserir(65), inserir(9)

---

## Parte A - Sondagem Linear: h(k, i) = (h(k) + i) % M

### Análise Detalhada

| Chave | h(k) | Sonda i=0 | Sonda i=1 | Sonda i=2 | Sonda i=3 | Slot Final |
|-------|------|-----------|-----------|-----------|-----------|-----------|
| 20 | 20%11=9 | 9 livre | — | — | — | 9 |
| 31 | 31%11=9 | 9 ocupado | 10 livre | — | — | 10 |
| 54 | 54%11=10 | 10 ocupado | 0 livre | — | — | 0 |
| 43 | 43%11=10 | 10 ocupado | 0 ocupado | 1 livre | — | 1 |
| 65 | 65%11=10 | 10 ocupado | 0 ocupado | 1 ocupado | 2 livre | 2 |
| 9 | 9%11=9 | 9 ocupado | 10 ocupado | 0 ocupado | 1 ocupado | 3 |

### Estado Final da Tabela (Sondagem Linear)

```
Slot 0: 54
Slot 1: 43
Slot 2: 65
Slot 3: 9
Slot 4: (vazio)
Slot 5: (vazio)
Slot 6: (vazio)
Slot 7: (vazio)
Slot 8: (vazio)
Slot 9: 20
Slot 10: 31
```

---

## Parte B - Sondagem Quadrática: h(k, i) = (h(k) + i²) % M

### Análise Detalhada

| Chave | h(k) | i=0 | i=1 (1²=1) | i=2 (2²=4) | i=3 (3²=9) | Slot Final |
|-------|------|-----|-----------|-----------|-----------|-----------|
| 20 | 9 | 9 livre | — | — | — | 9 |
| 31 | 9 | 9 ocupado | 10 livre | — | — | 10 |
| 54 | 10 | 10 ocupado | (10+1)%11=0 livre | — | — | 0 |
| 43 | 10 | 10 ocupado | (10+1)%11=0 ocupado | (10+4)%11=3 livre | — | 3 |
| 65 | 10 | 10 ocupado | (10+1)%11=0 ocupado | (10+4)%11=3 ocupado | (10+9)%11=8 livre | 8 |
| 9 | 9 | 9 ocupado | (9+1)%11=10 ocupado | (9+4)%11=2 livre | — | 2 |

### Estado Final da Tabela (Sondagem Quadrática)

```
Slot 0: 54
Slot 1: (vazio)
Slot 2: 9
Slot 3: 43
Slot 4: (vazio)
Slot 5: (vazio)
Slot 6: (vazio)
Slot 7: (vazio)
Slot 8: 65
Slot 9: 20
Slot 10: 31
```

---

## Respostas às Perguntas

### 5. Qual é o fenômeno que ocorre quando chaves com o mesmo h(k) inicial formam um bloco contíguo? Como a sondagem quadrática atenua esse problema?

**Resposta:**

#### Fenômeno: PRIMARY CLUSTERING (Clustering Primário)

Quando várias chaves hash para o mesmo valor inicial, elas formam um **bloco contíguo de ocupação** na tabela.

**Problema:**
- Chaves 20 e 31 hashizam para 9, causam colisão
- 54 é inserido em 0, aumentando o cluster
- 43 é inserido em 1, ampliando ainda mais o cluster
- Qualquer nova chave que hash para 9, 10 ou 0 terá que sondarse através do cluster inteiro

**Impacto:** À medida que o cluster cresce:
- Número de sondas aumenta exponencialmente
- Degenera para O(n) no pior caso
- Qualquer colisão dentro do cluster causa cascata de deslocamentos

#### Solução com Sondagem Quadrática

A sondagem **quadrática** usa: h(k, i) = (h(k) + i²) % M

**Como reduz clustering:**
- Passos: +1, +4, +9, +16, +25... (ao invés de +1, +2, +3, +4...)
- "Pula" mais longe, saindo do cluster
- Distribui melhor ao longo da tabela

**Exemplo prático:**
- Sondagem linear: 9→10→0→1→2→3→4→5... (sequencial)
- Sondagem quadrática: 9→10→(9+4)%11=2→(9+9)%11=7→... (pulando)

**Limitação:** Quadrática ainda sofre de **SECONDARY CLUSTERING** (agrupamentos menores), mas é significativamente melhor que linear.

### 6. Calcule o fator de carga após todas as inserções. O valor está dentro do limite recomendado para endereçamento aberto?

**Resposta:**

#### Cálculo
- Número de elementos: n = 6
- Capacidade: M = 11
- **Fator de carga: λ = n/M = 6/11 ≈ 0.545 (54.5%)**

#### Limites Recomendados para Endereçamento Aberto

| Fator de Carga | Classificação | Recomendação |
|---|---|---|
| λ < 0.50 | Excelente | Desempenho ótimo, sem rehashing necessário |
| 0.50 ≤ λ < 0.70 | Bom | Performance aceitável - NOSSO CASO |
| 0.70 ≤ λ < 0.85 | Risco moderado | Clustering perceptível, considerar rehashing |
| λ ≥ 0.85 | Alto risco | Rehashing obrigatório |

#### Conclusão
**SIM, λ ≈ 0.545 está dentro do limite recomendado.**

Está na zona "verde" (0.50 ≤ λ < 0.70), indicando:
- Performance média O(1) para operações
- Clustering ainda controlado
- Não há urgência de rehashing

#### Comparação Linear vs Quadrática

Em nossa sequência:
- **Linear:** Cluster primário em slots 0-3, menos eficiente
- **Quadrática:** Distribuição mais uniforme (0, 3, 8, 9, 10, 2), melhor performance

---

## Conceitos-Chave

### Endereçamento Aberto
- Cada slot armazena um elemento ou está vazio
- Colisões resolvidas por sondagem (busca de próximo slot livre)

### Sondagem Linear
- h(k, i) = (h(k) + i) % M
- Simples, mas sofre de clustering primário
- Degradação rápida com fator de carga alto

### Sondagem Quadrática
- h(k, i) = (h(k) + i²) % M
- Melhor distribuição, reduz clustering primário
- Pode não encontrar slot se tabela está cheia (necessário que M seja primo)

### Fator de Carga (λ)
- Razão entre elementos inseridos e tamanho da tabela
- Determina performance média das operações
- Mais crítico em endereçamento aberto que em encadeamento

---

## Visualização Gráfica

### Sondagem Linear
```
Sequência de sondas: 9 → 10 → 0 → 1 → 2 → 3
                           ↓    ↓   ↓   ↓   ↓   ↓
                           9   10   0   1   2   3
                          [20] [31] [54] [43] [65] [9]
                      
Cluster contíguo: slots 0-3 e 9-10 formam blocos grandes
```

### Sondagem Quadrática  
```
Passos: 9 → 10 → 2 → 3 → 8 → 2
        ↓    ↓   ↓   ↓   ↓   ↓
        9   10   2   3   8   2
       [20] [31] [9] [43] [65] [54]

Distribuição mais espalhada, melhor uso de espaço
```

---

**Conclusão:** O exercício demonstra que escolher uma boa estratégia de sondagem é crucial para minimizar clustering e manter performance O(1) em hash tables com endereçamento aberto.
