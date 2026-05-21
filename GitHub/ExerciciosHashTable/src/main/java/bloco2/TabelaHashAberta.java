package bloco2;

/**
 * Exercício 2.3 - Rehashing: análise e implementação
 * 
 * TabelaHashAberta com endereçamento aberto e sondagem linear.
 * Implementa rehashing automático.
 * 
 * Estratégia de rehashing:
 * - Limiar: quando λ ≥ 0.70
 * - Ação: dobra a capacidade e reinserem todos os pares válidos
 */
public class TabelaHashAberta<K, V> {
    private static final double LIMIAR_CARGA = 0.70;
    private static final Object DELETADO = new Object();

    private Object[] chaves;
    private Object[] valores;
    private int tamanho;
    private int capacidade;

    public TabelaHashAberta(int capacidade) {
        this.capacidade = capacidade;
        this.chaves = new Object[capacidade];
        this.valores = new Object[capacidade];
        this.tamanho = 0;
    }

    private int sonda(K chave, int i) {
        return (Math.abs(chave.hashCode()) + i) % capacidade;
    }

    public void put(K chave, V valor) {
        // Verificar se precisa rehashing ANTES de inserir
        if (fatorDeCarga() >= LIMIAR_CARGA) {
            rehash();
        }

        for (int i = 0; i < capacidade; i++) {
            int idx = sonda(chave, i);
            
            if (chaves[idx] == null || chaves[idx] == DELETADO) {
                chaves[idx] = chave;
                valores[idx] = valor;
                tamanho++;
                return;
            }
            
            if (chaves[idx].equals(chave)) {
                valores[idx] = valor; // atualização
                return;
            }
        }
        
        throw new IllegalStateException("Tabela cheia");
    }

    @SuppressWarnings("unchecked")
    public V get(K chave) {
        for (int i = 0; i < capacidade; i++) {
            int idx = sonda(chave, i);
            
            if (chaves[idx] == null) {
                return null; // Slot vazio indica fim da busca
            }
            
            if (chaves[idx] != DELETADO && chaves[idx].equals(chave)) {
                return (V) valores[idx];
            }
        }
        
        return null;
    }

    @SuppressWarnings("unchecked")
    public boolean remove(K chave) {
        for (int i = 0; i < capacidade; i++) {
            int idx = sonda(chave, i);
            
            if (chaves[idx] == null) {
                return false; // Slot vazio indica chave não existe
            }
            
            if (chaves[idx] != DELETADO && chaves[idx].equals(chave)) {
                chaves[idx] = DELETADO;
                tamanho--;
                return true;
            }
        }
        
        return false;
    }

    /**
     * Dobra a capacidade e reinserem todos os pares válidos.
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        Object[] chavasAntigas = chaves;
        Object[] valoresAntigos = valores;
        int capacidadeAntiga = capacidade;

        // Nova capacidade é o dobro
        capacidade = capacidade * 2;
        chaves = new Object[capacidade];
        valores = new Object[capacidade];
        tamanho = 0;

        // Reinserem todos os pares válidos
        for (int i = 0; i < capacidadeAntiga; i++) {
            if (chavasAntigas[i] != null && chavasAntigas[i] != DELETADO) {
                K chaveAnterior = (K) chavasAntigas[i];
                V valorAnterior = (V) valoresAntigos[i];
                // Usar put de forma recursiva (mas sem verificar limiar novamente)
                putSemRehash(chaveAnterior, valorAnterior);
            }
        }
    }

    /**
     * Put sem verificação de rehashing (usado durante rehash)
     */
    private void putSemRehash(K chave, V valor) {
        for (int i = 0; i < capacidade; i++) {
            int idx = sonda(chave, i);
            
            if (chaves[idx] == null || chaves[idx] == DELETADO) {
                chaves[idx] = chave;
                valores[idx] = valor;
                tamanho++;
                return;
            }
        }
    }

    public double fatorDeCarga() {
        return (double) tamanho / capacidade;
    }

    public int tamanho() {
        return tamanho;
    }

    public int capacidade() {
        return capacidade;
    }
}
