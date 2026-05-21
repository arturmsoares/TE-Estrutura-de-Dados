package bloco1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes JUnit 5 para TabelaHashEncadeada
 * Exercício 1.3
 */
class TabelaHashEncadeadaTest {
    private TabelaHashEncadeada<String, Integer> tabela;

    @BeforeEach
    void setUp() {
        tabela = new TabelaHashEncadeada<>(4);
    }

    @Test
    @DisplayName("put e get: chave inserida deve ser recuperada")
    void testPutGet() {
        // Inserir
        tabela.put("produto-42", 100);
        
        // Recuperar e verificar
        assertEquals(100, tabela.get("produto-42"));
        assertEquals(1, tabela.tamanho());
        assertFalse(tabela.estaVazia());
    }

    @Test
    @DisplayName("put com atualização: tamanho não deve aumentar")
    void testAtualizacaoNaoIncrementaTamanho() {
        // Inserir mesma chave duas vezes com valores diferentes
        tabela.put("chave-x", 50);
        assertEquals(1, tabela.tamanho());
        assertEquals(50, tabela.get("chave-x"));

        // Atualizar - tamanho NÃO deve aumentar
        tabela.put("chave-x", 75);
        assertEquals(1, tabela.tamanho());
        assertEquals(75, tabela.get("chave-x"));
    }

    @Test
    @DisplayName("colisão: duas chaves no mesmo bucket devem ser recuperadas corretamente")
    void testColisao() {
        // Com capacidade 4, encontrar duas chaves que colidem
        // Se hashCode() % 4 for igual, elas caem no mesmo bucket
        // Exemplo: "a".hashCode() % 4 pode ser igual a "b".hashCode() % 4
        
        // Inserir várias chaves que podem colidir
        tabela.put("key1", 100);
        tabela.put("key2", 200);
        tabela.put("key3", 300);
        
        // Recuperar e verificar que cada chave retorna seu valor correto
        assertEquals(100, tabela.get("key1"));
        assertEquals(200, tabela.get("key2"));
        assertEquals(300, tabela.get("key3"));
        assertEquals(3, tabela.tamanho());
    }

    @Test
    @DisplayName("remove: deve remover entrada e decrementar tamanho")
    void testRemove() {
        tabela.put("a", 1);
        tabela.put("b", 2);
        assertEquals(2, tabela.tamanho());

        // Remover chave existente
        assertTrue(tabela.remove("a"));
        assertEquals(1, tabela.tamanho());
        assertNull(tabela.get("a"));
        assertEquals(2, tabela.get("b"));

        // Remover chave inexistente
        assertFalse(tabela.remove("inexistente"));
        assertEquals(1, tabela.tamanho());
    }

    @Test
    @DisplayName("get: deve retornar null para chave inexistente")
    void testGetInexistente() {
        assertNull(tabela.get("nao-existe"));
    }

    @Test
    @DisplayName("null key: deve lançar IllegalArgumentException")
    void testNullKey() {
        assertThrows(IllegalArgumentException.class, () -> {
            tabela.put(null, 100);
        });
    }

    @Test
    @DisplayName("vários puts e gets")
    void testMultiplosPutsGets() {
        String[] chaves = {"a", "b", "c", "d", "e", "f"};
        
        for (int i = 0; i < chaves.length; i++) {
            tabela.put(chaves[i], i * 10);
        }
        
        assertEquals(chaves.length, tabela.tamanho());
        
        for (int i = 0; i < chaves.length; i++) {
            assertEquals(i * 10, tabela.get(chaves[i]));
        }
    }
}
