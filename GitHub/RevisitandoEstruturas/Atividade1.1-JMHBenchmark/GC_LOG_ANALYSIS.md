# Análise do GC Log - Atividade 1.3

## 📋 Seu GC Log

Copie e cole o conteúdo de `gc.log` aqui:

```
[COLE O CONTEÚDO DE gc.log AQUI]
```

## 📊 Análise

### a) Qual o tempo médio entre coletas?

**Eventos de GC encontrados:**

```
Evento 1: ___________s
Evento 2: ___________s
Evento 3: ___________s
Evento 4: ___________s
Evento 5: ___________s

Intervalo 1→2: __________ ms
Intervalo 2→3: __________ ms
Intervalo 3→4: __________ ms
Intervalo 4→5: __________ ms

Tempo MÉDIO entre coletas: __________ ms
```

**Interpretação:**
```
Se < 1000ms (1s): GC está muito frequente → Heap pequeno
Se > 1000ms:      GC é raro → Heap adequado
```

**Diagnóstico:** _______________________________________________________________

---

### b) Qual a duração máxima de pausa?

**Pausas encontradas no log:**

```
Pausa 1: __________ ms
Pausa 2: __________ ms
Pausa 3: __________ ms
Pausa 4: __________ ms
Pausa 5: __________ ms

PAUSA MÁXIMA: __________ ms
PAUSA MÍNIMA: __________ ms
PAUSA MÉDIA: __________ ms
```

**Interpretação:**
```
Se < 50ms:   ✅ Excelente (imperceptível)
Se 50-200ms: ⚠️  Aceitável (notável)
Se > 200ms:  ❌ Ruim (viola SLA)
```

**Diagnóstico:** _______________________________________________________________

---

### c) As pausas afetariam uma API com SLA de 200ms?

**Análise:**

```
Pausa máxima obtida: __________ ms
SLA da API: 200 ms

Tempo que a pausa de GC ocupa do SLA: __________%

Exemplo de request:
  - Processamento: ~100ms
  - Query BD: ~50ms
  - Pausa GC: __________ ms
  - TOTAL: __________ ms

Resultado: ✓ Dentro do SLA / ✗ Viola SLA
```

**Conclusão:** ___________________________________________________________________

___________________________________________________________________________

---

### d) Que ajuste de heap reduziria a frequência de GC?

**Ajustes recomendados:**

1. **Aumentar `-Xmx` (Heap máximo)**
   ```bash
   Atual:      -Xmx256m
   Sugerido:   -Xmx512m ou -Xmx1g
   Efeito:     Reduz frequência, aumenta duração de pausas
   ```

2. **Usar G1GC com MaxGCPauseMillis**
   ```bash
   java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xmx512m ...
   Efeito:     Mantém pausas curtas mesmo com heap grande
   ```

3. **Reduzir objetos de vida curta (no código)**
   ```java
   // Implementar cache
   // Reutilizar objetos
   // Reduzir alocações temporárias
   ```

**Minha Recomendação:**

Ajuste 1: _____________________________________________________________________

Ajuste 2: _____________________________________________________________________

Ajuste 3: _____________________________________________________________________

---

## 🎯 Checkpoint 1 - Respostas Finais

### 1. Qual estrutura foi mais rápida no benchmark: HashMap ou TreeMap? Por quê?

**Resposta:**

___________________________________________________________________________

___________________________________________________________________________

### 2. Em qual cenário o TreeMap superaria o HashMap na prática?

**Resposta:**

___________________________________________________________________________

___________________________________________________________________________

### 3. O que aconteceria ao benchmark se você remover @Fork? Por quê isso é problemático?

**Resposta:**

___________________________________________________________________________

___________________________________________________________________________

### 4. Quais flags JVM você usaria para monitorar GC em produção?

**Resposta:**

___________________________________________________________________________

___________________________________________________________________________

---

## 📈 Gráficos (opcional)

Se desejar, crie gráficos com:
- Eixo X: Tempo (segundos)
- Eixo Y: Tamanho do heap (MB)
- Marque as pausas de GC

Ferramentas úteis:
- [GCeasy](https://gceasy.io/) - Análise online de GC logs
- [GCviewer](https://www.tagtraum.com/gcviewer.html) - Visualização local
- Excel/Google Sheets - Gráficos customizados

---

## ✅ Checklist Final

- [ ] Executei `run-gc-test.bat` (ou `.sh`)
- [ ] Gerei o arquivo `gc.log`
- [ ] Analisei o gc.log respeitando todas as 4 perguntas
- [ ] Respondi todas as perguntas do Checkpoint 1
- [ ] Documentei minha análise neste arquivo
