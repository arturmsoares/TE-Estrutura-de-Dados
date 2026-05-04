package estruturadedados.exercicios;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import estruturadedados.ExPilhasFilas.PilhaComMinimo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

class PilhaComMinimoTest {
    private PilhaComMinimo<Integer> pilha;

    @BeforeEach
    void setup() {
        pilha = new PilhaComMinimo<>();
    }

    @Test
    @DisplayName("Deve atualizar o mínimo corretamente a cada push")
    void deveAtualizarMinimoNoPush() {
        pilha.push(10);
        assertEquals(10, pilha.min());

        pilha.push(5);
        assertEquals(5, pilha.min()); // novo mínimo inserido

        pilha.push(8);
        assertEquals(5, pilha.min()); // insere valor maior, o mínimo continua 5
    }

    @Test
    @DisplayName("Deve restaurar o mínimo anterior após o pop do mínimo atual")
    void deveRestaurarMinimoNoPop() {
        pilha.push(20);
        pilha.push(10);
        pilha.push(30);
        pilha.push(5);

        assertEquals(5, pilha.min()); // mínimo atual é 5

        // remove o 5, o mínimo atual
        assertEquals(5, pilha.pop());
        assertEquals(10, pilha.min()); // novo minimo deve ser 10

        // remove o 30 que não é o mínimo
        assertEquals(30, pilha.pop());
        assertEquals(10, pilha.min()); // continua sendo 10

        // remove o 10, mínimo atual
        assertEquals(10, pilha.pop());
        assertEquals(20, pilha.min()); // o mínimo deve ser 20
    }
}