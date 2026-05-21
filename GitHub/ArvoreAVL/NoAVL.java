public class NoAVL<T> {

    T dado;

    NoAVL<T> esquerdo;
    NoAVL<T> direito;

    //cada nó armazena sua altura
    int altura;

    public NoAVL(T dado) {
        this.dado = dado;
        this.altura = 0;
    }
}