package estruturadedados.ExPilhasFilas;

public class AvaliadorPosFixa {
    public static double avaliar(String expressao) {
        Pilha<Double> pilha = new PilhaArray<>();

        // O split separa os números e operadores por espaço
        for (String token : expressao.split(" ")) {

            switch (token) {
                case "+" -> {
                    double b = pilha.pop(), a = pilha.pop();
                    pilha.push(a + b);
                }
                case "-" -> {
                    double b = pilha.pop(), a = pilha.pop();
                    pilha.push(a - b);
                }
                case "*" -> {
                    double b = pilha.pop(), a = pilha.pop();
                    pilha.push(a * b);
                }
                case "/" -> {
                    double b = pilha.pop(), a = pilha.pop();
                    if (b == 0)
                        throw new ArithmeticException("Divisão por zero");
                    pilha.push(a / b);
                }
                case "%" -> {
                    double b = pilha.pop(); // Segundo operando
                    double a = pilha.pop(); // Primeiro operando
                    if (b == 0)
                        throw new ArithmeticException("Divisão por zero no módulo");
                    pilha.push(a % b);
                }
                default -> pilha.push(Double.parseDouble(token));

            }
        }
        return pilha.pop();
    }
}

// Para calcular 10%3, por exemplo, o computador encontra o %. 
// Ele não consegue calcular o resto da divisão de "nada" por 3
// então ele precisa de um dividendo e um divisor.