import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementação de um cache LRU utilizando LinkedHashMap.
 *
 * LRU = Least Recently Used
 *
 * O elemento menos recentemente acessado é removido
 * automaticamente quando a capacidade máxima é atingida.
 */
public class LRUCache {

    // Capacidade máxima do cache
    private final int capacidade;

    // Estrutura principal
    private final LinkedHashMap<String, Object> cache;

    /**
     * Construtor do cache.
     *
     * @param capacidade quantidade máxima de elementos
     */
    public LRUCache(int capacidade) {

        this.capacidade = capacidade;

        /*
         * LinkedHashMap com:
         *
         * initialCapacity = capacidade
         * loadFactor = 0.75f
         * accessOrder = true
         *
         * accessOrder=true:
         * mantém a ordem baseada em acesso.
         */
        this.cache = new LinkedHashMap<>(capacidade, 0.75f, true) {

            /**
             * Método chamado automaticamente após inserções.
             *
             * Retorna true quando o elemento mais antigo
             * deve ser removido.
             */
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {

                // Remove o mais antigo quando ultrapassar a capacidade
                return size() > LRUCache.this.capacidade;
            }
        };
    }

    /**
     * Insere ou atualiza um elemento no cache.
     *
     * Complexidade média: Θ(1)
     */
    public void put(String chave, Object valor) {
        cache.put(chave, valor);
    }

    /**
     * Busca um elemento no cache.
     *
     * Complexidade média: Θ(1)
     *
     * IMPORTANTE:
     * ao acessar, o elemento vira o mais recente.
     */
    public Object get(String chave) {
        return cache.get(chave);
    }

    /**
     * Retorna a ordem dos elementos
     * do mais antigo para o mais recente.
     */
    public List<String> ordemDeAcesso() {
        return new ArrayList<>(cache.keySet());
    }

    /**
     * Verifica se existe uma chave.
     */
    public boolean containsKey(String chave) {
        return cache.containsKey(chave);
    }

    /**
     * Retorna o tamanho atual do cache.
     */
    public int size() {
        return cache.size();
    }

    public void exibirCache() {

        System.out.println("\n========== ESTADO DO CACHE ==========");

        if (cache.isEmpty()) {
            System.out.println("Cache vazio.");
            return;
        }

        int posicao = 1;

        for (Map.Entry<String, Object> entry : cache.entrySet()) {

            System.out.println(
                    posicao + ". "
                            + entry.getKey()
                            + " -> "
                            + entry.getValue());

            posicao++;
        }

        System.out.println("\nLegenda:");
        System.out.println("- Primeiro = menos recente");
        System.out.println("- Último = mais recente");

        System.out.println("=====================================");
    }
}
