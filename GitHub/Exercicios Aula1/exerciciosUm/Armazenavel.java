package exerciciosUm;

public interface Armazenavel<T> {
    void guardar(T item);
    T recuperar();
}

class Caixa<T> implements Armazenavel<T> {
    private T conteudo; // O tipo T é definido na instanciação
    @Override
    public void guardar(T item) {
        this.conteudo = item;
    }

    @Override
    public T recuperar() {
        return conteudo;
    }
}

class TesteExercicio1 {
    public static void main(String[] args) {
        // Criando uma Caixa para Strings
        Caixa<String> caixaTexto = new Caixa<>();
        caixaTexto.guardar("Estrutura de Dados");
        System.out.println("Conteúdo da caixa de texto: " + caixaTexto.recuperar());

        // Criando uma Caixa para Inteiros
        Caixa<Integer> caixaNumero = new Caixa<>();
        caixaNumero.guardar(2026);
        System.out.println("Conteúdo da caixa numérica: " + caixaNumero.recuperar());

        caixaTexto.guardar(10); 
        // O erro acima ocorre pois o Java garante a segurança de tipos Type-safety
    }
}

// 
// Tente guardar um Integer em uma Caixa<String>. O que acontece?
// Ocorrerá um erro de compilação. Graças ao Type-safety dos Generics, o Java 
// impede que tipos incompatíveis sejam misturados, evitando erros em tempo de execução (runtime).