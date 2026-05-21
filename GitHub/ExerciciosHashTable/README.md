# Exercícios de Tabelas Hash

Implementação completa dos exercícios práticos de Tabelas Hash da disciplina de Estruturas de Dados - IFMTM.

## Estrutura do Projeto

```
ExerciciosHashTable/
├── pom.xml                          # Configuração Maven
├── README.md                         # Este arquivo
├── src/
│   ├── main/java/
│   │   ├── bloco1/                  # Exercícios do Bloco 1
│   │   │   ├── Produto.java         # Ex 1.1: Classe com hashCode()/equals() corrigido
│   │   │   ├── Sessao.java          # Ex 1.1: Classe com hashCode() bem distribuído
│   │   │   ├── Coordenada.java      # Ex 1.1: Classe com comparação segura de doubles
│   │   │   ├── Exercicio12Respostas.java  # Ex 1.2: Respostas teóricas
│   │   │   └── TabelaHashEncadeada.java   # Ex 1.3: Implementação com encadeamento separado
│   │   │
│   │   ├── bloco2/                  # Exercícios do Bloco 2
│   │   │   ├── Exercicio21Respostas.java  # Ex 2.1: Rastreamento sondagem linear/quadrática
│   │   │   ├── Exercicio22Respostas.java  # Ex 2.2: Double hashing
│   │   │   └── TabelaHashAberta.java      # Ex 2.3: Endereçamento aberto com rehashing
│   │   │
│   │   └── bloco3/                  # Problemas contextualizados do Bloco 3
│   │       ├── Usuario.java              # Classe de suporte
│   │       ├── GerenciadorSessao.java    # Prob 3.1: Cache de sessões HTTP
│   │       ├── IndiceInvertido.java      # Prob 3.2: Índice para busca de produtos
│   │       └── RateLimiter.java          # Prob 3.3: Rate limiting por IP
│   │
│   └── test/java/
│       ├── bloco1/TabelaHashEncadeadaTest.java    # Testes Ex 1.3
│       ├── bloco2/TabelaHashAbertaTest.java       # Testes Ex 2.3
│       ├── bloco3/                                 # Testes Bloco 3
│       │   ├── GerenciadorSessaoTest.java
│       │   ├── IndiceInvertidoTest.java
│       │   └── RateLimiterTest.java
│       └── (mais testes conforme necessário)
```

## Bloco 1: hashCode(), equals() e encadeamento separado

### Exercício 1.1 - Contrato hashCode()/equals()
Identificação e correção de 3 classes com problemas:
- **Produto**: Falta de `hashCode()` → viola contrato HashMap
- **Sessao**: `hashCode()` retorna constante → degrada para O(n)
- **Coordenada**: Comparação de `double` com `==` → resultado imprevisível

**Respostas incluídas como comentários no código.**

### Exercício 1.2 - Rastreamento manual
Análise de encadeamento separado com função hash h(k) = k % 7
- Cálculo de fator de carga
- Identificação de clustering
- Proposição de pior caso

**Respostas em `Exercicio12Respostas.java`**

### Exercício 1.3 - Implementação
Classe `TabelaHashEncadeada<K,V>` com:
- ✅ `put(K chave, V valor)` - Insert/Update
- ✅ `get(K chave)` - Retrieve
- ✅ `remove(K chave)` - Delete
- ✅ Testes JUnit 5 completos (8 testes)

## Bloco 2: Endereçamento aberto, rehashing e TabelaHash<K,V> genérica

### Exercício 2.1 - Rastreamento manual
Análise de sondagem linear h(k, i) = (h(k) + i) % M
- Preenchimento de tabela com 6 inserções
- Clustering primário
- Sondagem quadrática como mitigação

**Respostas em `Exercicio21Respostas.java`**

### Exercício 2.2 - Double hashing
Cálculo de inserções com h1(k) = k % 11 e h2(k) = 7 - (k % 7)
- Resolução de 6 chaves
- Análise de colisões

**Respostas em `Exercicio22Respostas.java`**

### Exercício 2.3 - Rehashing
Classe `TabelaHashAberta<K,V>` com:
- ✅ Endereçamento aberto (sondagem linear)
- ✅ Rehashing automático quando λ ≥ 0.70
- ✅ Lazy deletion com marcador `DELETADO`
- ✅ Testes JUnit 5 completos (7 testes)

## Bloco 3: Problemas contextualizados em sistemas web

### Problema 3.1 - Cache de sessões HTTP
Classe `GerenciadorSessao` usando `TabelaHashEncadeada<String, Usuario>`:
- ✅ O(1) amortizado para registrar(), buscar(), invalidar()
- ✅ Tokens UUID como chaves
- ✅ Suporte a invalidação de sessões
- ✅ Testes JUnit 5 completos (8 testes)

**Respostas de análise incluídas em comentários**

### Problema 3.2 - Índice invertido para busca
Classe `IndiceInvertido` para busca full-text em nomes de produtos:
- ✅ `indexar(long idProduto, String nomeProduto)` - O(W)
- ✅ `buscar(String palavra)` - O(1)
- ✅ `buscarMultiplas(String consulta)` - O(Q × R)
- ✅ Normalização de palavras (lowercase)
- ✅ Testes JUnit 5 completos (10 testes)

**Respostas de análise incluídas em comentários**

### Problema 3.3 - Rate limiting por IP
Classe `RateLimiter` com janela deslizante:
- ✅ Máximo 100 requisições por minuto por IP
- ✅ `permitir(String ip)` - O(1)
- ✅ `limparExpirados()` - O(N)
- ✅ Remoção segura com `removeIf()`
- ✅ Testes JUnit 5 completos (9 testes)

