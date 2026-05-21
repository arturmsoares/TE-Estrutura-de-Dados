package bloco1;

/**
 * Exercício 1.2 - Rastreamento manual: encadeamento separado
 * 
 * RESPOSTAS:
 * 
 * 1. Qual é o fator de carga λ = n/M após todas as inserções?
 *    λ = 7/7 = 1.0
 *    Explicação: Foram inseridas 7 chaves em uma tabela de capacidade M=7
 * 
 * 2. Qual é o comprimento da cadeia mais longa? Qual operação tem custo O(n) no pior caso quando isso acontece?
 *    A cadeia mais longa é aquela no bucket 3: [10 → 31 → 17] com comprimento 3
 *    As operações get(), put() e remove() têm custo O(n) quando a cadeia tem comprimento n
 *    No pior caso, se todas as chaves colidem no mesmo bucket, temos lista com n elementos
 * 
 * 3. Se inseríssemos a chave 38, em qual bucket ela seria alocada? Haveria colisão?
 *    h(38) = 38 % 7 = 3
 *    Sim, haveria colisão com as chaves 10, 31 e 17 que já estão no bucket 3
 * 
 * 4. A função h(k) = k % 7 distribui bem as chaves desta sequência? Proponha uma sequência de 7 chaves 
 *    que cause a pior distribuição possível para essa função.
 *    
 *    Para esta sequência [10,22,31,4,15,28,17]: a distribuição é razoável
 *    - Bucket 0: [28] (1 elemento)
 *    - Bucket 1: [22, 15] (2 elementos)
 *    - Bucket 3: [10, 31, 17] (3 elementos)
 *    - Bucket 4: [4] (1 elemento)
 *    
 *    Pior distribuição possível seria todas as chaves congruentes a k mod 7:
 *    Exemplo: [7, 14, 21, 28, 35, 42, 49]
 *    Todas caem no bucket 0, formando lista linear com 7 elementos
 *    Complexidade: get/put/remove degeneram para O(7) = O(n)
 */
public class Exercicio12Respostas {
    // Este arquivo contém apenas as respostas do exercício 1.2
}
