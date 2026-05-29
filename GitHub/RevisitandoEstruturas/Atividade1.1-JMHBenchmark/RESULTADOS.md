# Atividade 1.1, 1.2 e 1.3 - Resultados do Benchmark

## 📊 Resultados Obtidos

### Atividade 1.1: HashMap vs TreeMap

| Métrica | Valor | Unidade |
|---------|-------|---------|
| **hashMapGet** | _______ | ns/op |
| hashMapGet (erro) | ±_______ | % |
| **treeMapGet** | _______ | ns/op |
| treeMapGet (erro) | ±_______ | % |
| **treeMapRange** | _______ | ns/op |
| treeMapRange (erro) | ±_______ | % |

### Atividade 1.2: ArrayList vs LinkedList

| Métrica | Valor | Unidade |
|---------|-------|---------|
| **arrayListGet** | _______ | ns/op |
| arrayListGet (erro) | ±_______ | % |
| **linkedListGet** | _______ | ns/op |
| linkedListGet (erro) | ±_______ | % |
| **Razão (linked/array)** | _______ | x mais lento |

#### Previsão ANTES de executar:
```
Qual diferença de desempenho você espera entre ArrayList e LinkedList?

Resposta: __________________________________________________________________
__________________________________________________________________________
```

#### Análise DEPOIS de executar:
```
A previsão estava correta? Explique o resultado obtido:

Resposta: __________________________________________________________________
__________________________________________________________________________
```

---

## 🔍 Análise de Resultados

### Atividade 1.1: HashMap vs TreeMap

#### 1. HashMap Get é realmente mais rápido?

**Observação:**
```
Esperado: hashMapGet < treeMapGet (acesso O(1) vs O(log n))
Obtido: ___________________________________________________
```

#### 2. A variância foi aceitável?

**Análise:**
```
Margem de erro esperada: < 10% para medição confiável
Margem obtida (hashMapGet): _______%
Margem obtida (treeMapGet): _______%
Resultado: ✓ Confiável / ✗ Instável
```

#### 3. TreeMap Range Scan

**Diferencial do TreeMap:**
```
O TreeMap oferece operações de range scan que HashMap não oferece.
Tempo obtido (treeMapRange): _______ ns/op
Este tempo incluiu a busca de 100 elementos (5000 a 5100).
```

---

### Atividade 1.2: ArrayList vs LinkedList

#### 1. ArrayList realmente é mais rápido?

**Observação:**
```
Esperado: arrayListGet << linkedListGet 
Razão esperada: ~5.000x (acesso O(1) vs O(n) no meio da lista)

Obtido: 
- arrayListGet: _______ ns/op
- linkedListGet: _______ ns/op
- Razão real: _______x
```

#### 2. Por que LinkedList é tão lento para get()?

**Análise:**
```
ArrayList.get(i) é O(1):
- Acesso direto ao array interno: array[índice]
- Tempo constante, independente do tamanho

LinkedList.get(i) é O(n):
- Percorre a lista desde o início até o índice desejado
- Para index=5000 com 10.000 elementos, percorre ~5000 nós
- Cada acesso a nó.next() é lento (derreferência de ponteiro)
```

#### 3. Trade-offs práticos

**Quando usar ArrayList?**
```
✓ Acesso por índice frequente
✓ Maior parte das operações é get()
✓ Dados cabem na memória
```

**Quando usar LinkedList?**
```
✓ Muitas inserções/remoções no meio ou início
✓ Poucas operações get()
✓ Processamento sequencial apenas
```

---

## 💡 Conclusões

### O que aprendemos com Atividade 1.1?

1. **HashMap é mais rápido para buscas simples**
   - Acesso O(1) vs O(log n)
   - Ideal para: dicionários, caches, mapas de configuração

2. **TreeMap é mais lento para buscas, mas oferece ordenação**
   - Mantém chaves ordenadas
   - Suporta range queries (subMap, headMap, tailMap)
   - Ideal para: ranking, histórico ordenado, busca por range

3. **Trade-off de desempenho vs funcionalidade**
   - Use HashMap quando precisa de velocidade máxima
   - Use TreeMap quando precisa de ordenação ou range queries

### O que aprendemos com Atividade 1.2?

1. **ArrayList é MUITO mais rápido para acesso por índice**
   - O(1) vs O(n) = diferença de **milhares de vezes** mais rápido
   - Para n=10.000, razão esperada é ~5.000x

2. **LinkedList é lento para get() porque precisa percorrer**
   - Cada .get(i) requer i derreferências de ponteiro
   - get(5000) em lista de 10.000 = 5000 passos sequenciais

3. **Escolha a estrutura para o seu caso de uso**
   - ArrayList: leitura rápida, inserção/remoção no final
   - LinkedList: inserção/remoção rápida, leitura lenta

---

## 📝 Checkpoint 1 - Respostas

### 1. Qual estrutura foi mais rápida no benchmark: HashMap ou TreeMap? Por quê?

**Resposta:**  
_________________________________________________________________  
_________________________________________________________________

### 2. Em qual cenário o TreeMap superaria o HashMap na prática?

**Resposta:**  
_________________________________________________________________  
_________________________________________________________________

### 3. O que aconteceria ao benchmark se você remover @Fork? Por quê isso é problemático?

**Resposta:**  
_________________________________________________________________  
_________________________________________________________________

### 4. Quais flags JVM você usaria para monitorar GC em produção?

**Resposta:**  
_________________________________________________________________  
_________________________________________________________________

---

## � Atividade 1.3: Interpretando GC Logs

### Pasos para executar:

**Windows:**
```bash
.\run-gc-test.bat
```

**Linux/Mac:**
```bash
chmod +x run-gc-test.sh
./run-gc-test.sh
```

### a) Qual o tempo médio entre coletas?

**Tempo médio:** _________ ms

**Análise:**
```
Se < 1000ms: GC muito frequente (heap pequeno) - ❌ Ruim
Se > 1000ms: GC adequado - ✅ Bom
```

**Diagnóstico:** _________________________________________________________________

### b) Qual a duração máxima de pausa?

**Pausa máxima:** _________ ms

**Análise:**
```
Se < 50ms:   ✅ Excelente (imperceptível)
Se 50-200ms: ⚠️  Aceitável (notável mas tolerável)
Se > 200ms:  ❌ Ruim (viola SLA de 200ms)
```

**Diagnóstico:** _________________________________________________________________

### c) As pausas afetariam uma API com SLA de 200ms?

**Resposta:** ✓ SIM / ✗ NÃO

**Justificativa:**

_________________________________________________________________
_________________________________________________________________

### d) Que ajuste de heap reduziria a frequência de GC?

**Ajuste recomendado:** _________________________________________________________

**Justificativa:**

_________________________________________________________________
_________________________________________________________________

**Comando sugerido:**
```bash
java -Xms512m -Xmx1g -Xlog:gc*:file=gc.log:time,uptime ...
```

---

## 🚀 Próximos Passos

- [ ] Atividade 1.3: Executar GC test e analisar gc.log
- [ ] Checkpoint 1: Responder todas as perguntas acima
- [ ] Trilha 2: Estruturas em Bancos de Dados
