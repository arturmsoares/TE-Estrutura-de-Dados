package exercicioDois;

public class TestePar {
    public static void main(String[] args) {

        // 1. Criando um Par para representar (nome, idade)
        Par<String, Integer> aluno = new Par<>("João Silva", 20);
        System.out.println("Dados do Aluno: " + aluno.toString());
        System.out.println("Nome: " + aluno.getPrimeiro());
        System.out.println("Idade: " + aluno.getSegundo());

        System.out.println("---");

        // 2. Criando um Par com dados compostos (Par dentro de Par)
        Par<Integer, Double> coordenadas = new Par<>(10, 25.5);
        Par<String, Par<Integer, Double>> pontoNoMapa = new Par<>("Local A", coordenadas);
        
        System.out.println("Representação Composta: " + pontoNoMapa.toString());
        System.out.println("ID do Local: " + pontoNoMapa.getPrimeiro());
        System.out.println("Coordenada X: " + pontoNoMapa.getSegundo().getPrimeiro());
    }
}