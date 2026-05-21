package bloco3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes JUnit 5 para IndiceInvertido
 * Problema 3.2
 */
class IndiceInvertidoTest {
    private IndiceInvertido indice;

    @BeforeEach
    void setUp() {
        indice = new IndiceInvertido();
    }

    @Test
    @DisplayName("indexar e buscar: produto com uma palavra")
    void testIndexarBuscarUmaPalavra() {
        indice.indexar(1, "cadeira");
        
        List<Long> resultado = indice.buscar("cadeira");
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(1L));
    }

    @Test
    @DisplayName("indexar e buscar: múltiplas palavras em um produto")
    void testIndexarMultiplasPalavras() {
        indice.indexar(1, "cadeira ergonômica");
        
        List<Long> cadeiraResultado = indice.buscar("cadeira");
        List<Long> ergonômicaResultado = indice.buscar("ergonômica");
        
        assertEquals(1, cadeiraResultado.size());
        assertTrue(cadeiraResultado.contains(1L));
        
        assertEquals(1, ergonômicaResultado.size());
        assertTrue(ergonômicaResultado.contains(1L));
    }

    @Test
    @DisplayName("buscar palavra inexistente retorna lista vazia")
    void testBuscarInexistente() {
        indice.indexar(1, "mesa");
        
        List<Long> resultado = indice.buscar("inexistente");
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("buscar normaliza para lowercase")
    void testNormalizacaoLowercase() {
        indice.indexar(1, "CADEIRA");
        
        List<Long> resultado1 = indice.buscar("cadeira");
        List<Long> resultado2 = indice.buscar("CADEIRA");
        List<Long> resultado3 = indice.buscar("CaDeIrA");
        
        assertEquals(resultado1, resultado2);
        assertEquals(resultado2, resultado3);
        assertEquals(1, resultado1.size());
    }

    @Test
    @DisplayName("buscar múltiplas palavras (interseção)")
    void testBuscarMultiplas() {
        // Produtos do exemplo da documentação
        indice.indexar(1, "cadeira ergonômica");
        indice.indexar(2, "mesa de escritório");
        indice.indexar(3, "cadeira gamer");
        
        List<Long> resultado = indice.buscarMultiplas("cadeira gamer");
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(3L));
    }

    @Test
    @DisplayName("buscar múltiplas com nenhuma interseção retorna vazio")
    void testBuscarMultiplasVazio() {
        indice.indexar(1, "cadeira");
        indice.indexar(2, "mesa");
        
        List<Long> resultado = indice.buscarMultiplas("cadeira mesa");
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("múltiplos produtos com mesma palavra")
    void testMultiplosProdutosMesmaPalavra() {
        indice.indexar(1, "cadeira ergonômica");
        indice.indexar(2, "mesa de escritório");
        indice.indexar(3, "cadeira gamer");
        
        List<Long> resultado = indice.buscar("cadeira");
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(1L));
        assertTrue(resultado.contains(3L));
    }

    @Test
    @DisplayName("totalPalavrasIndexadas")
    void testTotalPalavrasIndexadas() {
        indice.indexar(1, "cadeira ergonômica");
        indice.indexar(2, "mesa de escritório");
        indice.indexar(3, "cadeira gamer");
        
        // Palavras: cadeira, ergonômica, mesa, de, escritório, gamer
        assertEquals(6, indice.totalPalavrasIndexadas());
    }

    @Test
    @DisplayName("não indexar mesma palavra duas vezes do mesmo produto")
    void testNaoDuplicar() {
        indice.indexar(1, "cadeira cadeira");
        
        List<Long> resultado = indice.buscar("cadeira");
        assertEquals(1, resultado.size());
        // Verifica que ID 1 aparece apenas uma vez
        long count = resultado.stream().filter(id -> id == 1L).count();
        assertEquals(1, count);
    }

    @Test
    @DisplayName("buscar multiplas com uma palavra retorna resultado único")
    void testBuscarMultiplasUmaPalavra() {
        indice.indexar(1, "cadeira");
        indice.indexar(2, "mesa");
        
        List<Long> resultado = indice.buscarMultiplas("cadeira");
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(1L));
    }

    @Test
    @DisplayName("indexar nome null ou vazio")
    void testIndexarNullVazio() {
        // Não deve lançar exceção
        indice.indexar(1, null);
        indice.indexar(2, "");
        
        // Não deve ter indexado nada
        assertEquals(0, indice.totalPalavrasIndexadas());
    }

    @Test
    @DisplayName("buscar null ou vazio")
    void testBuscarNullVazio() {
        indice.indexar(1, "teste");
        
        assertTrue(indice.buscar(null).isEmpty());
        assertTrue(indice.buscar("").isEmpty());
    }
}
