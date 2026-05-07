import java.util.Comparator;

/**
 * Árvore AVL genérica com Comparator<T> injetado no construtor.
 *
 * Estende BST<T> e adiciona balanceamento automático após cada inserção e remoção.
 * A invariante AVL garante |FB(nó)| <= 1 para todo nó, mantendo altura O(log n).
 *
 * DESAFIO DA SEMANA 9 — Apostila: Estruturas Não Lineares — Árvores AVL
 */
public class ArvoreAVL<T> extends BST<T> {

    // =========================================================
    // NÓ AVL — estende No<T> adicionando o campo 'altura'
    // =========================================================

    /**
     * NoAVL herda dado, esquerdo e direito de No<T>.
     * O campo 'altura' é o que diferencia um nó AVL de um nó BST comum.
     *
     * CONVENÇÃO DA APOSTILA: nó folha tem altura 0.
     * (Nó nulo retorna -1, assim: altura(folha) = 1 + max(-1, -1) = 0)
     */
    private static class NoAVL<T> extends No<T> {
        int altura;

        NoAVL(T dado) {
            super(dado);
            this.altura = 0; // folha recém-criada começa com altura 0
        }
    }

    // =========================================================
    // CONSTRUTOR
    // =========================================================

    /**
     * Cria uma AVL usando o Comparator fornecido.
     * O Comparator define a regra de ordenação sem exigir Comparable<T>.
     */
    public ArvoreAVL(Comparator<T> comparador) {
        super(comparador); // repassa o comparador para a BST base
    }

    // =========================================================
    // UTILITÁRIOS: ALTURA E FATOR DE BALANCEAMENTO
    // =========================================================

    /**
     * Retorna a altura de um nó.
     * Retorna -1 para nó nulo — convenção da apostila.
     * Isso simplifica o cálculo: altura(folha) = 1 + max(-1, -1) = 0.
     */
    private int altura(No<T> no) {
        if (no == null) return -1;
        return ((NoAVL<T>) no).altura;
    }

    /**
     * Recalcula e atualiza a altura de um nó com base em seus filhos.
     * DEVE ser chamado de baixo para cima (filho antes do pai).
     */
    private void atualizarAltura(NoAVL<T> no) {
        no.altura = 1 + Math.max(altura(no.esquerdo), altura(no.direito));
    }

    /**
     * Fator de Balanceamento (FB) = altura(esquerdo) - altura(direito).
     *
     * FB > 1  → subárvore esquerda alta demais → rotação à direita
     * FB < -1 → subárvore direita alta demais  → rotação à esquerda
     * -1 ≤ FB ≤ 1 → árvore balanceada (invariante AVL satisfeita)
     */
    private int fb(No<T> no) {
        if (no == null) return 0;
        return altura(no.esquerdo) - altura(no.direito);
    }

    // =========================================================
    // AS QUATRO ROTAÇÕES
    // =========================================================

    /**
     * ROTAÇÃO SIMPLES À DIREITA — Caso LL (Left-Left)
     *
     * Situação: FB(z) = +2 e FB(z.esquerdo) >= 0
     * O desequilíbrio veio da subárvore esquerda do filho esquerdo.
     *
     * Antes:            Depois:
     *      z                  y
     *     / \               /   \
     *    y   T4            x     z
     *   / \               / \   / \
     *  x   T3            T1 T2 T3  T4
     * / \
     *T1  T2
     *
     * PASSO A PASSO:
     * 1. y (filho esquerdo de z) sobe
     * 2. z desce para se tornar filho direito de y
     * 3. T3 (antigo filho direito de y) passa a ser filho esquerdo de z
     * 4. Atualiza altura de z primeiro (desceu), depois y (subiu)
     */
    private No<T> rotacionarDireita(NoAVL<T> z) {
        NoAVL<T> y = (NoAVL<T>) z.esquerdo; // y é o filho esquerdo que vai subir
        No<T>    T3 = y.direito;             // T3 é o "filho do meio" que vai mudar de lugar

        y.direito  = z;  // y sobe: z passa a ser filho direito de y
        z.esquerdo = T3; // T3 passa para z (antigo lugar de y)

        // ATENÇÃO: atualizar z antes de y (apostila, seção Armadilhas Clássicas)
        atualizarAltura(z); // z desceu → sua altura muda primeiro
        atualizarAltura(y); // y subiu  → depende da nova altura de z

        return y; // y é agora a nova raiz desta sub-árvore
    }

    /**
     * ROTAÇÃO SIMPLES À ESQUERDA — Caso RR (Right-Right)
     *
     * Situação: FB(z) = -2 e FB(z.direito) <= 0
     * O desequilíbrio veio da subárvore direita do filho direito.
     *
     * Antes:            Depois:
     *    z                   y
     *   / \                /   \
     *  T1   y             z     x
     *      / \           / \   / \
     *     T2  x         T1 T2 T3  T4
     *        / \
     *       T3  T4
     *
     * Simétrico à rotação à direita.
     */
    private No<T> rotacionarEsquerda(NoAVL<T> z) {
        NoAVL<T> y = (NoAVL<T>) z.direito; // y é o filho direito que vai subir
        No<T>    T2 = y.esquerdo;           // T2 é o "filho do meio"

        y.esquerdo = z;  // y sobe
        z.direito  = T2; // T2 passa para z

        atualizarAltura(z); // z desceu primeiro
        atualizarAltura(y); // y subiu depois

        return y;
    }

