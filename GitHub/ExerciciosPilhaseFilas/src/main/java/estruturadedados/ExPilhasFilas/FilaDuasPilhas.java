package estruturadedados.ExPilhasFilas;

public class FilaDuasPilhas<T> implements Fila<T> {

    private final PilhaArray<T> entrada = new PilhaArray<>();
    private final PilhaArray<T> saida = new PilhaArray<>();

    @Override
    // enqueue: sempre empilha em entrada. O(1).
    public void enqueue(T elemento) {
        entrada.push(elemento); // sempre empilha em entrada
    }

    @Override
    public T dequeue() {
        transferirSeNecessario();
        if (saida.isEmpty())
            throw new java.util.NoSuchElementException();
        return saida.pop();
    }

    @Override
    public T peek() {
        transferirSeNecessario();
        if (saida.isEmpty())
            throw new java.util.NoSuchElementException();
        return saida.peek();
    }

    @Override
    public boolean isEmpty() {
        return entrada.isEmpty() && saida.isEmpty();
    }

    @Override
    public int size() {
        return entrada.size() + saida.size();
    }

    // Transferência: move todos de entrada para saida (inverte a ordem)
    private void transferirSeNecessario() {
        if (saida.isEmpty()) {
            while (!entrada.isEmpty()) {
                saida.push(entrada.pop());
            }
        }
    }
}


// Pior Caso: Quando a saida está vazia e precisamos mover n elementos, o custo é O(n)
// Caso Amortizado: Na média, cada elemento é inserido uma vez, movido uma vez e removido uma vez. 
// O custo médio por operação acaba sendo O(1)