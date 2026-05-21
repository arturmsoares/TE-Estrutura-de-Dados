package bloco1;

import java.util.LinkedList;

/**
 * Exercício 1.3 - TabelaHashEncadeada<K,V>
 * Implementação de tabela hash com encadeamento separado
 * 
 * @param <K> Tipo da chave
 * @param <V> Tipo do valor
 */
public class TabelaHashEncadeada<K, V> {
    private static final int CAPACIDADE_PADRAO = 16;
    private LinkedList<Entrada<K, V>>[] buckets;
    private int tamanho;

    @SuppressWarnings("unchecked")
    public TabelaHashEncadeada(int capacidade) {
        buckets = new LinkedList[capacidade];
        for (int i = 0; i < capacidade; i++) {
            buckets[i] = new LinkedList<>();
        }
        this.tamanho = 0;
    }

    public TabelaHashEncadeada() {
        this(CAPACIDADE_PADRAO);
    }

    /**
     * Retorna o índice do bucket para a chave k.
     * Trata k == null explicitamente (lança IllegalArgumentException).
     */
    private int indiceBucket(K k) {
        if (k == null) {
            throw new IllegalArgumentException("Chave não pode ser nula");
        }
        // Math.abs para evitar índices negativos (hashCode() pode retornar valores negativos)
        return Math.abs(k.hashCode()) % buckets.length;
    }

    /**
     * Insere ou atualiza o par (chave, valor).
     * Se a chave já existir, substitui o valor e NÃO incrementa tamanho.
     */
    public void put(K chave, V valor) {
        int idx = indiceBucket(chave);
        LinkedList<Entrada<K, V>> bucket = buckets[idx];

        // Procura se a chave já existe
        for (Entrada<K, V> entrada : bucket) {
            if (entrada.chave.equals(chave)) {
                // Chave já existe - atualizar valor
                entrada.valor = valor;
                return;
            }
        }

        // Chave nova - adicionar
        bucket.add(new Entrada<>(chave, valor));
        tamanho++;
    }

    /**
     * Retorna o valor associado à chave, ou null se não encontrado.
     */
    public V get(K chave) {
        int idx = indiceBucket(chave);
        LinkedList<Entrada<K, V>> bucket = buckets[idx];

        for (Entrada<K, V> entrada : bucket) {
            if (entrada.chave.equals(chave)) {
                return entrada.valor;
            }
        }

        return null;
    }

    /**
     * Remove o par com a chave dada.
     * Retorna true se removido, false se não encontrado.
     */
    public boolean remove(K chave) {
        int idx = indiceBucket(chave);
        LinkedList<Entrada<K, V>> bucket = buckets[idx];

        for (Entrada<K, V> entrada : bucket) {
            if (entrada.chave.equals(chave)) {
                bucket.remove(entrada);
                tamanho--;
                return true;
            }
        }

        return false;
    }

    public int tamanho() {
        return tamanho;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    /**
     * Classe interna de entrada
     */
    private static class Entrada<K, V> {
        K chave;
        V valor;

        Entrada(K chave, V valor) {
            this.chave = chave;
            this.valor = valor;
        }
    }
}
