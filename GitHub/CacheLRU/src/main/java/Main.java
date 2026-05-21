import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Capacidade fixa do cache
        LRUCache cache = new LRUCache(3);

        System.out.println("====================================");
        System.out.println("       SIMULAÇÃO DE CACHE LRU");
        System.out.println("====================================");


        cache.put("A", "Notebook");
        cache.put("B", "Mouse");
        cache.put("C", "Teclado");

        System.out.println("\nCache inicial:");
        exibirEstado(cache);

        /*
         * Acesso para alterar ordem
         */
        System.out.println("\nAcesse uma determinada chave:");
        String chaveAcessada = scanner.nextLine();
        cache.get(chaveAcessada);

        System.out.println("\nEstado após get():");
        exibirEstado(cache);


        System.out.println("\n====================================");
        System.out.println("O cache está cheio.");
        System.out.println("A próxima inserção causará EVIÇÃO.");
        System.out.println("====================================");

        System.out.print("\nDigite a nova chave: ");
        String novaChave = scanner.nextLine();

        System.out.print("Digite o valor da chave: ");
        String novoValor = scanner.nextLine();

        System.out.println("\n--- ANTES DA INSERÇÃO ---");
        exibirEstado(cache);

        System.out.println("\nElemento menos recente:");
        System.out.println(cache.ordemDeAcesso().get(0));

        /*
         * Inserção que excede a capacidade
         */
        System.out.println("\nInserindo novo elemento...");
        cache.put(novaChave, novoValor);


        System.out.println("\n--- DEPOIS DA INSERÇÃO ---");
        cache.exibirCache();;

        System.out.println("\nObserve que o elemento menos recente foi removido automaticamente.");

        scanner.close();
    }


    private static void exibirEstado(LRUCache cache) {

        System.out.println("Ordem do cache:");
        System.out.println(cache.ordemDeAcesso());

        System.out.println("\nLegenda:");
        System.out.println("- Primeiro elemento = menos recente");
        System.out.println("- Último elemento = mais recente");

        System.out.println("\nTamanho atual:");
        System.out.println(cache.size());
    }
}