import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AVL<T> {

    private NoAVL<T> raiz;

    private Comparator<T> comparator;

    public AVL(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    // INSERÇÃO
    public void inserir(T valor) {
        raiz = inserirRecursivo(raiz, valor);
    }

    private NoAVL<T> inserirRecursivo(NoAVL<T> no, T valor) {

        // Inserção normal BST
        if (no == null) {
            return new NoAVL<>(valor);
        }

        int cmp = comparator.compare(valor, no.dado);

        if (cmp < 0) {
            no.esquerdo = inserirRecursivo(no.esquerdo, valor);

        } else if (cmp > 0) {
            no.direito = inserirRecursivo(no.direito, valor);

        } else {
            // duplicados ignorados
            return no;
        }

        // Atualiza altura
        atualizarAltura(no);

        // Balanceia
        return balancear(no);
    }

    // BUSCA
    public boolean contem(T valor) {
        return buscar(raiz, valor);
    }

    private boolean buscar(NoAVL<T> no, T valor) {

        if (no == null) {
            return false;
        }

        int cmp = comparator.compare(valor, no.dado);

        if (cmp == 0) {
            return true;
        }

        if (cmp < 0) {
            return buscar(no.esquerdo, valor);
        }

        return buscar(no.direito, valor);
    }

    // BALANCEAMENTO
    private NoAVL<T> balancear(NoAVL<T> no) {

        int fator = fatorBalanceamento(no);

        // Caso ESQUERDA-ESQUERDA
        if (fator > 1 && fatorBalanceamento(no.esquerdo) >= 0) {
            return rotacaoDireita(no);
        }

        // Caso DIREITA-DIREITA
        if (fator < -1 && fatorBalanceamento(no.direito) <= 0) {
            return rotacaoEsquerda(no);
        }

        // Caso ESQUERDA-DIREITA
        if (fator > 1 && fatorBalanceamento(no.esquerdo) < 0) {

            no.esquerdo = rotacaoEsquerda(no.esquerdo);

            return rotacaoDireita(no);
        }

        // Caso DIREITA-ESQUERDA
        if (fator < -1 && fatorBalanceamento(no.direito) > 0) {

            no.direito = rotacaoDireita(no.direito);

            return rotacaoEsquerda(no);
        }

        return no;
    }

    // ROTAÇÕES
    private NoAVL<T> rotacaoDireita(NoAVL<T> y) {

        NoAVL<T> x = y.esquerdo;

        NoAVL<T> t2 = x.direito;

        x.direito = y;

        y.esquerdo = t2;

        atualizarAltura(y);

        atualizarAltura(x);

        return x;
    }

    private NoAVL<T> rotacaoEsquerda(NoAVL<T> x) {

        NoAVL<T> y = x.direito;

        NoAVL<T> t2 = y.esquerdo;

        y.esquerdo = x;

        x.direito = t2;

        atualizarAltura(x);

        atualizarAltura(y);

        return y;
    }

    // ALTURA E FATOR
    private void atualizarAltura(NoAVL<T> no) {

        no.altura = 1 + Math.max(
                altura(no.esquerdo),
                altura(no.direito)
        );
    }

    private int altura(NoAVL<T> no) {

        if (no == null) {
            return -1;
        }

        return no.altura;
    }

    private int fatorBalanceamento(NoAVL<T> no) {

        if (no == null) {
            return 0;
        }

        return altura(no.esquerdo) - altura(no.direito);
    }

    // PERCURSO IN-ORDER
    public List<T> inOrder() {

        List<T> resultado = new ArrayList<>();

        inOrderRecursivo(raiz, resultado);

        return resultado;
    }

    private void inOrderRecursivo(NoAVL<T> no, List<T> resultado) {

        if (no == null) {
            return;
        }

        inOrderRecursivo(no.esquerdo, resultado);

        resultado.add(no.dado);

        inOrderRecursivo(no.direito, resultado);
    }

    // ALTURA DA ÁRVORE
    public int altura() {
        return altura(raiz);
    }
}