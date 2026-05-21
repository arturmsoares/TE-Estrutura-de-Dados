# ÍNDICE COMPLETO - Exercícios de Tabelas Hash

## 📋 Resumo Executivo

- ✅ **47 testes JUnit 5** - Todos passando
- ✅ **6 classes implementadas** com encadeamento e endereçamento aberto
- ✅ **3 aplicações web** contextualizadas
- ✅ **Todas as respostas teóricas** documentadas
- ✅ Projeto Maven completo e compilável

---

## 📂 Estrutura do Projeto

```
ExerciciosHashTable/
├── pom.xml                              [Configuração Maven]
├── README.md                            [Guia principal]
├── .gitignore                           [Git ignore]
├── RESPOSTAS_EXERCICIO_1_2.md           [Rastreamento encadeamento]
├── RESPOSTAS_EXERCICIO_2_1.md           [Sondagem linear/quadrática]
├── RESPOSTAS_EXERCICIO_2_2.md           [Double hashing]
│
├── src/main/java/
│   ├── bloco1/
│   │   ├── Produto.java                 [Ex 1.1: hashCode() corrigido]
│   │   ├── Sessao.java                  [Ex 1.1: hashCode() distribuído]
│   │   ├── Coordenada.java              [Ex 1.1: double seguro]
│   │   ├── Exercicio12Respostas.java    [Ex 1.2: Respostas teóricas]
│   │   └── TabelaHashEncadeada.java     [Ex 1.3: Encadeamento separado] ⭐
│   │
│   ├── bloco2/
│   │   ├── Exercicio21Respostas.java    [Ex 2.1: Sondagem análise]
│   │   ├── Exercicio22Respostas.java    [Ex 2.2: Double hashing calc]
│   │   └── TabelaHashAberta.java        [Ex 2.3: Endereçamento aberto] ⭐
│   │
│   └── bloco3/
│       ├── Usuario.java                 [Classe suporte]
│       ├── GerenciadorSessao.java       [Prob 3.1: Cache HTTP] ⭐
│       ├── IndiceInvertido.java         [Prob 3.2: Busca produto] ⭐
│       └── RateLimiter.java             [Prob 3.3: Rate limiting] ⭐
│
└── src/test/java/
    ├── bloco1/TabelaHashEncadeadaTest.java     [7 testes]
    ├── bloco2/TabelaHashAbertaTest.java        [7 testes]
    ├── bloco3/
    │   ├── GerenciadorSessaoTest.java          [10 testes]
    │   ├── IndiceInvertidoTest.java            [12 testes]
    │   └── RateLimiterTest.java                [11 testes]
```

⭐ = Classe principal do exercício com implementação completa

---

# 🎯 BLOCO 1: hashCode(), equals() e Encadeamento Separado

## Exercício 1.1 - Contrato hashCode()/equals()

**Objetivo:** Identificar e corrigir problemas de contrato em 3 classes

**Arquivos:**
- [src/main/java/bloco1/Produto.java](src/main/java/bloco1/Produto.java) - Problema: hashCode() faltando
- [src/main/java/bloco1/Sessao.java](src/main/java/bloco1/Sessao.java) - Problema: hashCode() retorna constante
- [src/main/java/bloco1/Coordenada.java](src/main/java/bloco1/Coordenada.java) - Problema: comparação double com ==

**Respostas incluídas em comentários detalhados em cada arquivo.**

---

## Exercício 1.2 - Rastreamento Manual: Encadeamento Separado

**Objetivo:** Análise manual de tabela hash com h(k) = k % 7

**Arquivo de respostas:** [RESPOSTAS_EXERCICIO_1_2.md](RESPOSTAS_EXERCICIO_1_2.md)

**Conteúdo:**
- Tabela com inserções de 7 chaves
- Análise de colisões e clustering
- Cálculo de fator de carga: λ = 1.0
- Comprimento máximo de cadeia: 3 elementos
- Pior caso: sequência que causa distribuição péssima

**4 Perguntas respondidas com análise completa**

---

## Exercício 1.3 - Implementação: TabelaHashEncadeada<K,V>

**Objetivo:** Implementar tabela com encadeamento separado

**Arquivo principal:** [src/main/java/bloco1/TabelaHashEncadeada.java](src/main/java/bloco1/TabelaHashEncadeada.java)

**Métodos implementados:**
- ✅ `put(K chave, V valor)` - Inserir/atualizar
- ✅ `get(K chave)` - Recuperar valor
- ✅ `remove(K chave)` - Remover par
- ✅ Tratamento de null
- ✅ Cálculo seguro de índice com Math.abs()

**Testes:** [src/test/java/bloco1/TabelaHashEncadeadaTest.java](src/test/java/bloco1/TabelaHashEncadeadaTest.java)
- 7 testes unitários
- Cobertura: put/get, atualização, colisão, remove, null handling

**Resultado:** ✅ 7/7 testes passando

---

# 🎯 BLOCO 2: Endereçamento Aberto, Rehashing e TabelaHash<K,V>

