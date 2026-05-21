# ⚡ QUICK START - Exercícios de Tabelas Hash

## 🎯 5 Minutos para Começar

### 1. Compilar o Projeto
```bash
cd "c:\Users\artur\Área de Trabalho\5º Período\Estrutura de Dados\GitHub\ExerciciosHashTable"
mvn clean compile
```
✅ Esperado: Compilação sem erros

### 2. Rodar Todos os Testes
```bash
mvn test
```
✅ Esperado: `BUILD SUCCESS` com 47/47 testes passando

### 3. Explorar o Código
Abra em VS Code a pasta do projeto e navegue:
- **Bloco 1** → `src/main/java/bloco1/`
- **Bloco 2** → `src/main/java/bloco2/`
- **Bloco 3** → `src/main/java/bloco3/`

### 4. Ler Documentação
1. [PROJETO_CONCLUIDO.md](PROJETO_CONCLUIDO.md) - Sumário geral (você lê primeiro!)
2. [INDICE_COMPLETO.md](INDICE_COMPLETO.md) - Índice detalhado
3. [README.md](README.md) - Guia técnico

### 5. Estudar Respostas
- [RESPOSTAS_EXERCICIO_1_2.md](RESPOSTAS_EXERCICIO_1_2.md) - Encadeamento
- [RESPOSTAS_EXERCICIO_2_1.md](RESPOSTAS_EXERCICIO_2_1.md) - Sondagem
- [RESPOSTAS_EXERCICIO_2_2.md](RESPOSTAS_EXERCICIO_2_2.md) - Double Hashing

---

## 📂 O Que Você Vai Encontrar

### Código Implementado

#### 🔵 BLOCO 1 - Encadeamento Separado
```
bloco1/
├── Produto.java                   ← Classe com hashCode corrigido
├── Sessao.java                    ← Classe com hashCode distribuído
├── Coordenada.java                ← Classe com double seguro
├── Exercicio12Respostas.java      ← Respostas teóricas
└── TabelaHashEncadeada<K,V>       ← Implementação principal
```

#### 🟢 BLOCO 2 - Endereçamento Aberto
```
bloco2/
├── Exercicio21Respostas.java      ← Análise sondagem
├── Exercicio22Respostas.java      ← Análise double hashing
└── TabelaHashAberta<K,V>          ← Implementação principal
```

#### 🟡 BLOCO 3 - Aplicações Web
```
bloco3/
├── Usuario.java                   ← Classe suporte
├── GerenciadorSessao.java         ← Cache HTTP sessions
├── IndiceInvertido.java           ← Busca full-text
└── RateLimiter.java               ← Limite de requisições
```

### Testes (47 Total)
```
test/
├── bloco1/TabelaHashEncadeadaTest.java    (7 testes)
├── bloco2/TabelaHashAbertaTest.java       (7 testes)
├── bloco3/
│   ├── GerenciadorSessaoTest.java        (10 testes)
│   ├── IndiceInvertidoTest.java          (12 testes)
│   └── RateLimiterTest.java              (11 testes)
```

---

## 🔥 Executar Testes Específicos

### Teste uma classe individual
```bash
# Encadeamento separado
mvn test -Dtest=TabelaHashEncadeadaTest

# Endereçamento aberto
mvn test -Dtest=TabelaHashAbertaTest

# Cache de sessões
mvn test -Dtest=GerenciadorSessaoTest

# Índice invertido
mvn test -Dtest=IndiceInvertidoTest

# Rate limiting
mvn test -Dtest=RateLimiterTest
```

### Teste apenas um método
```bash
mvn test -Dtest=TabelaHashEncadeadaTest#testPutGet
mvn test -Dtest=TabelaHashAbertaTest#testRehashingDisparo
```

### Teste por bloco
```bash
mvn test -Dtest=bloco1.*
mvn test -Dtest=bloco2.*
mvn test -Dtest=bloco3.*
```

---

## 💻 Usar as Classes no Seu Código

### TabelaHashEncadeada (Bloco 1)
```java
import bloco1.TabelaHashEncadeada;

// Criar tabela
TabelaHashEncadeada<String, Integer> tabela = new TabelaHashEncadeada<>();

// Usar
tabela.put("alice", 1001);
tabela.put("bob", 1002);

System.out.println(tabela.get("alice"));  // 1001
tabela.remove("bob");
```

### TabelaHashAberta (Bloco 2)
```java
import bloco2.TabelaHashAberta;

// Criar tabela
TabelaHashAberta<String, String> tabela = new TabelaHashAberta<>();

// Usar
tabela.put("Produto1", "Teclado");
tabela.put("Produto2", "Mouse");

System.out.println(tabela.get("Produto1"));  // Teclado
```

### GerenciadorSessao (Bloco 3.1)
```java
import bloco3.GerenciadorSessao;
import bloco3.Usuario;

// Usar
GerenciadorSessao gerenciador = new GerenciadorSessao();
Usuario usuario = new Usuario(1, "admin");

gerenciador.registrar("token_xyz", usuario);
Usuario recuperado = gerenciador.buscar("token_xyz");

if (gerenciador.eValido("token_xyz")) {
    System.out.println("Sessão ativa!");
}
```

### IndiceInvertido (Bloco 3.2)
```java
import bloco3.IndiceInvertido;

// Usar
IndiceInvertido indice = new IndiceInvertido();

indice.indexar(1, "Cadeira ergonômica");
indice.indexar(2, "Mesa de escritório");
indice.indexar(3, "Cadeira gamer");

List<Long> resultados = indice.buscar("cadeira");
// [1, 3]

List<Long> multi = indice.buscarMultiplas("cadeira gamer");
// [3]
```

