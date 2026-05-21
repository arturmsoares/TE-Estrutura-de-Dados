import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class LRUCache {

    private final int capacidade;

    private final LinkedHashMap<String, Object> cache; 

    public LRUCache(int capacidade) {

        this.capacidade = capacidade;

        /*
         * loadFactor = 0.75f - load factor é a proporção de ocupação do mapa antes de redimensionar.
         * accessOrder = true - define que a ordem dos elementos é baseada no acesso (get ou put).
         */
        this.cache = new LinkedHashMap<>(capacidade, 0.75f, true) {

            /**
             * Método chamado após inserções.
             * Retorna true quando o elemento mais antigo deve ser removido.
             */
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {

                // Remove o mais antigo quando ultrapassar a capacidade
                return size() > LRUCache.this.capacidade;
            }
        };
    }


    public void put(String chave, Object valor) {
        cache.put(chave, valor);
    }


    public Object get(String chave) {
        return cache.get(chave);
    }


    public List<String> ordemDeAcesso() {
        return new ArrayList<>(cache.keySet()); 
    }


    public boolean containsKey(String chave) {
        return cache.containsKey(chave);
    }


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
