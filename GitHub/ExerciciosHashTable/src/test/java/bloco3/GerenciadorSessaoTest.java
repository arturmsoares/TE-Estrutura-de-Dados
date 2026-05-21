package bloco3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes JUnit 5 para GerenciadorSessao
 * Problema 3.1
 */
class GerenciadorSessaoTest {
    private GerenciadorSessao gerenciador;

    @BeforeEach
    void setUp() {
        gerenciador = new GerenciadorSessao();
    }

    @Test
    @DisplayName("registrar e buscar sessão")
    void testRegistrarBuscar() {
        Usuario user = new Usuario(1, "admin");
        gerenciador.registrar("token-123", user);

        Usuario recuperado = gerenciador.buscar("token-123");
        assertNotNull(recuperado);
        assertEquals(1, recuperado.getId());
        assertEquals("admin", recuperado.getPerfil());
    }

    @Test
    @DisplayName("buscar token inexistente retorna null")
    void testBuscarInexistente() {
        assertNull(gerenciador.buscar("token-inexistente"));
    }

    @Test
    @DisplayName("invalidar sessão remove token")
    void testInvalidar() {
        Usuario user = new Usuario(2, "user");
        gerenciador.registrar("token-456", user);
        assertTrue(gerenciador.eValido("token-456"));

        assertTrue(gerenciador.invalidar("token-456"));
        assertFalse(gerenciador.eValido("token-456"));
        assertNull(gerenciador.buscar("token-456"));
    }

    @Test
    @DisplayName("invalidar token inexistente retorna false")
    void testInvalidarInexistente() {
        assertFalse(gerenciador.invalidar("token-inexistente"));
    }

    @Test
    @DisplayName("eValido retorna true/false corretamente")
    void testEValido() {
        Usuario user = new Usuario(3, "guest");
        gerenciador.registrar("token-789", user);

        assertTrue(gerenciador.eValido("token-789"));
        assertFalse(gerenciador.eValido("token-inexistente"));
    }

    @Test
    @DisplayName("token nulo lança exceção")
    void testTokenNulo() {
        Usuario user = new Usuario(4, "admin");
        assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.registrar(null, user);
        });
    }

    @Test
    @DisplayName("token vazio lança exceção")
    void testTokenVazio() {
        Usuario user = new Usuario(5, "admin");
        assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.registrar("", user);
        });
    }

    @Test
    @DisplayName("usuário nulo lança exceção")
    void testUsuarioNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.registrar("token-123", null);
        });
    }

    @Test
    @DisplayName("múltiplas sessões simultâneas")
    void testMultiplasSessoes() {
        for (int i = 0; i < 10; i++) {
            Usuario user = new Usuario(i, "user" + i);
            gerenciador.registrar("token-" + i, user);
        }

        assertEquals(10, gerenciador.totalSessoes());

        // Verificar que cada sessão pode ser recuperada
        for (int i = 0; i < 10; i++) {
            Usuario user = gerenciador.buscar("token-" + i);
            assertNotNull(user);
            assertEquals(i, user.getId());
        }
    }

    @Test
    @DisplayName("atualizar sessão existente")
    void testAtualizarSessao() {
        Usuario user1 = new Usuario(1, "admin");
        Usuario user2 = new Usuario(1, "supervisor");

        gerenciador.registrar("token-xyz", user1);
        assertEquals("admin", gerenciador.buscar("token-xyz").getPerfil());

        // Atualizar com novo usuário
        gerenciador.registrar("token-xyz", user2);
        assertEquals("supervisor", gerenciador.buscar("token-xyz").getPerfil());
        assertEquals(1, gerenciador.totalSessoes());
    }
}
