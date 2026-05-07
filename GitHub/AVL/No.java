/**
 * Nó genérico da BST base.
 * Armazena um dado do tipo T e referências para os filhos esquerdo e direito.
 */
public class No<T> {
    T dado;
    No<T> esquerdo;
    No<T> direito;

    public No(T dado) {
        this.dado = dado;
        this.esquerdo = null;
        this.direito = null;
    }
}
