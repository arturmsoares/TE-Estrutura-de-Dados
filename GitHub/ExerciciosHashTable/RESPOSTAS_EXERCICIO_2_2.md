# Exercício 2.2 - Double Hashing

## Enunciado
Utilize double hashing com as funções abaixo para inserir as chaves 76, 40, 48, 5, 55, 47 em uma tabela de capacidade M = 11.

### Funções de Sondagem
- h1(k) = k % 11
- h2(k) = 7 - (k % 7)    [passo secundário; 7 é primo < M]
- h(k, i) = (h1(k) + i * h2(k)) % 11

---

## Análise Detalhada de Cada Inserção

### Chave 76

```
h1(76) = 76 % 11 = 10
h2(76) = 7 - (76 % 7) = 7 - 6 = 1

Sonda i=0: (10 + 0*1) % 11 = 10 % 11 = 10 → LIVRE
```

**Resultado:** Insere em slot **10**

---

### Chave 40

```
h1(40) = 40 % 11 = 7
h2(40) = 7 - (40 % 7) = 7 - 5 = 2

Sonda i=0: (7 + 0*2) % 11 = 7 % 11 = 7 → LIVRE
```

**Resultado:** Insere em slot **7**

---

### Chave 48

```
h1(48) = 48 % 11 = 4
h2(48) = 7 - (48 % 7) = 7 - 6 = 1

Sonda i=0: (4 + 0*1) % 11 = 4 % 11 = 4 → LIVRE
```

**Resultado:** Insere em slot **4**

---

### Chave 5

```
h1(5) = 5 % 11 = 5
h2(5) = 7 - (5 % 7) = 7 - 5 = 2

Sonda i=0: (5 + 0*2) % 11 = 5 % 11 = 5 → LIVRE
```

**Resultado:** Insere em slot **5**

---

### Chave 55

```
h1(55) = 55 % 11 = 0
h2(55) = 7 - (55 % 7) = 7 - 6 = 1

Sonda i=0: (0 + 0*1) % 11 = 0 % 11 = 0 → LIVRE
```

**Resultado:** Insere em slot **0**

---

### Chave 47

```
h1(47) = 47 % 11 = 3
h2(47) = 7 - (47 % 7) = 7 - 5 = 2

Sonda i=0: (3 + 0*2) % 11 = 3 % 11 = 3 → LIVRE
```

**Resultado:** Insere em slot **3**

---

## Estado Final da Tabela

| Slot | Conteúdo | Chave Inserida |
|------|----------|---|
| 0 | 55 | Chave 55 |
| 1 | (vazio) | — |
| 2 | (vazio) | — |
| 3 | 47 | Chave 47 |
| 4 | 48 | Chave 48 |
| 5 | 5 | Chave 5 |
| 6 | (vazio) | — |
| 7 | 40 | Chave 40 |
| 8 | (vazio) | — |
| 9 | (vazio) | — |
| 10 | 76 | Chave 76 |

```
┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬────┐
│55 │   │   │47 │48 │ 5 │   │40 │   │   │76 │
└───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴────┘
  0   1   2   3   4   5   6   7   8   9  10
```

---

## Observação Importante

**NENHUMA COLISÃO ocorreu nesta sequência!**

Todas as 6 chaves encontraram seu slot livre na **primeira sonda (i=0)**, demonstrando que:
1. A função h1 distribuiu bem as chaves inicialmente
2. A capacidade era suficiente (λ = 6/11 ≈ 0.545)
3. Double hashing é muito eficaz mesmo em casos que linear/quadrática teriam colisões

---

## Double Hashing - Vantagens

### 1. Uso de Dois Hashes
- h1(k): hash primária
- h2(k): hash secundária para passo variável

### 2. Redução de Clustering
- **Sondagem Linear:** Todos usam passo +1
- **Sondagem Quadrática:** Todos usam mesma sequência de quadrados
- **Double Hashing:** Cada chave tem sequência **única** baseada em h2(k)

### 3. Exemplo de Diferença

Suponha colisão em h1(k) = 7 para duas chaves diferentes:

**Sondagem Linear:**
```
Chave A: 7 → 8 → 9 → 10 → ...
Chave B: 7 → 8 → 9 → 10 → ...  (mesma sequência!)
```

**Double Hashing:**
```
Chave A: h2(A)=2  → 7 → 9 → 0 → 2 → 4 → ...
Chave B: h2(B)=3  → 7 → 10 → 2 → 5 → 8 → ... (sequência diferente!)
```

---

## Condições para Double Hashing

1. **M deve ser primo** (ou h2(k) deve ser coprimo com M)
   - Garante que sequência de sondas eventualmente visita todos slots

2. **h2(k) deve ser diferente de 0**
   - Evita entrar em loop infinito
   - Em nosso caso: h2(k) = 7 - (k % 7) está entre 1 e 7, perfeito

3. **h2(k) e M devem ser coprimos**
   - gcd(h2(k), M) = 1 para M primo é garantido

---

## Complexidade de Double Hashing

### Caso Médio
- **Busca:** O(1) amortizado quando λ < 0.5
- **Inserção:** O(1) amortizado quando λ < 0.5
- **Remoção:** O(1) amortizado (com lazy deletion)

### Pior Caso
- **Tabela cheia:** O(M) = O(n)
- **Clustering secundário:** Significativamente menor que linear/quadrática

### Comparação

| Estratégia | Melhor Caso | Caso Médio | Pior Caso | Clustering |
|---|---|---|---|---|
| **Encadeamento** | O(1) | O(1+λ) | O(n) | Nenhum |
| **Linear** | O(1) | O(1/(1-λ)) | O(n) | Primário |
| **Quadrática** | O(1) | O(1/(1-λ)) | O(n) | Secundário |
| **Double** | O(1) | O(1/(1-λ)) | O(n) | Mínimo |

---

## Conclusão

Double hashing é **a melhor estratégia de endereçamento aberto** porque:
1. Elimina praticamente clustering primário
2. Reduz significantly clustering secundário
3. Cada chave tem sequência de sondagem única
4. Complexidade comparável a outras estratégias, mas prática bem superior

**Recomendação:** Use double hashing ao invés de linear/quadrática quando performance for crítica e não for possível usar encadeamento.
