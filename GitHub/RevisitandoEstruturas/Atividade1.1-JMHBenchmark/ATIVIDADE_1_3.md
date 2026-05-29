# Atividade 1.3 - Interpretando GC Logs

## 📝 O que é GC (Garbage Collection)?

**GC** é o processo automático da JVM que:
1. Identifica objetos que não estão mais em uso
2. Libera a memória por eles ocupada
3. Reorganiza a memória restante

### Problema: Pausas de GC

Quando o GC roda, **pausa toda a aplicação** para limpar memória:
- Isto afeta a latência da aplicação
- APIs REST precisam de respostas rápidas (SLA)
- Pausas longas violam SLAs

## 🚀 Como Executar a Atividade 1.3

### Windows
```bash
.\run-gc-test.bat
```

### Linux/Mac
```bash
chmod +x run-gc-test.sh
./run-gc-test.sh
```

### Manualmente (qualquer SO)
```bash
javac src/main/java/br/edu/iftm/estruturas/benchmark/GCTestApplication.java
java -Xms256m -Xmx256m -Xlog:gc*:file=gc.log:time,uptime ^
    -cp src/main/java ^
    br.edu.iftm.estruturas.benchmark.GCTestApplication
```

## 📊 O que o programa faz?

A classe `GCTestApplication.java` executa 3 fases:

### Fase 1: Objetos de vida curta (Alto GC pressure)
```
10.000 iterações × 1.000 arrays × 1KB = ~10GB de lixo criado
Cada iteração cria objetos, usa, e descarta
→ Força o GC a trabalhar constantemente
```

### Fase 2: Objetos de vida longa
```
50 arrays × 10KB = ~500KB persistentes
Vivem até o final do programa
→ Mantém heap ocupado
```

### Fase 3: Aguarda 10 segundos
```
Permite observar o comportamento do GC em repouso
```

## 📈 Analisando o gc.log

Após executar, abra o arquivo `gc.log` gerado:

```bash
# Windows
type gc.log

# Linux/Mac
cat gc.log
```

### Formato típico do log:

```
[2026-05-28T20:35:15.123+0000][0.145s] GC(0) Pause Young (Normal) (G1 Evacuation Pause)
[2026-05-28T20:35:15.125+0000][0.147s] GC(0) Pause Young (Normal) (G1 Evacuation Pause) 2M->1M(256M) 2.567ms
[2026-05-28T20:35:15.156+0000][0.178s] GC(1) Pause Young (Normal) (G1 Evacuation Pause) 2M->1M(256M) 2.123ms
```

### Interpretação:

| Campo | Significado |
|-------|------------|
| `[2026-05-28T20:35:15.123+0000]` | Timestamp absoluto |
| `[0.145s]` | Uptime (tempo desde início) |
| `GC(0)` | ID do evento de GC |
| `Pause Young` | Tipo de pausa (Young Gen) |
| `2M->1M(256M)` | Memória: antes→depois(total) |
| `2.567ms` | Duração da pausa |

## 🔍 Perguntas para Responder

### a) Qual o tempo médio entre coletas?

**Como encontrar:**
1. Liste todos os eventos de GC
2. Calcule o tempo entre eventos consecutivos
3. Tire a média

**Exemplo:**
```
Evento 1: 0.145s
Evento 2: 0.178s
Evento 3: 0.212s
Intervalo 1: 0.033s (33ms)
Intervalo 2: 0.034s (34ms)
Média: 33.5ms
```

**O que significa:**
- **< 1s entre coletas** = Alto GC pressure (heap pequeno)
- **> 10s entre coletas** = Baixo GC pressure (heap grande ou pouca carga)

### b) Qual a duração máxima de pausa?

**Como encontrar:**
1. Encontre todos os valores `XXXms` no log
2. Identifique o maior

**Exemplo:**
```
2.567ms
2.123ms
3.456ms  ← Máximo
1.789ms
```

**O que significa:**
- **< 50ms** = Excelente (imperceptível ao usuário)
- **50-200ms** = Aceitável (pode ser notado)
- **> 200ms** = Problemático (viola SLA de APIs)

### c) As pausas afetariam uma API com SLA de 200ms?

**Lógica:**
```
Se pausa máxima > 200ms → SIM, viola SLA
Se pausa máxima ≤ 200ms → Provavelmente não

Também considerar:
- Tempo de processamento da request
- Tempo de I/O do banco
- Pausa de GC

Exemplo: 
  Processamento: 150ms
  Pausa GC: 80ms
  Total: 230ms > 200ms SLA → VIOLA
```

### d) Que ajuste de heap reduziria a frequência de GC?

**Resposta:** **Aumentar o `-Xmx`**

**Lógica:**
```
Heap pequeno (-Xmx256m):
  ↓ Mais rápido fica cheio
  ↓ GC roda mais frequentemente

Heap grande (-Xmx512m ou -Xmx1g):
  ↓ Demora mais para ficar cheio
  ↓ GC roda menos frequentemente
  ↑ Mas cada pausa pode ser mais longa
```

**Trade-off:**
- ✅ Aumentar heap = menos pausas, melhor throughput
- ❌ Aumentar heap = pausas podem ser mais longas
- ✅ Melhor solução: usar G1GC com `-XX:MaxGCPauseMillis=200`

## 💡 Soluções para Reduzir GC Pressure

### 1. Aumentar o Heap
```bash
java -Xms512m -Xmx1g -Xlog:gc* ...
     # De 256m para 512m-1g
```

### 2. Usar G1GC (default em Java 9+)
```bash
java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xlog:gc* ...
```

### 3. Reduzir criação de objetos
```java
// ❌ Ruim: cria objeto a cada iteração
for (int i = 0; i < 1000; i++) {
    String s = new String("valor");  // garbage
}

// ✅ Bom: reutiliza objeto
String s = "valor";
for (int i = 0; i < 1000; i++) {
    // usa s
}
```

### 4. Usar Cache
```java
// ✅ Bom: objeto lives long
Map<String, Data> cache = new HashMap<>();
cache.put("key", expensiveData);  // criado uma vez
```

## 📚 Documentação de Flags JVM

### Flags usados no teste

| Flag | Significado |
|------|------------|
| `-Xms256m` | Heap mínimo = 256MB |
| `-Xmx256m` | Heap máximo = 256MB |
| `-Xlog:gc*` | Log todos os eventos de GC |
| `file=gc.log` | Escrever em arquivo |
| `time,uptime` | Incluir timestamp e uptime |

### Outros flags úteis

| Flag | Significado |
|------|------------|
| `-XX:+UseG1GC` | Usar G1 Garbage Collector |
| `-XX:MaxGCPauseMillis=200` | Pausa máxima alvo: 200ms |
| `-XX:+PrintGCDetails` | Detalhes verbose |
| `-XX:+PrintGCDateStamps` | Timestamp absoluto |
| `-XX:+PrintHeapAtGC` | Estado do heap antes/depois de GC |

## 🎯 Próximas Atividades

- Checkpoint 1: Responder todas as perguntas da Atividade 1.3
- Trilha 2: Estruturas em Bancos de Dados

## 📖 Referências

- [Java GC Documentation](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/)
- [G1 Garbage Collector](https://www.oracle.com/technical-resources/articles/java/g1gc.html)
- [JVM Flags](https://chriswhocodes.com/hotspot_options/)
