import java.util.Comparator;
import java.util.List;

/**
 * Classe principal de demonstração e testes manuais da ArvoreAVL<T>.
 *
 * Testa:
 *  1. AVL com Integer (para verificar rotações com números simples)
 *  2. AVL com Produto ordenado por nome (requisito do desafio)
 *  3. AVL com Produto ordenado por preço (demonstra flexibilidade do Comparator)
 */
public class Main {

    public static void main(String[] args) {
        testarComInteiros();
        System.out.println("\n" + "=".repeat(60) + "\n");
        testarComProdutoPorNome();
        System.out.println("\n" + "=".repeat(60) + "\n");
        testarComProdutoPorPreco();
        System.out.println("\n" + "=".repeat(60) + "\n");
        testarRotacoes();
    }

    // =========================================================
    // TESTE 1: AVL com inteiros — verifica ordem e altura
    // =========================================================

    static void testarComInteiros() {
        System.out.println(">>> TESTE 1: AVL com Integer (inserção em ordem crescente)\n");

        // Comparator para inteiros em ordem natural
        ArvoreAVL<Integer> avl = new ArvoreAVL<>(Comparator.<Integer>naturalOrder());

        // Inserindo em ordem crescente — em uma BST pura, isso geraria uma lista (degenerada)
        for (int i = 1; i <= 15; i++) {
            avl.inserir(i);
        }

        List<Integer> resultado = avl.inOrder();
        System.out.println("In-order (deve estar ordenado de 1 a 15): " + resultado);
        System.out.println("Altura da AVL: " + avl.alturaAVL()
                + " (BST degenerada teria altura 14; AVL deve ter ≤ 5)");

        // Verificação: a apostila garante h <= ceil(1.44 * log2(n+2))
        boolean alturaOk = avl.alturaAVL() <= 5;
        System.out.println("Altura dentro do limite AVL? " + (alturaOk ? "SIM ✓" : "NÃO ✗"));
    }

    // =========================================================
    // TESTE 2: AVL com Produto ordenado por nome (requisito do desafio)
    // =========================================================

    static void testarComProdutoPorNome() {
        System.out.println(">>> TESTE 2: AVL com Produto ordenado por NOME\n");

        // Comparator que ordena Produto pelo nome em ordem alfabética
        Comparator<Produto> porNome = Comparator.comparing(Produto::getNome);

        ArvoreAVL<Produto> avl = new ArvoreAVL<>(porNome);

        // Inserindo produtos em ordem aleatória de nome
        avl.inserir(new Produto("Notebook",    3500.00, 10));
        avl.inserir(new Produto("Mouse",         89.90, 50));
        avl.inserir(new Produto("Teclado",       149.90, 30));
        avl.inserir(new Produto("Monitor",      1200.00, 15));
        avl.inserir(new Produto("Headset",       299.90, 25));
        avl.inserir(new Produto("Webcam",        199.90, 20));
        avl.inserir(new Produto("HD Externo",    350.00, 12));
        avl.inserir(new Produto("Pendrive",       35.90, 100));

        System.out.println("Produtos em ordem ALFABÉTICA (in-order):");
        List<Produto> emOrdem = avl.inOrder();
        for (Produto p : emOrdem) {
            System.out.println("  " + p);
        }

        System.out.println("\nAltura da AVL com 8 produtos: " + avl.alturaAVL()
                + " (esperado ≤ 4)");

        // Teste de busca
        Produto buscado = new Produto("Mouse", 0, 0); // só o nome importa para o Comparator
        System.out.println("\nBuscar 'Mouse': " + (avl.contem(buscado) ? "ENCONTRADO ✓" : "NÃO ENCONTRADO ✗"));

        Produto ausente = new Produto("Impressora", 0, 0);
        System.out.println("Buscar 'Impressora': " + (avl.contem(ausente) ? "ENCONTRADO ✗" : "NÃO ENCONTRADO ✓"));

        // Teste de remoção
        System.out.println("\nRemovendo 'Teclado'...");
        avl.remover(new Produto("Teclado", 0, 0));
        System.out.println("Buscar 'Teclado' após remoção: "
                + (avl.contem(new Produto("Teclado", 0, 0)) ? "ENCONTRADO ✗" : "NÃO ENCONTRADO ✓"));
        System.out.println("In-order após remoção:");
        avl.inOrder().forEach(p -> System.out.println("  " + p));
    }

