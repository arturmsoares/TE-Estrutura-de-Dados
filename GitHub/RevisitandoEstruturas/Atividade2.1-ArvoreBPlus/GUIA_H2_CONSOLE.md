# Guia Prático: H2 Console e EXPLAIN Queries

**Objetivo**: Analisar planos de execução SQL em tempo real durante Atividade 2.1

---

## 1. Acessar H2 Console

### Passo 1: Iniciar Aplicação

```bash
cd Atividade2.1-ArvoreBPlus
mvn clean spring-boot:run
```

**Sucesso esperado**:
```
Started Atividade2Application in 2.3 seconds
```

### Passo 2: Abrir H2 Console

**URL**: `http://localhost:8080/h2-console`

**Login (H2 Console)**:
- JDBC URL: `jdbc:h2:mem:testdb`
- User Name: `sa`
- Password: (deixe em branco)
- Driver Class: `org.h2.Driver`

Clique em **Connect**

---

## 2. Preparar Dados

### Inserir dados teste

Copie e execute no H2 Console:

```sql
-- Limpar dados anteriores
DELETE FROM transacoes;

-- Inserir 50 transações para teste
INSERT INTO transacoes(conta_id, data_transacao, valor, descricao, tipo) VALUES
(1, CURRENT_TIMESTAMP, 100.00, 'Transacao 1', 'DÉBITO'),
(1, CURRENT_TIMESTAMP, 150.00, 'Transacao 2', 'CRÉDITO'),
(2, CURRENT_TIMESTAMP, 200.00, 'Transacao 3', 'DÉBITO'),
(2, CURRENT_TIMESTAMP, 250.00, 'Transacao 4', 'CRÉDITO'),
(3, CURRENT_TIMESTAMP, 300.00, 'Transacao 5', 'DÉBITO'),
-- ... (mais inserções podem ser feitas programaticamente via API)
```

---

## 3. Executar EXPLAIN (Análise de Planos)

### Exemplo 1: Query COM Índice

```sql
EXPLAIN SELECT * FROM transacoes WHERE conta_id = 1;
```

**Resultado esperado** (H2):

```
┌──────────────────────────────────────────────┐
│ PLAN                                         │
├──────────────────────────────────────────────┤
│ SELECT *                                     │
│ FROM PUBLIC.TRANSACOES                       │
│ /* index lookup: PUBLIC.IDX_CONTA_ID: ...*/  │
│ WHERE CONTA_ID = 1                           │
└──────────────────────────────────────────────┘
```

**Interpretação**:
- ✅ Contém: `index lookup`
- ✅ Usa: `IDX_CONTA_ID` (índice B+)
- ✅ Operação: **INDEX SCAN** - O(log n)

---

### Exemplo 2: Query SEM Índice

```sql
EXPLAIN SELECT * FROM transacoes WHERE valor > 100;
```

**Resultado esperado** (H2):

```
┌──────────────────────────────────────────────┐
│ PLAN                                         │
├──────────────────────────────────────────────┤
│ SELECT *                                     │
│ FROM PUBLIC.TRANSACOES                       │
│ WHERE VALOR > 100                            │
│ /* scan: PUBLIC.TRANSACOES */                │
└──────────────────────────────────────────────┘
```

**Interpretação**:
- ❌ Contém: `scan` (sem index)
- ❌ Percorre: Todas as linhas
- ❌ Operação: **TABLE SCAN** - O(n)

---

## 4. Comparação Visual

### Copiar as duas queries abaixo uma após outra

#### Query 1: COM Índice

```sql
-- Executar com F5
EXPLAIN SELECT * FROM transacoes WHERE conta_id = 1;
```

**Resultado**: Veja `index lookup`

---

#### Query 2: SEM Índice

```sql
-- Executar com F5
EXPLAIN SELECT * FROM transacoes WHERE valor > 100;
```

**Resultado**: Veja `scan`

---

## 5. Consultas Úteis no H2 Console

### Ver Todas as Transações

```sql
SELECT * FROM transacoes;
```

---

### Contar Registros por Conta

```sql
SELECT conta_id, COUNT(*) as total 
FROM transacoes 
GROUP BY conta_id 
ORDER BY conta_id;
```

---

### Ver Índices Criados

