package br.edu.iftm.estruturas.benchmark;

import org.openjdk.jmh.annotations.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark comparando desempenho de estruturas de dados
 * 
 * Atividade 1.1 e 1.2 - Trilha 1: Benchmarking com JMH
 * Instituto Federal do Triângulo Mineiro - Campus Uberlândia Centro
 * 
 * Testes:
 * - HashMap vs TreeMap (Atividade 1.1)
 * - ArrayList vs LinkedList (Atividade 1.2)
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(2)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class MapBenchmark {

    private static final int SIZE = 10_000;
    private static final int SEARCH_KEY = 5_000;
    private static final int LIST_MIDDLE_INDEX = SIZE / 2; // 5.000
    
    private HashMap<Integer, String> hashMap;
    private TreeMap<Integer, String> treeMap;
    private ArrayList<Integer> arrayList;
    private LinkedList<Integer> linkedList;
    
    /**
     * Setup: inicializa os mapas e listas com dados
     */
    @Setup(Level.Trial)
    public void setup() {
        hashMap = new HashMap<>();
        treeMap = new TreeMap<>();
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
        
        // Insere 10.000 elementos em todas as estruturas
        for (int i = 0; i < SIZE; i++) {
            // Maps
            String value = "Valor_" + i;
            hashMap.put(i, value);
            treeMap.put(i, value);
            
            // Lists
            arrayList.add(i);
            linkedList.add(i);
        }
    }
    
    /**
     * Benchmark: Get no HashMap
     * Esperado: O(1) em média
     */
    @Benchmark
    public String hashMapGet() {
        return hashMap.get(SEARCH_KEY);
    }
    
    /**
     * Benchmark: Get no TreeMap
     * Esperado: O(log n)
     */
    @Benchmark
    public String treeMapGet() {
        return treeMap.get(SEARCH_KEY);
    }
    
    /**
     * Benchmark: Range scan no TreeMap
     * Esperado: O(log n + k) onde k é o número de elementos retornados
     */
    @Benchmark
    public int treeMapRange() {
        // Retorna todos os valores entre 5000 e 5100
        SortedMap<Integer, String> subMap = treeMap.subMap(5000, 5100);
        return subMap.size();
    }
    
    /**
     * Benchmark: Get no ArrayList
     * Esperado: O(1) — acesso direto ao índice
     */
    @Benchmark
    public Integer arrayListGet() {
        return arrayList.get(LIST_MIDDLE_INDEX);
    }
    
    /**
     * Benchmark: Get no LinkedList
     * Esperado: O(n) — percorre lista até o índice
     * Para n=10.000 e índice=5.000, percorre ~5.000 nós
     */
    @Benchmark
    public Integer linkedListGet() {
        return linkedList.get(LIST_MIDDLE_INDEX);
    }
}
