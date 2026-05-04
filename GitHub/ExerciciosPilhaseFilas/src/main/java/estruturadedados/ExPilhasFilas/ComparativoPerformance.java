package estruturadedados.ExPilhasFilas;


import java.util.ArrayDeque;
import java.util.Deque;

public class ComparativoPerformance {

    public static void main(String[] args) {
        int operacoes = 1_000_000;

        System.out.println("Iniciando benchmark (1 milhão de operações)...");

        // 1. Aquecimento (Warm-up)
        // Essencial para a JVM otimizar o código (JIT) antes da medição real
        executarPilhaPropria(new PilhaArray<>(), operacoes);
        executarPilhaPropria(new PilhaLista<>(), operacoes);
        executarJCF(new ArrayDeque<>(), operacoes);

        // 2. Medição Real
        long tempoArray = medir(() -> executarPilhaPropria(new PilhaArray<>(), operacoes));
        long tempoLista = medir(() -> executarPilhaPropria(new PilhaLista<>(), operacoes));
        long tempoJCF = medir(() -> executarJCF(new ArrayDeque<>(), operacoes));

        // 3. Exibição dos Resultados
        System.out.println("\n--- Resultados ---");
        System.out.printf("PilhaArray (Nossa):  %.2f ms%n", tempoArray / 1e6);
        System.out.printf("PilhaLista (Nossa):  %.2f ms%n", tempoLista / 1e6);
        System.out.printf("ArrayDeque (Java):   %.2f ms%n", tempoJCF / 1e6);
        
        System.out.println("\n--- Análise Teórica ---");
        System.out.println("A PilhaArray costuma ser mais rápida pois o array armazena elementos em posições contíguas, o que favorece o cache.");
        System.out.println("A PilhaLista gasta tempo com alocação de memória (Nodes na Heap).");
        System.out.println("O ArrayDeque é otimizado pela JVM e pode ser tão rápido quanto ou mais rápido que a PilhaArray, dependendo do cenário.");
    }

    // Método auxiliar para cronometrar a execução
    private static long medir(Runnable tarefa) {
        long inicio = System.nanoTime();
        tarefa.run();
        return System.nanoTime() - inicio;
    }

    // Executa operações na nossa interface Pilha<T>
    private static void executarPilhaPropria(Pilha<Integer> pilha, int n) {
        for (int i = 0; i < n; i++) {
            pilha.push(i);
        }
        for (int i = 0; i < n; i++) {
            pilha.pop();
        }
    }

    // Executa operações na interface do Java (Deque)
    private static void executarJCF(Deque<Integer> pilha, int n) {
        for (int i = 0; i < n; i++) {
            pilha.push(i);
        }
        for (int i = 0; i < n; i++) {
            pilha.pop();
        }
    }
} 

