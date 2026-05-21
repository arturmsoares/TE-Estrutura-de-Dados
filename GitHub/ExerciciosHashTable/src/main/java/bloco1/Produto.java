package bloco1;

import java.util.Objects;

/**
 * Exercício 1.1 - Classe A: Produto
 * PROBLEMA: hashCode() não foi sobrescrito
 * 
 * EXPLICAÇÃO DO PROBLEMA:
 * - Quando equals() é sobrescrito, hashCode() DEVE ser sobrescrito também
 * - Se dois objetos são iguais (equals() retorna true), devem ter o mesmo hashCode()
 * - HashMap e HashSet usam hashCode() para encontrar o bucket, depois usam equals() para confirmação
 * - Se Produto1.equals(Produto2) = true, mas hashCode() é diferente, podem ser inseridos 2 vezes na tabela
 * - Isso viola o contrato HashMap/HashSet
 * 
 * SOLUÇÃO: Implementar hashCode() baseado nos mesmos campos de equals()
 */
public class Produto {
    private String sku;
    private String nome;

    public Produto(String sku, String nome) {
        this.sku = sku;
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produto)) return false;
        Produto p = (Produto) o;
        return sku.equals(p.sku);
    }

    @Override
    public int hashCode() {
        // CORRIGIDO: usar Objects.hash() com os mesmos campos de equals()
        return Objects.hash(sku);
    }

    public String getSku() {
        return sku;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "sku='" + sku + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}
