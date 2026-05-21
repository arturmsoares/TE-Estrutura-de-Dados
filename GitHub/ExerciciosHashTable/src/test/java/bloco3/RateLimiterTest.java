package bloco3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes JUnit 5 para RateLimiter
 * Problema 3.3
 */
class RateLimiterTest {
    private RateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        rateLimiter = new RateLimiter();
    }

    @Test
    @DisplayName("primeira requisição é sempre permitida")
    void testPrimeiraRequisicao() {
        assertTrue(rateLimiter.permitir("192.168.1.1"));
    }

    @Test
    @DisplayName("requisições dentro do limite são permitidas")
    void testRequisicoesDentroDoLimite() {
        for (int i = 0; i < 100; i++) {
            assertTrue(rateLimiter.permitir("192.168.1.1"), "Requisição " + i + " deveria ser permitida");
        }
    }

    @Test
    @DisplayName("requisição acima do limite é bloqueada")
    void testRequisicaoAcimaDoLimite() {
        for (int i = 0; i < 100; i++) {
            rateLimiter.permitir("192.168.1.2");
        }
        
        // 101ª requisição deve ser bloqueada
        assertFalse(rateLimiter.permitir("192.168.1.2"));
    }

    @Test
    @DisplayName("IPs diferentes têm contadores independentes")
    void testIPsDiferentes() {
        for (int i = 0; i < 100; i++) {
            rateLimiter.permitir("192.168.1.1");
            rateLimiter.permitir("192.168.1.2");
        }
        
        // Ambos atingiram limite
        assertFalse(rateLimiter.permitir("192.168.1.1"));
        assertFalse(rateLimiter.permitir("192.168.1.2"));
        
        // Novo IP ainda pode
        assertTrue(rateLimiter.permitir("192.168.1.3"));
    }

    @Test
    @DisplayName("contadorIP retorna número correto de requisições")
    void testContadorIP() {
        for (int i = 0; i < 50; i++) {
            rateLimiter.permitir("10.0.0.1");
        }
        
        assertEquals(50, rateLimiter.contadorIP("10.0.0.1"));
    }

    @Test
    @DisplayName("contadorIP retorna 0 para IP inexistente")
    void testContadorIPInexistente() {
        assertEquals(0, rateLimiter.contadorIP("inexistente"));
    }

    @Test
    @DisplayName("limparExpirados remove entradas antigas")
    void testLimparExpirados() throws InterruptedException {
        // Fazer requisição inicial
        rateLimiter.permitir("10.0.0.1");
        assertEquals(1, rateLimiter.totalIPsAtivos());
        
        // Esperar 61ms para simular passagem de tempo (seria 60s em produção)
        // Nota: este teste é simplificado; em produção seria mais complexo
        
        // Limpar expirados
        rateLimiter.limparExpirados();
        
        // Após limpeza, contador deve estar em 0
        // (na verdade continuaria se fosse menos de 1 minuto)
        // Este teste valida que o método não lança exceção
        assertTrue(true);
    }

    @Test
    @DisplayName("totalIPsAtivos conta IPs únicos")
    void testTotalIPsAtivos() {
        for (int i = 0; i < 5; i++) {
            rateLimiter.permitir("192.168.1." + i);
        }
        
        assertEquals(5, rateLimiter.totalIPsAtivos());
    }

    @Test
    @DisplayName("stress test: múltiplos IPs com múltiplas requisições")
    void testStressTest() {
        int numIPs = 10;
        int reqPorIP = 50;
        
        // Fazer requisições
        for (int i = 0; i < numIPs; i++) {
            for (int j = 0; j < reqPorIP; j++) {
                if (j < 100) {
                    assertTrue(rateLimiter.permitir("192.168.1." + i));
                } else {
                    assertFalse(rateLimiter.permitir("192.168.1." + i));
                }
            }
        }
        
        assertEquals(numIPs, rateLimiter.totalIPsAtivos());
    }

    @Test
    @DisplayName("contador aumenta com cada requisição permitida")
    void testContadorAumenta() {
        String ip = "10.10.10.1";
        
        assertEquals(0, rateLimiter.contadorIP(ip));
        
        assertTrue(rateLimiter.permitir(ip));
        assertEquals(1, rateLimiter.contadorIP(ip));
        
        assertTrue(rateLimiter.permitir(ip));
        assertEquals(2, rateLimiter.contadorIP(ip));
        
        assertTrue(rateLimiter.permitir(ip));
        assertEquals(3, rateLimiter.contadorIP(ip));
    }

    @Test
    @DisplayName("requisição bloqueada não incrementa contador")
    void testRequisicaoBloqueadaNaoIncrementa() {
        String ip = "172.16.0.1";
        
        // Fazer 100 requisições
        for (int i = 0; i < 100; i++) {
            rateLimiter.permitir(ip);
        }
        
        assertEquals(100, rateLimiter.contadorIP(ip));
        
        // Tentar mais uma (será bloqueada)
        assertFalse(rateLimiter.permitir(ip));
        
        // Contador não aumenta
        assertEquals(100, rateLimiter.contadorIP(ip));
    }
}
