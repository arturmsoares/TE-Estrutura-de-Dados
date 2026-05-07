/**
 * Classe de domínio simples para testar a AVL com Comparator<T>.
 *
 * Produto NÃO implementa Comparable — a ordem é definida externamente
 * pelo Comparator passado ao construtor da AVL.
 * Isso demonstra a flexibilidade de usar Comparator<T> em vez de Comparable<T>.
 */
public class Produto {

    private String nome;
    private double preco;
    private int estoque;

    public Produto(String nome, double preco, int estoque) {
        this.nome    = nome;
        this.preco   = preco;
        this.estoque = estoque;
    }

    public String getNome()    { return nome; }
    public double getPreco()   { return preco; }
    public int    getEstoque() { return estoque; }

    @Override
    public String toString() {
        return String.format("Produto{nome='%s', preco=R$%.2f, estoque=%d}",
                nome, preco, estoque);
    }
}
