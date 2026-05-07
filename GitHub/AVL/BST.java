import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Árvore Binária de Busca (BST) genérica com Comparator<T> injetado no construtor.
 * Serve como classe base para a ArvoreAVL.
 *
 * O Comparator define a regra de ordenação sem exigir que T implemente Comparable.
 * Isso permite usar qualquer critério externo (ex: ordenar Produto por nome, por preço, etc.)
 */
public class BST<T> {

    // A raiz é 'protected' para que a subclasse AVL possa acessá-la diretamente.
    protected No<T> raiz;

    // O comparador é 'protected' para que a AVL também possa utilizá-lo nas buscas.
    protected Comparator<T> comparador;

    // =========================================================
    // CONSTRUTOR
    // =========================================================

    public BST(Comparator<T> comparador) {
        this.raiz = null;
        this.comparador = comparador;
    }

    // =========================================================
    // INSERÇÃO
    // =========================================================

    public void inserir(T valor) {
        raiz = inserirRec(raiz, valor);
    }

    protected No<T> inserirRec(No<T> no, T valor) {
        if (no == null) return new No<>(valor);

        int cmp = comparador.compare(valor, no.dado);
        if (cmp < 0)      no.esquerdo = inserirRec(no.esquerdo, valor);
        else if (cmp > 0) no.direito  = inserirRec(no.direito,  valor);
        // cmp == 0: duplicata ignorada

        return no;
    }

    // =========================================================
    // BUSCA
    // =========================================================

    public boolean contem(T valor) {
        return buscarRec(raiz, valor) != null;
    }

    private No<T> buscarRec(No<T> no, T valor) {
        if (no == null) return null;
        int cmp = comparador.compare(valor, no.dado);
        if (cmp < 0) return buscarRec(no.esquerdo, valor);
        if (cmp > 0) return buscarRec(no.direito,  valor);
        return no; // encontrado
    }

    // =========================================================
    // REMOÇÃO
    // =========================================================

    public void remover(T valor) {
        raiz = removerRec(raiz, valor);
    }

    protected No<T> removerRec(No<T> no, T valor) {
        if (no == null) return null;

        int cmp = comparador.compare(valor, no.dado);
        if (cmp < 0) {
            no.esquerdo = removerRec(no.esquerdo, valor);
        } else if (cmp > 0) {
            no.direito = removerRec(no.direito, valor);
        } else {
            // Nó encontrado — três casos possíveis:
            if (no.esquerdo == null) return no.direito; // 0 ou 1 filho (direito)
            if (no.direito  == null) return no.esquerdo; // 1 filho (esquerdo)

            // Dois filhos: substituir pelo sucessor in-order (menor da subárvore direita)
            No<T> sucessor = no.direito;
            while (sucessor.esquerdo != null) sucessor = sucessor.esquerdo;
            no.dado = sucessor.dado;
            no.direito = removerRec(no.direito, sucessor.dado);
        }
        return no;
    }

    // =========================================================
    // PERCURSO EM ORDEM (In-Order): esq → raiz → dir
    // Garante saída ordenada em uma BST/AVL válida.
    // =========================================================

    public List<T> inOrder() {
        List<T> resultado = new ArrayList<>();
        inOrderRec(raiz, resultado);
        return resultado;
    }

    private void inOrderRec(No<T> no, List<T> lista) {
        if (no == null) return;
        inOrderRec(no.esquerdo, lista);
        lista.add(no.dado);
        inOrderRec(no.direito, lista);
    }
}