    // =========================================================
    // TESTE 3: mesmos produtos, mas ordenados por preço
    // Demonstra que só mudar o Comparator muda o critério de ordenação
    // =========================================================

    static void testarComProdutoPorPreco() {
        System.out.println(">>> TESTE 3: mesmos dados, Comparator por PREÇO\n");

        Comparator<Produto> porPreco = Comparator.comparingDouble(Produto::getPreco);

        ArvoreAVL<Produto> avl = new ArvoreAVL<>(porPreco);

        avl.inserir(new Produto("Notebook",   3500.00, 10));
        avl.inserir(new Produto("Mouse",        89.90, 50));
        avl.inserir(new Produto("Teclado",     149.90, 30));
        avl.inserir(new Produto("Monitor",    1200.00, 15));
        avl.inserir(new Produto("Headset",     299.90, 25));
        avl.inserir(new Produto("Webcam",      199.90, 20));
        avl.inserir(new Produto("HD Externo",  350.00, 12));
        avl.inserir(new Produto("Pendrive",     35.90, 100));

        System.out.println("Produtos em ordem de PREÇO (in-order):");
        avl.inOrder().forEach(p -> System.out.println("  " + p));
    }

    // =========================================================
    // TESTE 4: verificação explícita das rotações (apostila, seção 6)
    // =========================================================

    static void testarRotacoes() {
        System.out.println(">>> TESTE 4: Rotações explícitas com Integer\n");

        ArvoreAVL<Integer> avl;

        // --- Caso LL: inserir 30 → 20 → 10 ---
        // Sem AVL: 30 → 20 (esq) → 10 (esq de 20) — totalmente à esquerda
        // Com AVL: rotação direita → raiz vira 20, filhos 10 e 30
        avl = new ArvoreAVL<>(Comparator.<Integer>naturalOrder());
        avl.inserir(30); avl.inserir(20); avl.inserir(10);
        System.out.println("Caso LL (30→20→10):");
        System.out.println("  In-order: " + avl.inOrder() + " | Altura: " + avl.alturaAVL());
        System.out.println("  Raiz esperada = 20, altura esperada = 1");
        System.out.println("  Raiz atual    = " + avl.raiz.dado + ", altura atual = " + avl.alturaAVL());

        System.out.println();

        // --- Caso RR: inserir 10 → 20 → 30 ---
        avl = new ArvoreAVL<>(Comparator.<Integer>naturalOrder());
        avl.inserir(10); avl.inserir(20); avl.inserir(30);
        System.out.println("Caso RR (10→20→30):");
        System.out.println("  In-order: " + avl.inOrder() + " | Altura: " + avl.alturaAVL());
        System.out.println("  Raiz esperada = 20, altura esperada = 1");
        System.out.println("  Raiz atual    = " + avl.raiz.dado + ", altura atual = " + avl.alturaAVL());

        System.out.println();

        // --- Caso LR: inserir 30 → 10 → 20 ---
        avl = new ArvoreAVL<>(Comparator.<Integer>naturalOrder());
        avl.inserir(30); avl.inserir(10); avl.inserir(20);
        System.out.println("Caso LR (30→10→20):");
        System.out.println("  In-order: " + avl.inOrder() + " | Altura: " + avl.alturaAVL());
        System.out.println("  Raiz esperada = 20, altura esperada = 1");
        System.out.println("  Raiz atual    = " + avl.raiz.dado + ", altura atual = " + avl.alturaAVL());

        System.out.println();

        // --- Caso RL: inserir 10 → 30 → 20 ---
        avl = new ArvoreAVL<>(Comparator.<Integer>naturalOrder());
        avl.inserir(10); avl.inserir(30); avl.inserir(20);
        System.out.println("Caso RL (10→30→20):");
        System.out.println("  In-order: " + avl.inOrder() + " | Altura: " + avl.alturaAVL());
        System.out.println("  Raiz esperada = 20, altura esperada = 1");
        System.out.println("  Raiz atual    = " + avl.raiz.dado + ", altura atual = " + avl.alturaAVL());
    }
}
