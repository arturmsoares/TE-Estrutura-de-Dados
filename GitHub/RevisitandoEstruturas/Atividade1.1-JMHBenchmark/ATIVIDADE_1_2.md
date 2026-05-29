# Atividade 1.2 - Adicionando ArrayList vs LinkedList

## 📝 O que foi adicionado

A classe `MapBenchmark.java` agora inclui benchmarks para comparar ArrayList vs LinkedList:

### Novos campos na classe:
```java
private ArrayList<Integer> arrayList;
private LinkedList<Integer> linkedList;
private static final int LIST_MIDDLE_INDEX = SIZE / 2; // 5.000
```

### Novos métodos de benchmark:
```java
@Benchmark
public Integer arrayListGet() {
    return arrayList.get(LIST_MIDDLE_INDEX);
}

@Benchmark
public Integer linkedListGet() {
    return linkedList.get(LIST_MIDDLE_INDEX);
}
```

## 🔄 Como as listas são inicializadas

No método `setup()`:
```java
arrayList = new ArrayList<>();
linkedList = new LinkedList<>();

for (int i = 0; i < SIZE; i++) {
    arrayList.add(i);
    linkedList.add(i);
}
```

- Ambas as listas contêm 10.000 inteiros (0 a 9.999)
- O benchmark mede o tempo para acessar o elemento no índice **5.000** (meio)

## 🎯 O que Esperar

### Antes de Executar - Faça uma Previsão

**Pergunta:** Qual será o tempo de cada operação?

1. **ArrayList.get(5000)**
   - Esperado: rápido (O(1))
   - Estimativa: ~10-50 ns/op

2. **LinkedList.get(5000)**
   - Esperado: MUITO lento (O(n))
   - Estimativa: ~50.000-100.000 ns/op (percorre 5.000 nós)

3. **Razão de Lentidão**
   - linkedListGet / arrayListGet ≈ **5.000x**
   - Muito maior que HashMap vs TreeMap (3-10x)

### Por que LinkedList é tão lento?

```java
// ArrayList.get(5000) — O(1)
return array[5000];  // acesso direto ao array

// LinkedList.get(5000) — O(n)
Node current = first;
for (int i = 0; i < 5000; i++) {
    current = current.next;  // percorre 5000 nós
}
return current.value;
```

Para acessar um elemento no meio de uma LinkedList, o JDK precisa:
1. Começar no início (ou fim)
2. Percorrer nó por nó
3. Para 10.000 nós, acesso ao meio = 5.000 derreferências de ponteiro

## 🚀 Como Executar

### Compilar
```bash
mvn clean package
```

### Executar todos os benchmarks (incluindo ArrayList vs LinkedList)
```bash
java -jar target/benchmarks.jar
```

### Executar apenas os testes de listas
```bash
java -jar target/benchmarks.jar ".*List"
```

### Executar apenas ArrayList
```bash
java -jar target/benchmarks.jar "arrayListGet"
```

## 📊 Analisando Resultados

Saída esperada:

```
Benchmark                      Mode  Cnt      Score      Error   Units
MapBenchmark.arrayListGet      avgt    5     25.234 ±    2.234   ns/op
MapBenchmark.linkedListGet     avgt    5  82456.789 ± 5234.567   ns/op
```

**Interpretação:**
- `arrayListGet`: ~25 ns/op (muito rápido)
- `linkedListGet`: ~82.000 ns/op (muito lento)
- **Razão**: 82.456 / 25 ≈ **3.300x** (próximo de 5.000x!)

## 📝 Registre seus Resultados

Abra [RESULTADOS.md](RESULTADOS.md) e preencha:

1. Valores obtidos para arrayListGet e linkedListGet
2. Calcule a razão (razão = linkedListGet / arrayListGet)
3. Compare com a previsão (esperava 5.000x?)
4. Analise por que a razão é exatamente essa

## 💡 Lições Aprendidas

### ✅ Use ArrayList quando:
- Precisa de acesso por índice frequente
- Operações get() são comuns
- Dados cabem na memória

### ❌ Evite LinkedList para:
- Acesso aleatório por índice
- Operações get() frequentes
- Grandes volumes de dados

### ✅ Use LinkedList quando:
- Muitas inserções/remoções no início ou meio
- Processamento puramente sequencial
- Poucas operações get()

## 🔗 Comparação com HashMap vs TreeMap

| Comparação | HashMap vs TreeMap | ArrayList vs LinkedList |
|------------|-------------------|----------------------|
| Diferença de desempenho | 3-10x | 100-5.000x |
| Complexidade pior | O(log n) | O(n) |
| Cenário crítico | Busca com chave | Acesso por índice no meio |
| Trade-off | Ordenação vs velocidade | Inserção no meio vs acesso rápido |

**Conclusão:** A diferença entre O(1) e O(n) é **muito maior** que entre O(1) e O(log n)!

## 📚 Referências

- [ArrayList JavaDoc](https://docs.oracle.com/javase/11/docs/api/java.base/java/util/ArrayList.html)
- [LinkedList JavaDoc](https://docs.oracle.com/javase/11/docs/api/java.base/java/util/LinkedList.html)
- [Java Collections Performance](https://docs.oracle.com/javase/tutorial/collections/index.html)
