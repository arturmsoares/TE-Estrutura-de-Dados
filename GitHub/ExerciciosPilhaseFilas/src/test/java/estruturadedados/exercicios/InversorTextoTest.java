package estruturadedados.exercicios;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import estruturadedados.ExPilhasFilas.InversorTexto;

import org.junit.jupiter.api.DisplayName;

class InversorTextoTest {

    @Test
    @DisplayName("Deve inverter uma string comum corretamente")
    void deveInverterStringComum() {
        assertEquals("MTFI", InversorTexto.inverter("IFTM"));
    }

    @Test
    @DisplayName("Deve retornar string vazia quando receber vazio")
    void deveLidarComStringVazia() {
        assertEquals("", InversorTexto.inverter(""));
    }

    @Test
    @DisplayName("Deve funcionar com um único caractere")
    void deveInverterUmCaractere() {
        assertEquals("A", InversorTexto.inverter("A"));
    }

}
