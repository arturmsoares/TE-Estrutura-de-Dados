# 🚀 Quick Start - Atividade 1.3

## ⏱️ 5 Minutos para Começar

### 1️⃣ Windows
```bash
.\run-gc-test.bat
```

### 2️⃣ Linux/Mac
```bash
chmod +x run-gc-test.sh
./run-gc-test.sh
```

---

## 📊 Resultado

Um arquivo `gc.log` será criado com eventos de GC.

---

## 📋 Respostas Esperadas

Abra [GC_LOG_ANALYSIS.md](GC_LOG_ANALYSIS.md) e responda:

| Pergunta | Resposta | Significado |
|----------|----------|-------------|
| a) Tempo médio entre coletas | ____ ms | < 1000ms = ruim, > 1000ms = bom |
| b) Pausa máxima | ____ ms | < 50ms = ✅, > 200ms = ❌ |
| c) Viola SLA 200ms? | Sim/Não | Impacto na API |
| d) Ajuste de heap | `java -Xmx___` | Aumentar reduz frequência |

---

## 📝 Registre em

[RESULTADOS.md](RESULTADOS.md#-atividade-13-interpretando-gc-logs)

---

## 💡 Leitura Rápida

- [ATIVIDADE_1_3.md](ATIVIDADE_1_3.md) - Guia completo
- [GC_LOG_ANALYSIS.md](GC_LOG_ANALYSIS.md) - Template
