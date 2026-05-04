package estruturadedados.ExPilhasFilas;

import java.util.EmptyStackException;

public class PilhaLista<T> implements Pilha<T> {

    private Node<T> topo;      // referência para o nó no topo
    private int quantidade;

    public PilhaLista() {
        topo = null;
        quantidade = 0;
    }

    // ── push: cria nó cujo .proximo aponta para o topo atual ─────────
    @Override   // Θ(1) — sem amortização, sem redimensionamento
    public void push(T elemento) {
        // new Node<>(dado, proximoNó)
        topo = new Node<>(elemento, topo);
        quantidade++;
    }

    // ── pop: salva dado, avança topo para o próximo nó ──────────────
    @Override   // Θ(1)
    public T pop() {
        if (isEmpty()) throw new EmptyStackException();
        T removido = topo.dado;
        topo = topo.proximo;   // GC coleta o nó desconectado
        quantidade--;
        return removido;
    }

    @Override   // Θ(1)
    public T peek() {
        if (isEmpty()) throw new EmptyStackException();
        return topo.dado;
    }

    @Override public boolean isEmpty() { return topo == null; }
    @Override public int size()        { return quantidade; }
}

