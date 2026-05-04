package br.edu.iftm.ed;

import org.openjdk.jmh.annotations.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(2)
public class BenchmarkRemocao {

    @Param({"100", "1000", "10000"})
    private int N;

    private ArrayList<Integer>  al;
    private LinkedList<Integer> ll;

    @Setup(Level.Invocation)
    public void setup() {
        al = new ArrayList<>();
        ll = new LinkedList<>();
        for (int i = 0; i < N; i++) { al.add(i); ll.add(i); }
    }

    @Benchmark
    public Integer removeInicioArrayList() {
        return al.remove(0);         // O(n): desloca todos
    }

    @Benchmark
    public Integer removeInicioLinkedList() {
        return ll.removeFirst();      // O(1): ajusta ponteiro
    }

    @Benchmark
    public Integer removeFinalArrayList() {
        return al.remove(al.size()-1); // O(1)
    }

    @Benchmark
    public Integer removeFinalLinkedList() {
        return ll.removeLast();        // O(1)
    }

    @Benchmark
    public Integer removeMeioArrayList() {
        return al.remove(N / 2);       // O(n/2) = O(n)
    }

    @Benchmark
    public Integer removeMeioLinkedList() {
        return ll.remove(N / 2);       // O(n/2) percurso + O(1) remoção
    }
}
