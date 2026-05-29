package br.edu.iftm.estruturas.benchmark;

import java.util.*;

/**
 * Aplicação para testar GC (Garbage Collection)
 * 
 * Atividade 1.3 - Trilha 1: Interpretando GC Logs
 * Instituto Federal do Triângulo Mineiro - Campus Uberlândia Centro
 * 
 * Como executar:
 * javac src/main/java/br/edu/iftm/estruturas/benchmark/GCTestApplication.java
 * java -Xlog:gc*:file=gc.log:time,uptime br.edu.iftm.estruturas.benchmark.GCTestApplication
 * 
 * Ou com flags adicionais:
 * java -Xms256m -Xmx256m -Xlog:gc*:file=gc.log:time,uptime br.edu.iftm.estruturas.benchmark.GCTestApplication
 */
public class GCTestApplication {
    
    private static final int ITERATIONS = 10_000;
    private static final int OBJECTS_PER_ITERATION = 1_000;
    private static List<byte[]> longLivedObjects = new ArrayList<>();
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("========================================");
        System.out.println("GC Test Application - Trilha 1 Atividade 1.3");
        System.out.println("========================================");
        System.out.println();
        
        // Exibe informações de memória
        printMemoryInfo();
        
        System.out.println("Iniciando teste de GC...");
        System.out.println("Criando objetos de vida curta...");
        System.out.println();
        
        long startTime = System.currentTimeMillis();
        
        // Fase 1: Criar muitos objetos de vida curta
        phase1_ShortLivedObjects();
        
        System.out.println();
        System.out.println("Criando objetos de vida longa...");
        
        // Fase 2: Criar alguns objetos de vida longa
        phase2_LongLivedObjects();
        
        System.out.println();
        System.out.println("Aguardando 10 segundos antes de sair...");
        Thread.sleep(10_000);
        
        long endTime = System.currentTimeMillis();
        
        System.out.println();
        System.out.println("========================================");
        System.out.println("Teste concluído em " + (endTime - startTime) + "ms");
        printMemoryInfo();
        System.out.println("========================================");
    }
    
    /**
     * Fase 1: Cria muitos objetos de vida curta (Alto GC pressure)
     * Isso força o GC a trabalhar bastante
     */
    private static void phase1_ShortLivedObjects() {
        for (int i = 0; i < ITERATIONS; i++) {
            // Cria muitos arrays pequenos que serão descartados
            List<byte[]> temporaryObjects = new ArrayList<>();
            
            for (int j = 0; j < OBJECTS_PER_ITERATION; j++) {
                // Cria arrays de 1KB cada
                byte[] array = new byte[1024];
                temporaryObjects.add(array);
            }
            
            // Simula processamento
            long sum = 0;
            for (byte[] obj : temporaryObjects) {
                sum += obj.length;
            }
            
            // Descarta os objetos (garbage)
            temporaryObjects.clear();
            
            if ((i + 1) % 1000 == 0) {
                System.out.println("  Iteração " + (i + 1) + "/" + ITERATIONS);
                printMemoryInfo();
            }
        }
    }
    
    /**
     * Fase 2: Cria objetos que vivem até o final do programa
     * Isso mantém heap ocupado
     */
    private static void phase2_LongLivedObjects() {
        for (int i = 0; i < 50; i++) {
            // Cria arrays que vivem até o final
            byte[] persistent = new byte[10_000]; // 10KB cada
            longLivedObjects.add(persistent);
        }
        
        System.out.println("  Criados " + longLivedObjects.size() + " objetos persistentes");
        printMemoryInfo();
    }
    
    /**
     * Exibe informações de memória
     */
    private static void printMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / (1024 * 1024);
        long totalMemory = runtime.totalMemory() / (1024 * 1024);
        long freeMemory = runtime.freeMemory() / (1024 * 1024);
        long usedMemory = totalMemory - freeMemory;
        
        System.out.printf("  Memória: usado=%dMB, livre=%dMB, total=%dMB, máx=%dMB%n",
                usedMemory, freeMemory, totalMemory, maxMemory);
    }
}
