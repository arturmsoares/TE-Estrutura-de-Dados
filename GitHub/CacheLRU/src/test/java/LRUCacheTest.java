import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    @Test
    void deveInserirElementosAteOLimite() {

        LRUCache cache = new LRUCache(3);

        cache.put("A", 1);
        cache.put("B", 2);
        cache.put("C", 3);

        assertEquals(3, cache.size());

        assertEquals(
                List.of("A", "B", "C"),
                cache.ordemDeAcesso()
        );
    }

    @Test
    void deveRemoverElementoMenosRecente() {

        LRUCache cache = new LRUCache(3);

        cache.put("A", 1);
        cache.put("B", 2);
        cache.put("C", 3);

        // Inserção além do limite
        cache.put("D", 4);

        // A deve ser removido
        assertFalse(cache.containsKey("A"));

        assertEquals(
                List.of("B", "C", "D"),
                cache.ordemDeAcesso()
        );
    }

    @Test
    void devePromoverElementoAoAcessarComGet() {

        LRUCache cache = new LRUCache(3);

        cache.put("A", 1);
        cache.put("B", 2);
        cache.put("C", 3);

        // Acessa A
        cache.get("A");

        // A vai para o final
        assertEquals(
                List.of("B", "C", "A"),
                cache.ordemDeAcesso()
        );
    }

    @Test
    void deveRemoverOMenosRecenteAposGet() {

        LRUCache cache = new LRUCache(3);

        cache.put("A", 1);
        cache.put("B", 2);
        cache.put("C", 3);

        // Promove A
        cache.get("A");

        // Agora B é o menos recente
        cache.put("D", 4);

        assertFalse(cache.containsKey("B"));

        assertEquals(
                List.of("C", "A", "D"),
                cache.ordemDeAcesso()
        );
    }
}