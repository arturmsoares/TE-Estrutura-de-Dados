import java.util.Comparator;

public class Main {

    public static void main(String[] args) {

        // Comparator ordenando por NOME
        Comparator<Produto> comparatorNome =
                (p1, p2) -> p1.getNome().compareTo(p2.getNome());

        AVL<Produto> avl = new AVL<>(comparatorNome);

        avl.inserir(new Produto("Mouse", 120));
        avl.inserir(new Produto("Teclado", 250));
        avl.inserir(new Produto("Monitor", 900));
        avl.inserir(new Produto("Headset", 300));
        avl.inserir(new Produto("Notebook", 3500));
        avl.inserir(new Produto("Caixa de Som", 450));
        avl.inserir(new Produto("Webcam", 200));

        System.out.println("Produtos em ordem alfabética:");

        System.out.println(avl.inOrder());

        System.out.println();

        System.out.println("Altura da AVL:");

        System.out.println(avl.altura());

        System.out.println();

        Produto busca = new Produto("Monitor", 0);

        System.out.println("Existe Monitor?");

        System.out.println(avl.contem(busca));
    }
}