**Respostas de análise incluídas em comentários**

## Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Compilar o projeto
```bash
mvn clean compile
```

### Executar todos os testes
```bash
mvn test
```

### Executar testes de um bloco específico
```bash
# Bloco 1
mvn test -Dtest=bloco1.*

# Bloco 2
mvn test -Dtest=bloco2.*

# Bloco 3
mvn test -Dtest=bloco3.*
```

### Executar teste específico
```bash
mvn test -Dtest=TabelaHashEncadeadaTest
mvn test -Dtest=TabelaHashAbertaTest
mvn test -Dtest=GerenciadorSessaoTest
mvn test -Dtest=IndiceInvertidoTest
mvn test -Dtest=RateLimiterTest
```

### Ver relatório de testes
```bash
mvn surefire-report:report
```

## Respostas aos Exercícios

### Exercício 1.2 - Respostas teóricas
Ver arquivo [src/main/java/bloco1/Exercicio12Respostas.java](src/main/java/bloco1/Exercicio12Respostas.java)

Resumo:
- λ = 1.0
- Cadeia mais longa: 3 elementos (bucket 3)
- Chave 38 → bucket 3 (colisão)
- Pior caso: todas as chaves ≡ 0 (mod 7)

### Exercício 2.1 - Respostas teóricas
Ver arquivo [src/main/java/bloco2/Exercicio21Respostas.java](src/main/java/bloco2/Exercicio21Respostas.java)

Resumo:
- Clustering primário é o fenômeno de agrupamento
- Sondagem quadrática reduz com passos i², i⁴, i⁹, ...
- λ ≈ 0.545 está dentro do limite recomendado

### Exercício 2.2 - Respostas teóricas
Ver arquivo [src/main/java/bloco2/Exercicio22Respostas.java](src/main/java/bloco2/Exercicio22Respostas.java)

Resumo:
- Nenhuma colisão nesta sequência
- Todos os 6 elementos encontram slot livre na primeira sonda

### Respostas de Análise (Blocos 1, 2 e 3)
Todas as perguntas de análise (questões 10-18) estão documentadas como comentários:
- **Questões 10-12**: Em `GerenciadorSessao.java`
- **Questões 13-15**: Em `IndiceInvertido.java`
- **Questões 16-18**: Em `RateLimiter.java`

## Conceitos-chave implementados

### Tabelas Hash
- ✅ Funções hash: `h(k) = k.hashCode() % M`
- ✅ Contrato `hashCode()/equals()`
- ✅ Encadeamento separado (chaining)
- ✅ Endereçamento aberto (open addressing)
- ✅ Sondagem linear: h(k, i) = (h(k) + i) % M
- ✅ Sondagem quadrática: h(k, i) = (h(k) + i²) % M
- ✅ Double hashing: h(k, i) = (h1(k) + i × h2(k)) % M
- ✅ Rehashing: dobrando capacidade
- ✅ Lazy deletion com tombstones
- ✅ Fator de carga λ = n/M

### Estruturas de Dados Utilizadas
- `LinkedList<>[]` - para encadeamento separado
- `Object[]` - para endereçamento aberto
- `HashMap<>` - para índice invertido
- `Map.Entry` - para iteração segura

### Complexidades
- Operações com λ ≤ 0.75: O(1) amortizado
- Rehashing: O(n)
- Busca, inserção, remoção no caso médio: O(1)

## Arquivos de Resposta Teórica

As respostas às questões de análise estão incluídas como comentários nos arquivos:

1. [Exercicio12Respostas.java](src/main/java/bloco1/Exercicio12Respostas.java) - Questões 1-4
2. [Exercicio21Respostas.java](src/main/java/bloco2/Exercicio21Respostas.java) - Questões 5-6
3. [Exercicio22Respostas.java](src/main/java/bloco2/Exercicio22Respostas.java) - Cálculos e inserções
4. [GerenciadorSessao.java](src/main/java/bloco3/GerenciadorSessao.java) - Questões 10-12
5. [IndiceInvertido.java](src/main/java/bloco3/IndiceInvertido.java) - Questões 13-15
6. [RateLimiter.java](src/main/java/bloco3/RateLimiter.java) - Questões 16-18

## Testes Unitários

Total de 42 testes JUnit 5:
- **Bloco 1**: 8 testes (TabelaHashEncadeadaTest)
- **Bloco 2**: 7 testes (TabelaHashAbertaTest)
- **Bloco 3**: 27 testes
  - GerenciadorSessaoTest: 8 testes
  - IndiceInvertidoTest: 10 testes
  - RateLimiterTest: 9 testes

## Notas Importantes

1. **Imutabilidade de Chaves**: As chaves não devem mudar após inserção
2. **hashCode() e equals()**: Devem ser consistentes
3. **Fator de Carga**: Manter λ ≤ 0.75 para encadeamento, λ ≤ 0.70 para endereçamento aberto
4. **Null Handling**: Chaves null são explicitamente rejeitadas
5. **Thread Safety**: Implementações NÃO são thread-safe (como esperado)

## Referências

- CORMEN, T. H. et al. *Introdução a algoritmos*. 4. ed. 2024. Cap. 11.
- GOODRICH, M. T.; TAMASSIA, R. *Estruturas de dados e algoritmos em Java*. 6. ed. 2013.
- BLOCH, J. *Java Efetivo*. 3. ed. 2019. Item 11.
- Oracle Java Documentation - HashMap, LinkedHashMap

## Licença

Material educacional da disciplina de Estruturas de Dados - IFMTM

---

**Data**: Maio 2026
**Disciplina**: Estruturas de Dados
**Curso**: Tecnologia em Sistemas para Internet
