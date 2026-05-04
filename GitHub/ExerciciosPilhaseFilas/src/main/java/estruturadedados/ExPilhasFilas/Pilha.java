package estruturadedados.ExPilhasFilas;



public interface Pilha<T> {

  /** Empilha elemento no topo. O(1) amortizado. */
  void push(T elemento);

  /** Remove e retorna o topo. O(1). Lança EmptyStackException se vazia. */
  T pop();

  /** Retorna o topo sem remover. O(1). */
  T peek();

  /** true se não houver elementos. */
  boolean isEmpty();

  /** Número de elementos. */
  int size();
}