## Exercício 2.1 - Rastreamento Manual: Sondagem Linear e Quadrática

**Objetivo:** Análise de endereçamento aberto com 2 estratégias de sondagem

**Arquivo de respostas:** [RESPOSTAS_EXERCICIO_2_1.md](RESPOSTAS_EXERCICIO_2_1.md)

**Conteúdo:**
- Sondagem linear: h(k, i) = (h(k) + i) % M
- Sondagem quadrática: h(k, i) = (h(k) + i²) % M
- Comparação visual de distribuição
- Explicação de clustering primário
- Fator de carga: λ ≈ 0.545

**2 Perguntas principais:**
1. Fenômeno de clustering e mitigação com quadrática
2. Fator de carga e limites recomendados

---

## Exercício 2.2 - Double Hashing

**Objetivo:** Cálculo de inserções com double hashing

**Arquivo de respostas:** [RESPOSTAS_EXERCICIO_2_2.md](RESPOSTAS_EXERCICIO_2_2.md)

**Conteúdo:**
- Funções: h1(k) = k % 11, h2(k) = 7 - (k % 7)
- Inserção de 6 chaves: 76, 40, 48, 5, 55, 47
- Estado final da tabela (nenhuma colisão!)
- Vantagens de double hashing vs linear/quadrática

---

## Exercício 2.3 - Rehashing: Análise e Implementação

**Objetivo:** Implementar endereçamento aberto com rehashing automático

**Arquivo principal:** [src/main/java/bloco2/TabelaHashAberta.java](src/main/java/bloco2/TabelaHashAberta.java)

**Características implementadas:**
- ✅ Endereçamento aberto (sondagem linear)
- ✅ Lazy deletion com marcador DELETADO
- ✅ Rehashing automático quando λ ≥ 0.70
- ✅ Dobragem de capacidade
- ✅ Reinserção de todos os pares válidos
- ✅ Método get/put/remove com sondagem

**Testes:** [src/test/java/bloco2/TabelaHashAbertaTest.java](src/test/java/bloco2/TabelaHashAbertaTest.java)
- 7 testes unitários
- Validação: rehashing trigger, migração, remove, atualização
- Stress test com 50 inserções

**Resultado:** ✅ 7/7 testes passando

---

# 🎯 BLOCO 3: Problemas Contextualizados em Sistemas Web

## Problema 3.1 - Cache de Sessões HTTP

**Objetivo:** Gerenciador de sessões usando tabela hash para O(1)

**Arquivo principal:** [src/main/java/bloco3/GerenciadorSessao.java](src/main/java/bloco3/GerenciadorSessao.java)

**API:**
- `registrar(String token, Usuario usuario)` - Registrar nova sessão
- `buscar(String token)` - O(1) busca
- `invalidar(String token)` - Remover sessão
- `eValido(String token)` - Verificar validação

**Respostas de análise (comentadas no código):**
- Q10: Complexidade O(1) com λ ≤ 0.75
- Q11: String é ótima chave (imutável, hashCode bem distribuído)
- Q12: Problema em produção (cache não compartilhado), solução (Redis)

**Testes:** [src/test/java/bloco3/GerenciadorSessaoTest.java](src/test/java/bloco3/GerenciadorSessaoTest.java)
- 10 testes unitários
- Cobertura: registrar, buscar, invalidar, validação, exceções

**Resultado:** ✅ 10/10 testes passando

---

## Problema 3.2 - Índice Invertido para Busca de Produtos

**Objetivo:** Implementar busca full-text com índice invertido

**Arquivo principal:** [src/main/java/bloco3/IndiceInvertido.java](src/main/java/bloco3/IndiceInvertido.java)

**API:**
- `indexar(long idProduto, String nomeProduto)` - Indexar produto
- `buscar(String palavra)` - O(1) busca por palavra
- `buscarMultiplas(String consulta)` - Interseção de palavras O(Q×R)

**Exemplo:**
```
Indexar: 1 → "cadeira ergonômica"
Indexar: 3 → "cadeira gamer"

buscar("cadeira") → [1, 3]
buscarMultiplas("cadeira gamer") → [3]
```

**Respostas de análise (comentadas no código):**
- Q13: Complexidade indexar O(W)
- Q14: Complexidade buscarMultiplas O(Q×R)
- Q15: HashMap vs TreeMap (HashMap melhor para busca, TreeMap para prefixo)

**Testes:** [src/test/java/bloco3/IndiceInvertidoTest.java](src/test/java/bloco3/IndiceInvertidoTest.java)
- 12 testes unitários
- Cobertura: indexar, buscar, buscarMultiplas, normalização, interseção

**Resultado:** ✅ 12/12 testes passando

---

## Problema 3.3 - Rate Limiting por IP com Janela Deslizante

**Objetivo:** Limitar 100 requisições por minuto por IP

**Arquivo principal:** [src/main/java/bloco3/RateLimiter.java](src/main/java/bloco3/RateLimiter.java)

