package exercicioQuatro;

public class ContainerSimples<T> implements Container<T> {
    private Object[] elementos;
    private int tamanho = 0;

    ContainerSimples(int capacidade) {
        this.elementos = new Object[capacidade];
    }

    @Override
    public void adicionar(T item) {
        // Se atingir a capacidade máxima, dobramos o tamanho do array
        if (tamanho == elementos.length) {
            redimensionar();
        }
        elementos[tamanho++] = item;
    }

    private void redimensionar() {
        int novaCapacidade = elementos.length * 2;
        Object[] novoArray = new Object[novaCapacidade];

        for (int i = 0; i < elementos.length; i++) {
            novoArray[i] = elementos[i];
        }

    // Substituindo a referência antiga pela nova
    this.elementos = novoArray;
    System.out.println("Capacidade aumentada para: " + novaCapacidade);
}

    @Override
    @SuppressWarnings("unchecked") // Suprime o aviso de cast inseguro
    public T remover() {
        if (estaVazio())
            return null;
        T item = (T) elementos[--tamanho];
        elementos[tamanho] = null;
        return item;
    }

    @Override
    public int tamanho() {
        return tamanho;
    }

    @Override
    public boolean estaVazio() {
        return tamanho == 0;
    }
}