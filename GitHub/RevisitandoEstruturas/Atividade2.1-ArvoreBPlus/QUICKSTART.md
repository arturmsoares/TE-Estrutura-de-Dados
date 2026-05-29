# QUICKSTART - Atividade 2.1 (5 minutos)

## Passos Rápidos

### 1. Iniciar a Aplicação (30 segundos)

```bash
cd Atividade2.1-ArvoreBPlus
mvn clean spring-boot:run
```

**Sucesso**: Veja `Started Atividade2Application`

---

### 2. Popular Banco (10 segundos)

```bash
curl http://localhost:8080/api/transacoes/popular
```

**Sucesso**: Retorna `{"status":"OK","mensagem":"100 transações inseridas com sucesso"}`

---

### 3. Query COM Índice (30 segundos)

**Browser**:
```
http://localhost:8080/api/transacoes/buscar-com-indice?contaId=5
```

**Terminal**:
```bash
curl "http://localhost:8080/api/transacoes/buscar-com-indice?contaId=5" | jq
```

**Esperado**:
```json
{
  "query": "SELECT * FROM transacoes WHERE conta_id = 5",
  "tipo_plano": "INDEX SCAN (usa índice idx_conta_id)",
  "complexidade": "O(log n) — percorre árvore B+",
  "resultados": 5,
  "tempo_ms": 1
}
```

---

### 4. H2 Console - Verificar Plano (1 minuto)

1. Abrir: `http://localhost:8080/h2-console`
2. JDBC URL: `jdbc:h2:mem:testdb`
3. Executar:
   ```sql
   EXPLAIN SELECT * FROM transacoes WHERE conta_id = 5;
   ```
4. **Resultado**: Veja `INDEX SCAN` ou `INDEX LOOKUP`

---

### 5. Query SEM Índice (30 segundos)

**Browser**:
```
http://localhost:8080/api/transacoes/buscar-sem-indice?valor=200
```

**Terminal**:
```bash
curl "http://localhost:8080/api/transacoes/buscar-sem-indice?valor=200" | jq
```

**Esperado**:
```json
{
  "query": "SELECT * FROM transacoes WHERE valor > 200",
  "tipo_plano": "TABLE SCAN (sem índice)",
  "complexidade": "O(n) — percorre todas as linhas",
  "resultados": 25,
  "tempo_ms": 1
}
```

---

### 6. H2 Console - Verificar Segundo Plano (1 minuto)

1. H2 Console ainda aberto
2. Executar:
   ```sql
   EXPLAIN SELECT * FROM transacoes WHERE valor > 200;
   ```
3. **Resultado**: Veja `SCAN` (sem INDEX)

---

## Respostas Esperadas

| Pergunta | Resposta |
|----------|----------|
| Qual é mais rápido: INDEX SCAN ou TABLE SCAN? | INDEX SCAN |
| Complexidade com índice | O(log n) |
| Complexidade sem índice | O(n) |
| Por que usar índices? | Reduzem drasticamente número de acessos a disco |

---

## Endpoints da API

| Endpoint | Método | Descrição |
|----------|--------|-----------|
| `/api/transacoes/info` | GET | Retorna informações do projeto |
| `/api/transacoes/popular` | GET | Insere 100 transações teste |
| `/api/transacoes/buscar-com-indice` | GET | Query com INDEX SCAN (conta_id) |
| `/api/transacoes/buscar-sem-indice` | GET | Query com TABLE SCAN (valor) |
| `/api/transacoes/listar-tudo` | GET | Lista todas as transações |

---

## Parar a Aplicação

```
CTRL + C
```

---

**Próxima**: Atividade 2.2 - N+1 Query Problem (15 minutos)