**API:**
- `permitir(String ip)` - O(1) verificar e autorizar
- `limparExpirados()` - O(N) limpeza de entradas expiradas
- `contadorIP(String ip)` - Número de requisições
- `totalIPsAtivos()` - Contagem de IPs

**Implementação:**
- HashMap com IP → Janela(timestamp, contador)
- Remoção segura com `removeIf()`
- Lógica: se λ ≥ 60s desde início da janela, reset

**Respostas de análise (comentadas no código):**
- Q16: Complexidade permitir O(1), limparExpirados O(N)
- Q17: ConcurrentModificationException com forEach + remove (solução: removeIf)
- Q18: LinkedHashMap com accessOrder=true para expiração automática/LRU

**Testes:** [src/test/java/bloco3/RateLimiterTest.java](src/test/java/bloco3/RateLimiterTest.java)
- 11 testes unitários
- Cobertura: limite, independência de IPs, limpeza, stress test

**Resultado:** ✅ 11/11 testes passando

---

# 📊 Resumo de Testes

| Bloco | Exercício | Classe Testada | Testes | Status |
|-------|-----------|---|---|---|
| 1 | 1.3 | TabelaHashEncadeada | 7 | ✅ PASS |
| 2 | 2.3 | TabelaHashAberta | 7 | ✅ PASS |
| 3 | 3.1 | GerenciadorSessao | 10 | ✅ PASS |
| 3 | 3.2 | IndiceInvertido | 12 | ✅ PASS |
| 3 | 3.3 | RateLimiter | 11 | ✅ PASS |
| **TOTAL** | — | — | **47** | ✅ **PASS** |

---

# 🔑 Conceitos Implementados

## Fundamentais
- ✅ Função hash: h(k) = k.hashCode() % M
- ✅ Contrato hashCode/equals()
- ✅ Fator de carga: λ = n/M

## Encadeamento
- ✅ Linked lists em cada bucket
- ✅ Resolução de colisões por chaining
- ✅ Complexidade média: O(1 + λ)

## Endereçamento Aberto
- ✅ Sondagem linear: h(k,i) = (h(k) + i) % M
- ✅ Sondagem quadrática: h(k,i) = (h(k) + i²) % M
- ✅ Double hashing: h(k,i) = (h1(k) + i×h2(k)) % M
- ✅ Lazy deletion com tombstones
- ✅ Clustering primário e secundário

## Rehashing
- ✅ Trigger: λ ≥ 0.70
- ✅ Dobragem de capacidade
- ✅ Reinserção de todos os pares

## Aplicações Reais
- ✅ Cache de sessões HTTP
- ✅ Índice invertido para busca full-text
- ✅ Rate limiting

---

# 🚀 Como Usar

### Compilar
```bash
mvn clean compile
```

### Executar todos os testes
```bash
mvn test
```

### Executar teste específico
```bash
mvn test -Dtest=TabelaHashEncadeadaTest
mvn test -Dtest=GerenciadorSessaoTest
```

### Teste por bloco
```bash
mvn test -Dtest=bloco1.*
mvn test -Dtest=bloco2.*
mvn test -Dtest=bloco3.*
```

---

# 📚 Leitura Complementar

### Respostas Teóricas
Todas as questões de análise (1-18) estão documentadas:
1. **Questões 1-4:** Em `Exercicio12Respostas.java` e [RESPOSTAS_EXERCICIO_1_2.md](RESPOSTAS_EXERCICIO_1_2.md)
2. **Questões 5-6:** Em [RESPOSTAS_EXERCICIO_2_1.md](RESPOSTAS_EXERCICIO_2_1.md)
3. **Questão 7-9:** Em `TabelaHashAberta.java`
4. **Questões 10-12:** Em `GerenciadorSessao.java`
5. **Questões 13-15:** Em `IndiceInvertido.java`
6. **Questões 16-18:** Em `RateLimiter.java`

### Referências
- CORMEN et al. - Introdução a Algoritmos, Cap. 11
- BLOCH, J. - Java Efetivo, Item 11
- Oracle Java Documentation

---

# ✨ Destaques

- **🎯 Implementação Completa:** Todos os 6 exercícios com código funcional
- **📝 Documentação Extensa:** Respostas teóricas em comentários e markdown
- **✅ Testes Robustos:** 47 testes unitários com 100% de aprovação
- **🏗️ Pronto para Produção:** Projeto Maven compilável e executável
- **💡 Aplicações Reais:** 3 sistemas web contextualizados (cache, busca, rate limiting)

---

**Data:** Maio 2026  
**Disciplina:** Estruturas de Dados  
**Instituição:** IFMTM - Campus Uberlândia Centro  
**Curso:** Tecnologia em Sistemas para Internet

---

## Checklist de Entrega

- ✅ Repositório Git com exercícios resolvidos
- ✅ Suíte JUnit 5 passando (47/47 testes)
- ✅ Código compilável com Maven
- ✅ Respostas de análise documentadas
- ✅ Todas as 3 aplicações web implementadas
- ✅ README e documentação completa

**Status: PRONTO PARA ENTREGA** ✅
