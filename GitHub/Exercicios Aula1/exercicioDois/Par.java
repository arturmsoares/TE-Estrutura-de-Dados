package exercicioDois;

public class Par<A, B> {
    private A primeiro;
    private B segundo;

    public Par(A primeiro, B segundo) {
        this.primeiro = primeiro;
        this.segundo = segundo;
    }

    public A getPrimeiro() { return primeiro; }
    public B getSegundo() { return segundo; }

    @Override
    public String toString() {
        return "Par[" + primeiro + ", " + segundo + "]";
    }
}
