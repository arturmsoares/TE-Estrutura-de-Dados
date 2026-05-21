package bloco1;

import java.util.Objects;

/**
 * Exercício 1.1 - Classe C: Coordenada
 * PROBLEMA: Comparação de double com == é perigosa para floats/doubles
 * 
 * EXPLICAÇÃO DO PROBLEMA:
 * - double tem representação de ponto flutuante inexata (arredondamento)
 * - Duas coordenadas calculadas diferentemente podem ter valores ligeiramente diferentes
 * - Usar == com double raramente retorna true para valores equivalentes
 * - A comparação lat == c.lat && lon == c.lon pode falhar mesmo com coordenadas iguais
 * - Para usar em HashSet/HashMap, coordenadas "iguais" devem ter hashCode() idêntico
 * 
 * SOLUÇÃO: 
 * 1. Se coordenadas devem ser exatamente iguais: usar Double.doubleToLongBits() para comparação segura
 * 2. Para tolerância: usar epsilon (|a - b| < epsilon)
 * 3. Aqui usamos solução segura com doubleToLongBits
 */
public class Coordenada {
    public double lat;
    public double lon;

    public Coordenada(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coordenada)) return false;
        Coordenada c = (Coordenada) o;
        // CORRIGIDO: usar doubleToLongBits para comparação segura de doubles
        return Double.doubleToLongBits(lat) == Double.doubleToLongBits(c.lat) &&
               Double.doubleToLongBits(lon) == Double.doubleToLongBits(c.lon);
    }

    @Override
    public String toString() {
        return "Coordenada{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