```sql
SELECT * FROM INFORMATION_SCHEMA.INDEXES 
WHERE TABLE_NAME = 'TRANSACOES';
```

**Resultado esperado**:

| INDEX_NAME | COLUMN_NAME | UNIQUE | COMMENT |
|------------|-------------|--------|---------|
| IDX_CONTA_ID | CONTA_ID | FALSE | |
| IDX_CONTA_DATA | CONTA_ID, DATA_TRANSACAO | FALSE | |
| PRIMARY_KEY | ID | TRUE | |

---

### Criar Índice Adicional (Teste)

```sql
-- Criar índice na coluna 'tipo'
CREATE INDEX idx_tipo ON transacoes(tipo);

-- Verificar EXPLAIN após criar
EXPLAIN SELECT * FROM transacoes WHERE tipo = 'DÉBITO';
```

---

### Remover Índice

```sql
-- Remover índice de teste
DROP INDEX idx_tipo;

-- Verificar EXPLAIN após remover
EXPLAIN SELECT * FROM transacoes WHERE tipo = 'DÉBITO';
```

---

## 6. Análise Passo-a-Passo

### Procedimento Completo

1. **Preparar Console**:
   - Limpar dados: `DELETE FROM transacoes;`
   - Inserir 100 registros via API: `GET /api/transacoes/popular`

2. **Analisar Query COM Índice**:
   ```sql
   EXPLAIN SELECT * FROM transacoes WHERE conta_id = 5;
   ```
   - Esperar: `index lookup` ou `INDEX SCAN`
   - Registrar resultado

3. **Analisar Query SEM Índice**:
   ```sql
   EXPLAIN SELECT * FROM transacoes WHERE valor > 200;
   ```
   - Esperar: `scan` ou `TABLE SCAN`
   - Registrar resultado

4. **Comparar Performance**:
   - Contar registros analisados (WITH TIMING)
   - Medir diferença de velocidade

---

## 7. Modo Análise Completa (EXPLAIN ANALYZE)

H2 também suporta `EXPLAIN ANALYZE` (execute e mostre métricas reais):

```sql
EXPLAIN ANALYZE SELECT * FROM transacoes WHERE conta_id = 5;
```

---

## 8. Referência Rápida de Operações

| Tipo de Operação | Palavra-chave | Complexidade |
|-------------------|---------------|-------------|
| INDEX LOOKUP | `index lookup:` | O(log n) ✅ |
| INDEX SCAN | `Index scan:` | O(log n) ✅ |
| TABLE SCAN | `scan:` | O(n) ❌ |
| FULL SCAN | `full scan` | O(n) ❌ |

---

## 9. Troubleshooting

### Problema: H2 Console não conecta

**Solução**:
```bash
# Verificar se aplicação está rodando
curl http://localhost:8080/api/transacoes/info

# Se responder, o servidor está ok
# Se não responder, iniciar: mvn spring-boot:run
```

---

### Problema: Tabela `transacoes` não aparece

**Solução**:
```sql
-- H2 Console
-- 1. Atualizar schema (F5)
-- 2. Expandir menu "PUBLIC"
-- 3. Se ainda não aparecer, executar via API:
GET http://localhost:8080/api/transacoes/popular
```

---

### Problema: EXPLAIN retorna vazio

**Solução**:
```sql
-- Tentar com maiúsculas:
EXPLAIN SELECT * FROM "TRANSACOES" WHERE "CONTA_ID" = 5;

-- Ou usar script pré-pronto:
SELECT COUNT(*) FROM transacoes;  -- primeiro testar acesso
```

---

## 10. Template para Registro de Resultados

Copie e preencha na atividade:

```
Query COM Índice:
  SQL: EXPLAIN SELECT * FROM transacoes WHERE conta_id = 5;
  Resultado: ____________________
  Tipo: ☐ INDEX SCAN   ☐ TABLE SCAN
  
Query SEM Índice:
  SQL: EXPLAIN SELECT * FROM transacoes WHERE valor > 200;
  Resultado: ____________________
  Tipo: ☐ INDEX SCAN   ☐ TABLE SCAN
```

---

**Dúvidas?** Consulte `CONCEITOS_BTREE.md` para entender a teoria por trás.
