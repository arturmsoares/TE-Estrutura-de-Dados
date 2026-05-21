package bloco2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes JUnit 5 para TabelaHashAberta
 * Exercício 2.3
 */
class TabelaHashAbertaTest {
    private TabelaHashAberta<String, Integer> tabela;

    @BeforeEach
    void setUp() {
        tabela = new TabelaHashAberta<>(10);
    }

    @Test
    @DisplayName("put e get básico")
    void testPutGet() {
        tabela.put("chave1", 100);
        assertEquals(100, tabela.get("chave1"));
    }

    @Test
    @DisplayName("rehashing é disparado quando λ ≥ 0,70")
    void testRehashingDisparo() {
        int capacidadeInicial = tabela.capacidade();
        
        // Inserir 7 elementos (λ = 7/10 = 0.70)
        for (int i = 0; i < 7; i++) {
            tabela.put("chave" + i, i * 10);
        }
        
        // Fator de carga é exatamente 0.70, ainda não fez rehash
        // (porque check é >= e a 8ª inserção dispara rehash)
        
        // Inserir 8º elemento - deve disparar rehashing
        tabela.put("chave7", 700);
        
        // Capacidade deve ter dobrado
        assertEquals(capacidadeInicial * 2, tabela.capacidade());
        
        // Fator de carga deve estar abaixo de 0.70 após rehashing
        assertTrue(tabela.fatorDeCarga() < 0.70);
    }

    @Test
    @DisplayName("rehashing: todos os pares são corretamente migrados")
    void testRehashingMigracao() {
        // Inserir vários elementos
        String[] chaves = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
        for (int i = 0; i < chaves.length; i++) {
            tabela.put(chaves[i], i * 100);
        }
        
        // Verificar que todos os elementos podem ser recuperados após rehashing
        for (int i = 0; i < chaves.length; i++) {
            assertEquals(i * 100, tabela.get(chaves[i]), 
                "Chave '" + chaves[i] + "' não foi encontrada corretamente após rehashing");
        }
    }

    @Test
    @DisplayName("remove com lazy deletion")
    void testRemove() {
        tabela.put("a", 1);
        tabela.put("b", 2);
        assertEquals(2, tabela.tamanho());
        
        assertTrue(tabela.remove("a"));
        assertEquals(1, tabela.tamanho());
        assertNull(tabela.get("a"));
        assertEquals(2, tabela.get("b"));
        
        assertFalse(tabela.remove("inexistente"));
    }

    @Test
    @DisplayName("atualização não incrementa tamanho")
    void testAtualizacao() {
        tabela.put("x", 10);
        int tamanhoAntes = tabela.tamanho();
        
        tabela.put("x", 20);
        
        assertEquals(tamanhoAntes, tabela.tamanho());
        assertEquals(20, tabela.get("x"));
    }

    @Test
    @DisplayName("múltiplas reinserções após rehashing")
    void testMultiplosRehashings() {
        // Inserir muitos elementos para disparar múltiplos rehashings
        int capacidadeInicial = tabela.capacidade();
        
        for (int i = 0; i < 100; i++) {
            tabela.put("chave" + i, i);
        }
        
        // Capacidade deve ter crescido muito além do inicial
        assertTrue(tabela.capacidade() > capacidadeInicial);
        
        // Todos os elementos devem estar presentes
        for (int i = 0; i < 100; i++) {
            assertEquals(i, tabela.get("chave" + i), 
                "Elemento i=" + i + " não encontrado");
        }
    }

    @Test
    @DisplayName("fator de carga mantém-se abaixo de 0.70 após rehashing")
    void testFatorDeCargaPosRehashing() {
        for (int i = 0; i < 50; i++) {
            tabela.put("elem" + i, i);
        }
        
        // Após múltiplos rehashings, fator deve estar abaixo de 0.70
        assertTrue(tabela.fatorDeCarga() < 0.70 || tabela.tamanho() == 0,
            "Fator de carga: " + tabela.fatorDeCarga());
    }
}
