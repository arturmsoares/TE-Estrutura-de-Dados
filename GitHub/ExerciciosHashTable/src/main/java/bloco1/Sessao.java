package bloco1;

import java.util.Objects;

/**
 * Exercício 1.1 - Classe B: Sessao
 * PROBLEMA: hashCode() sempre retorna 42 (mesma constante)
 * 
 * EXPLICAÇÃO DO PROBLEMA:
 * - Retornar uma constante em hashCode() é permitido, mas MUITO ruim para desempenho
 * - Força todas as chaves para o MESMO bucket, causando lista encadeada gigante
 * - Degrada a tabela hash para complexidade O(n) ao invés de O(1)
 * - Isso viola o princípio de distribuição uniforme das funções hash
 * 
 * SOLUÇÃO: Implementar hashCode() que distribui bem os valores baseado em token
 */
public class Sessao {
    private final String token;

    public Sessao(String token) {
        this.token = token;
    }

    @Override
    public int hashCode() {
        // CORRIGIDO: usar Objects.hash() com token para distribuição uniforme
        return Objects.hash(token);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sessao)) return false;
        return token.equals(((Sessao) o).token);
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "Sessao{" +
                "token='" + token + '\'' +
                '}';
    }
}