    // =========================================================
    // REBALANCEAMENTO — detecta o caso e aplica a rotação certa
    // =========================================================

    /**
     * Verifica se o nó está desbalanceado e aplica a rotação adequada.
     * Chamado na VOLTA da recursão, de baixo para cima na árvore.
     *
     * Os quatro casos possíveis:
     *
     * CASO LL: FB > 1 e filho esquerdo não pende para direita
     *   → Rotação simples à direita
     *
     * CASO LR: FB > 1 e filho esquerdo pende para direita
     *   → Rotação esquerda no filho, depois rotação direita na raiz
     *
     * CASO RR: FB < -1 e filho direito não pende para esquerda
     *   → Rotação simples à esquerda
     *
     * CASO RL: FB < -1 e filho direito pende para esquerda
     *   → Rotação direita no filho, depois rotação esquerda na raiz
     */
    private No<T> rebalancear(NoAVL<T> no) {
        atualizarAltura(no);       // recalcula a altura após a inserção/remoção
        int fatorBalanco = fb(no); // calcula o FB atual

        // ── CASO LL ──────────────────────────────────────────────────────────────
        if (fatorBalanco > 1 && fb(no.esquerdo) >= 0)
            return rotacionarDireita(no);

        // ── CASO LR ──────────────────────────────────────────────────────────────
        if (fatorBalanco > 1 && fb(no.esquerdo) < 0) {
            no.esquerdo = rotacionarEsquerda((NoAVL<T>) no.esquerdo); // 1ª rotação
            return rotacionarDireita(no);                              // 2ª rotação
        }

        // ── CASO RR ──────────────────────────────────────────────────────────────
        if (fatorBalanco < -1 && fb(no.direito) <= 0)
            return rotacionarEsquerda(no);

        // ── CASO RL ──────────────────────────────────────────────────────────────
        if (fatorBalanco < -1 && fb(no.direito) > 0) {
            no.direito = rotacionarDireita((NoAVL<T>) no.direito); // 1ª rotação
            return rotacionarEsquerda(no);                         // 2ª rotação
        }

        return no; // |FB| <= 1 → já está balanceado, nada a fazer
    }

    // =========================================================
    // INSERÇÃO AVL
    // =========================================================

    /**
     * Sobrescreve a inserção da BST para adicionar rebalanceamento.
     */
    @Override
    public void inserir(T valor) {
        raiz = inserirAVL(raiz, valor);
    }

    private No<T> inserirAVL(No<T> no, T valor) {
        // PASSO 1: Inserção BST padrão (desce na árvore igual à BST)
        if (no == null) return new NoAVL<>(valor); // cria nó folha

        int cmp = comparador.compare(valor, no.dado);
        if (cmp < 0)      no.esquerdo = inserirAVL(no.esquerdo, valor);
        else if (cmp > 0) no.direito  = inserirAVL(no.direito,  valor);
        else return no; // duplicata: ignora

        // PASSO 2: Rebalancear na VOLTA da recursão (de baixo para cima)
        return rebalancear((NoAVL<T>) no);
    }

    // =========================================================
    // REMOÇÃO AVL
    // =========================================================

    /**
     * Sobrescreve a remoção da BST para adicionar rebalanceamento.
     */
    @Override
    public void remover(T valor) {
        raiz = removerAVL(raiz, valor);
    }

    private No<T> removerAVL(No<T> no, T valor) {
        if (no == null) return null; // valor não encontrado

        int cmp = comparador.compare(valor, no.dado);
        if (cmp < 0) {
            no.esquerdo = removerAVL(no.esquerdo, valor);
        } else if (cmp > 0) {
            no.direito = removerAVL(no.direito, valor);
        } else {
            // Nó encontrado — mesma lógica da BST:
            if (no.esquerdo == null) return no.direito;
            if (no.direito  == null) return no.esquerdo;

            // Dois filhos: encontra o sucessor in-order (menor da subárvore direita)
            No<T> minDir = no.direito;
            while (minDir.esquerdo != null) minDir = minDir.esquerdo;
            no.dado = minDir.dado; // substitui o dado
            no.direito = removerAVL(no.direito, minDir.dado); // remove o sucessor
        }

        // Rebalancear na volta da recursão
        return rebalancear((NoAVL<T>) no);
    }

    // =========================================================
    // UTILIDADE: ALTURA PÚBLICA (usada nos testes)
    // =========================================================

    /** Retorna a altura da raiz da AVL (-1 se vazia). */
    public int alturaAVL() {
        return altura(raiz);
    }
}
