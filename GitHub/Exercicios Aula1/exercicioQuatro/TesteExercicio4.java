package exercicioQuatro;

public class TesteExercicio4 {
    public static void main(String[] args) {
        Container<String> sacola = new ContainerSimples<>(5);
        
        sacola.adicionar("Java");
        sacola.adicionar("Estrutura de Dados");
        
        System.out.println("Tamanho atual: " + sacola.tamanho());
        System.out.println("Removido: " + sacola.remover());
    }
}