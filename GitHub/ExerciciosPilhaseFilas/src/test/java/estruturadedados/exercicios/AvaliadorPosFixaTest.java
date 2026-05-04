package estruturadedados.exercicios;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import estruturadedados.ExPilhasFilas.AvaliadorPosFixa;

public class AvaliadorPosFixaTest {

    @Test
    @DisplayName("Deve avaliar expressão com módulo")
    void deveAvaliarModulo() {
        // "10 3 %" -> 10 % 3 = 1
        assertEquals(1.0, AvaliadorPosFixa.avaliar("10 3 %"));
    }

    @Test
    @DisplayName("Deve avaliar expressão complexa da apostila")
    void deveAvaliarExpressaoComplexa() {
        // "5 1 2 + 4 * + 3 -" -> 14.0
        assertEquals(14.0, AvaliadorPosFixa.avaliar("5 1 2 + 4 * + 3 -"));
    }
}
