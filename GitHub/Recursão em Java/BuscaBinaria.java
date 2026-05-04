public class BuscaBinaria {

    public static int buscaBinaria(int[] arr, int inicio, int fim, int alvo) {
    if (inicio > fim) {
        return -1; // não encontrado
    }

    int meio = (inicio + fim) / 2;

    if (arr[meio] == alvo) {
        return meio;
    } else if (alvo < arr[meio]) {
        return buscaBinaria(arr, inicio, meio - 1, alvo);
    } else {
        return buscaBinaria(arr, meio + 1, fim, alvo);
    }
}

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int alvo = 5;
        int resultado = buscaBinaria(arr, 0, arr.length - 1, alvo);
        if (resultado != -1) {
            System.out.println("Elemento encontrado no índice: " + resultado);
        } else {
            System.out.println("Elemento não encontrado.");
        }
    }
}
