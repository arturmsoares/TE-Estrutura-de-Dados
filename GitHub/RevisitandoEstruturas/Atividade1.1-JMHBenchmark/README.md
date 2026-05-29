# Atividade 1.1, 1.2 e 1.3 - Benchmarks e Análise de GC

## 📋 Descrição

Projeto completo para a **Trilha 1 - Benchmarking com JMH** com as 3 atividades:

- **Atividade 1.1**: Benchmark HashMap vs TreeMap
- **Atividade 1.2**: Benchmark ArrayList vs LinkedList
- **Atividade 1.3**: Análise de GC Logs

### Tecnologias
- Maven
- JMH (Java Microbenchmark Harness)
- Java 11+

## 🚀 Como Executar

### 1. Compilar e gerar o JAR executável

```bash
mvn clean package
```

Este comando irá:
- Compilar o código-fonte
- Processar as anotações JMH
- Gerar um JAR otimizado (`target/benchmarks.jar`)

### 2. Executar os benchmarks

```bash
java -jar target/benchmarks.jar
```

Ou, para executar um benchmark específico:

```bash
java -jar target/benchmarks.jar MapBenchmark
```

## 📊 O que é Medido

O benchmark executa as seguintes operações:

### Atividade 1.1: HashMap vs TreeMap

| Operação | Estrutura | Complexidade | Descrição |
|----------|-----------|--------------|-----------|
| `hashMapGet` | HashMap | O(1) | Busca de um elemento por chave |
| `treeMapGet` | TreeMap | O(log n) | Busca de um elemento por chave |
| `treeMapRange` | TreeMap | O(log n + k) | Busca de range (intervalo de chaves) |

### Atividade 1.2: ArrayList vs LinkedList

| Operação | Estrutura | Complexidade | Descrição |
|----------|-----------|--------------|-----------|
| `arrayListGet` | ArrayList | O(1) | Acesso ao elemento no índice 5.000 (meio) |
| `linkedListGet` | LinkedList | O(n) | Acesso ao elemento no índice 5.000 (meio) |

## 🔑 Orientação - O que Esperar

### Atividade 1.1: HashMap vs TreeMap

✅ **hashMapGet < treeMapGet** por fator de **3–10x**
- HashMap tem acesso direto O(1)
- TreeMap precisa percorrer árvore O(log n)

✅ **Variância (erro) < 10%** = medição confiável
- JMH usa múltiplas iterações para garantir estabilidade

✅ **TreeMap Range Scan** = diferenciais reais
- HashMap não oferece operações de range
- TreeMap oferece via `subMap()`, `headMap()`, `tailMap()`

### Atividade 1.2: ArrayList vs LinkedList

✅ **arrayListGet << linkedListGet** por fator de **~5.000x**
- ArrayList acesso O(1) = ~10-50 ns/op
- LinkedList acesso O(n) = ~50.000-100.000 ns/op (percorre 5.000 nós)

✅ **Razão esperada: linkedList / array ≈ 5.000x**
- Para n=10.000 e índice=5.000 (meio), LinkedList precisa percorrer ~5.000 nós
- ArrayList acessa diretamente o índice

✅ **Este é um resultado bem diferente de HashMap vs TreeMap**
- HashMap vs TreeMap: 3-10x de diferença
- ArrayList vs LinkedList: 100-5.000x de diferença
- A complexidade O(n) do LinkedList é muito mais custosa que O(log n) do TreeMap

## ⚙️ Configurações JMH

No arquivo `MapBenchmark.java`:

```java
@BenchmarkMode(Mode.AverageTime)      // Mede tempo médio
@OutputTimeUnit(TimeUnit.NANOSECONDS)  // Resultado em nanosegundos
@Fork(2)                               // Executa em 2 JVMs diferentes
@Warmup(iterations = 3)                // 3 iterações de aquecimento
@Measurement(iterations = 5)           // 5 iterações de medição
```

### Por que @Fork(2)?

O compilador JIT invalida medições ingênuas:
- Primeira execução: código interpretado (lento)
- Após ~10k execuções: JIT compila para código nativo (rápido)
- @Fork(2) executa em JVMs separadas para evitar interferência

## 📝 Analisando Resultados

Saída esperada:

```
Benchmark                   Mode  Cnt      Score      Error   Units
MapBenchmark.hashMapGet     avgt    5     15.234 ±    1.234   ns/op
MapBenchmark.treeMapGet     avgt    5    112.567 ±    8.234   ns/op
MapBenchmark.treeMapRange   avgt    5    456.789 ±   34.567   ns/op
```

**Interpretação:**
- `Score`: tempo médio por operação (em nanosegundos)
- `Error`: margem de erro (±valor)
- `Units`: ns/op = nanosegundos por operação

## 🔬 Atividade 1.3: Interpretando GC Logs

### Como Executar

**Windows:**
```bash
.\run-gc-test.bat
```

**Linux/Mac:**
```bash
chmod +x run-gc-test.sh
./run-gc-test.sh
```

### O que a aplicação faz

A classe `GCTestApplication.java` simula carga e gera muitos objetos:
1. Cria 10.000 iterações × 1.000 arrays de 1KB
2. Força o GC a trabalhar constantemente
3. Registra tudo em `gc.log`

### Analisando o gc.log

Após executar, analise o arquivo `gc.log`:

```bash
# Ver o arquivo
cat gc.log (Linux/Mac) ou type gc.log (Windows)
```

Responda às 4 perguntas em [GC_LOG_ANALYSIS.md](GC_LOG_ANALYSIS.md):

1. **Qual o tempo médio entre coletas?**
2. **Qual a duração máxima de pausa?**
3. **As pausas afetariam uma API com SLA de 200ms?**
4. **Que ajuste de heap reduziria a frequência de GC?**

## 📖 Documentação Adicional

- [ATIVIDADE_1_2.md](ATIVIDADE_1_2.md) - Guia detalhado do ArrayList vs LinkedList
- [ATIVIDADE_1_3.md](ATIVIDADE_1_3.md) - Guia completo sobre GC Logs
- [GC_LOG_ANALYSIS.md](GC_LOG_ANALYSIS.md) - Template para análise
- [RESULTADOS.md](RESULTADOS.md) - Folha de respostas

## 🎯 Próximas Atividades

- **Checkpoint 1**: Responder todas as perguntas de análise
- **Trilha 2**: Estruturas em Bancos de Dados

## 📚 Referências

- [JMH Official Documentation](https://github.com/openjdk/jmh)
- [Java GC Tuning](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/)
- [G1 Garbage Collector](https://www.oracle.com/technical-resources/articles/java/g1gc.html)
- [Java Collections Performance](https://docs.oracle.com/javase/tutorial/collections/index.html)