### RateLimiter (Bloco 3.3)
```java
import bloco3.RateLimiter;

// Usar
RateLimiter limiter = new RateLimiter();

if (limiter.permitir("192.168.1.1")) {
    System.out.println("Requisição autorizada!");
} else {
    System.out.println("Limite excedido!");
}

System.out.println(limiter.contadorIP("192.168.1.1"));
```

---

## 📚 Documentação Disponível

| Arquivo | Conteúdo | Tempo de Leitura |
|---------|----------|---|
| PROJETO_CONCLUIDO.md | Sumário executivo | 5 min |
| INDICE_COMPLETO.md | Índice detalhado | 10 min |
| ESTRUTURA_DO_PROJETO.md | Estrutura e organização | 5 min |
| README.md | Guia técnico | 10 min |
| RESPOSTAS_EXERCICIO_1_2.md | Q1-4 encadeamento | 10 min |
| RESPOSTAS_EXERCICIO_2_1.md | Q5-6 sondagem | 8 min |
| RESPOSTAS_EXERCICIO_2_2.md | Q7 double hashing | 5 min |

---

## 🧪 Teste de Validação (5 minutos)

Execute este script para validar tudo:

```bash
# 1. Compilar
mvn clean compile

# 2. Rodar testes
mvn test -q

# 3. Verificar resultado
echo "Tudo pronto!"
```

Se ver `BUILD SUCCESS`, projeto está 100% funcional! ✅

---

## 📊 Estatísticas Rápidas

```
💾 Tamanho do código:       ~1.500 linhas
📦 Classes implementadas:   13
🧪 Testes:                 47 (100% pass)
📄 Documentação:           6 arquivos
⏱️  Tempo compilação:       <5 segundos
⏱️  Tempo testes:           <10 segundos
```

---

## 🎓 Roteiro de Aprendizado (Sugerido)

### Dia 1 - Fundamentação
1. Leia [RESPOSTAS_EXERCICIO_1_2.md](RESPOSTAS_EXERCICIO_1_2.md)
2. Estude [TabelaHashEncadeada.java](src/main/java/bloco1/TabelaHashEncadeada.java)
3. Execute `mvn test -Dtest=TabelaHashEncadeadaTest`

### Dia 2 - Endereçamento Aberto
1. Leia [RESPOSTAS_EXERCICIO_2_1.md](RESPOSTAS_EXERCICIO_2_1.md)
2. Leia [RESPOSTAS_EXERCICIO_2_2.md](RESPOSTAS_EXERCICIO_2_2.md)
3. Estude [TabelaHashAberta.java](src/main/java/bloco2/TabelaHashAberta.java)
4. Execute `mvn test -Dtest=TabelaHashAbertaTest`

### Dia 3 - Aplicações Reais
1. Estude [GerenciadorSessao.java](src/main/java/bloco3/GerenciadorSessao.java)
2. Estude [IndiceInvertido.java](src/main/java/bloco3/IndiceInvertido.java)
3. Estude [RateLimiter.java](src/main/java/bloco3/RateLimiter.java)
4. Execute `mvn test -Dtest=bloco3.*`

---

## ❓ Perguntas Frequentes

### P: Como saber se compilou certo?
R: Execute `mvn clean compile`. Se não tiver erros em vermelho, está OK!

### P: Todos os testes devem passar?
R: Sim! 47/47 devem passar. Se algum falhar, há bug no código.

### P: Posso usar as classes em meu projeto?
R: Sim! Copie a classe que precisa e use normalmente.

### P: Como corrigir erro de compilação?
R: Execute `mvn clean compile` novamente. Se persistir, abra issue.

### P: Preciso de IDE especial?
R: Não! Qualquer editor com Maven funciona. VS Code, IntelliJ, Eclipse...

---

## 🚀 Próximas Ações

- [ ] Compilar projeto (`mvn clean compile`)
- [ ] Rodar todos os testes (`mvn test`)
- [ ] Ler PROJETO_CONCLUIDO.md
- [ ] Ler INDICE_COMPLETO.md
- [ ] Estudar TabelaHashEncadeada.java
- [ ] Estudar TabelaHashAberta.java
- [ ] Estudar aplicações (GerenciadorSessao, etc)
- [ ] Fazer backup do projeto
- [ ] Enviar para Git
- [ ] Compartilhar com professor

---

## 📞 Resumo dos Comandos

```bash
# Compilar
mvn clean compile

# Testes (todos)
mvn test

# Testes (bloco 1)
mvn test -Dtest=bloco1.*

# Testes (bloco 2)
mvn test -Dtest=bloco2.*

# Testes (bloco 3)
mvn test -Dtest=bloco3.*

# Testes (classe específica)
mvn test -Dtest=TabelaHashEncadeadaTest

# Limpar build
mvn clean

# Compilar sem testes
mvn clean compile -DskipTests
```

---

## ✨ Destaques

✅ Código compilável  
✅ 47 testes passando  
✅ Documentação completa  
✅ Pronto para entrega  
✅ Fácil de usar  
✅ Bem comentado  
✅ Organizadíssimo  
✅ Tudo que precisa  

---

**Começar agora! ⚡**

Execute na pasta do projeto:
```bash
mvn test
```

Viu `BUILD SUCCESS`? 🎉 Então tá tudo certo!

---

**Last Updated:** Maio 2026  
**Status:** ✅ COMPLETO E FUNCIONAL
