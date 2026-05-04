package estruturadedados.ExPilhasFilas;

public class InversorTexto {

    public static String inverter(String texto) {
        Pilha<Character> pilha = new PilhaArray<>();

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            pilha.push(c); // Adiciona cada letra ao topo
        }

        StringBuilder resultado = new StringBuilder();

        while (!pilha.isEmpty()) {
            char c = pilha.pop(); // Remove do topo 
            resultado.append(c); // Adiciona ao novo texto
        }

        return resultado.toString();
    }

    // Complexidade Temporal: Como percorremos o texto uma vez para empilhar e uma vez para desempilhar, a complexidade final é O(n).
    // Complexidade Espacial: É necessária uma pilha que consiga guardar todos os n caracteres, logo, o custo de memória também é O(n).
    // Casos de teste esperados:
    // inverter("IFTM") → "MTFI"
    // inverter("") → ""
    // inverter("A") → "A"

}
