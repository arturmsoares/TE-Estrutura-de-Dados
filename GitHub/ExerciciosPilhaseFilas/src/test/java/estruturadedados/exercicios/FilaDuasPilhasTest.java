package estruturadedados.exercicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import estruturadedados.ExPilhasFilas.FilaDuasPilhas;

class FilaDuasPilhasTest {
    private FilaDuasPilhas<Integer> fila;

    @BeforeEach
    void setup() {
        fila = new FilaDuasPilhas<>();
    }

    @Test
    @DisplayName("Deve manter a ordem FIFO básica")
    void deveManterOrdemFifo() {
        fila.enqueue(10);
        fila.enqueue(20);
        fila.enqueue(30);

        assertEquals(10, fila.dequeue()); // O primeiro a entrar deve ser o primeiro a sair
        assertEquals(20, fila.dequeue());
        assertEquals(30, fila.dequeue());
        assertTrue(fila.isEmpty());
    }

    @Test
    @DisplayName("Deve funcionar corretamente com inserções e remoções intercaladas")
    void deveFuncionarIntercalado() {
        fila.enqueue(1);
        fila.enqueue(2);
        assertEquals(1, fila.dequeue()); // Transferência ocorre aqui: saida fica [2]

        fila.enqueue(3); // entrada fica [3]
        assertEquals(2, fila.dequeue()); // ainda remove da saida [2]
        assertEquals(3, fila.dequeue()); // saida esvaziou, nova transferência ocorre
        
        assertTrue(fila.isEmpty());
    }
}