package estruturadedados.exercicios;

import org.junit.jupiter.api.Test;

import estruturadedados.ExPilhasFilas.BufferCircular;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

class BufferCircularTest {
    private BufferCircular<String> buffer;
    private final int CAPACIDADE = 3;

    @BeforeEach
    void setup() {
        buffer = new BufferCircular<>(CAPACIDADE);
    }

    @Test
    @DisplayName("Deve adicionar e remover elementos respeitando a ordem FIFO")
    void deveFuncionarComoFilaBasica() {
        buffer.adicionar("A");
        buffer.adicionar("B");
        
        assertEquals(2, buffer.tamanho());
        assertEquals("A", buffer.removerMaisAntigo());
        assertEquals("B", buffer.removerMaisAntigo());
        assertTrue(buffer.estaVazio());
    }

    @Test
    @DisplayName("Deve sobrescrever o elemento mais antigo quando atingir a capacidade")
    void deveSobrescreverNoLimite() {
        // Preenche a capacidade (3)
        buffer.adicionar("Log 1");
        buffer.adicionar("Log 2");
        buffer.adicionar("Log 3");

        // Adiciona o quarto elemento. O "Log 1" deve ser descartado.
        buffer.adicionar("Log 4");

        assertEquals(3, buffer.tamanho(), "O tamanho deve permanecer fixo na capacidade máxima");
        assertEquals("Log 2", buffer.removerMaisAntigo(), "O próximo a sair deve ser o Log 2, pois o 1 foi descartado");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar remover de um buffer vazio")
    void deveLancarExcecaoBufferVazio() {
        assertThrows(NoSuchElementException.class, () -> {
            buffer.removerMaisAntigo();
        });
    }

    @Test
    @DisplayName("Deve identificar corretamente quando o buffer está cheio")
    void deveIdentificarBufferCheio() {
        buffer.adicionar("1");
        buffer.adicionar("2");
        assertFalse(buffer.estaCheio());
        
        buffer.adicionar("3");
        assertTrue(buffer.estaCheio());
    }

    @Test
    @DisplayName("Deve funcionar corretamente após múltiplos giros (wrap-around)")
    void deveFuncionarAposMultiplosGiros() {
        // Ciclo de preenchimento e descarte intenso
        for (int i = 1; i <= 10; i++) {
            buffer.adicionar("Msg " + i);
        }

        // Com capacidade 3, após 10 inserções, devem sobrar: Msg 8, Msg 9, Msg 10
        assertEquals(3, buffer.tamanho());
        assertEquals("Msg 8", buffer.removerMaisAntigo());
        assertEquals("Msg 9", buffer.removerMaisAntigo());
        assertEquals("Msg 10", buffer.removerMaisAntigo());
    }
}
