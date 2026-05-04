package estruturadedados.ExPilhasFilas;

public class BufferCircular<T> {
    private final Object[] dados;
    private int inicio, fim, quantidade;

    public BufferCircular(int capacidade) {
        dados = new Object[capacidade];
    }

    public void adicionar(T elemento) {
        dados[fim] = elemento;
        fim = (fim + 1) % dados.length;
        if (quantidade == dados.length)
            inicio = (inicio + 1) % dados.length;  // descarta o mais antigo
        else
            quantidade++;
    }

    @SuppressWarnings("unchecked")
    public T removerMaisAntigo() {
        if (estaVazio()) {
            throw new java.util.NoSuchElementException("Buffer vazio");
        }
        
        T removido = (T) dados[inicio];
        dados[inicio] = null; // Boa prática: evita memory leak
        inicio = (inicio + 1) % dados.length; // Avança o início circularmente
        quantidade--;
        return removido;
    }

    public boolean estaVazio() {
        return quantidade == 0;
    }

    public boolean estaCheio() {
        return quantidade == dados.length; // Capacidade máxima atingida
    }

    public int tamanho() {
        return quantidade;
    }
}

// Teste de uso:
// BufferCircular<String> logs = new BufferCircular<>(3);
// logs.adicionar("INFO: Servidor iniciado");
// logs.adicionar("INFO: Conexão aceita");
// logs.adicionar("WARN: Timeout na query");
// logs.adicionar("ERROR: Falha na autenticação");
// O primeiro log foi descartado — buffer contém os 3 mais recentes

