package bloco2;

/**
 * Exercício 2.2 - Double hashing
 * 
 * Tabela de capacidade M = 11
 * Funções de sondagem:
 *   h1(k) = k % 11
 *   h2(k) = 7 - (k % 7)
 *   h(k, i) = (h1(k) + i * h2(k)) % 11
 * 
 * Sequência: inserir(76), inserir(40), inserir(48), inserir(5), inserir(55), inserir(47)
 * 
 * RESULTADO DAS INSERÇÕES:
 * 
 * Chave 76:
 *   h1(76) = 76 % 11 = 10
 *   h2(76) = 7 - (76 % 7) = 7 - 6 = 1
 *   i=0: (10 + 0*1) % 11 = 10 → LIVRE → Insere em slot 10
 * 
 * Chave 40:
 *   h1(40) = 40 % 11 = 7
 *   h2(40) = 7 - (40 % 7) = 7 - 5 = 2
 *   i=0: (7 + 0*2) % 11 = 7 → LIVRE → Insere em slot 7
 * 
 * Chave 48:
 *   h1(48) = 48 % 11 = 4
 *   h2(48) = 7 - (48 % 7) = 7 - 6 = 1
 *   i=0: (4 + 0*1) % 11 = 4 → LIVRE → Insere em slot 4
 * 
 * Chave 5:
 *   h1(5) = 5 % 11 = 5
 *   h2(5) = 7 - (5 % 7) = 7 - 5 = 2
 *   i=0: (5 + 0*2) % 11 = 5 → LIVRE → Insere em slot 5
 * 
 * Chave 55:
 *   h1(55) = 55 % 11 = 0
 *   h2(55) = 7 - (55 % 7) = 7 - 6 = 1
 *   i=0: (0 + 0*1) % 11 = 0 → LIVRE → Insere em slot 0
 * 
 * Chave 47:
 *   h1(47) = 47 % 11 = 3
 *   h2(47) = 7 - (47 % 7) = 7 - 5 = 2
 *   i=0: (3 + 0*2) % 11 = 3 → LIVRE → Insere em slot 3
 * 
 * TABELA FINAL:
 * Slot 0: 55
 * Slot 3: 47
 * Slot 4: 48
 * Slot 5: 5
 * Slot 7: 40
 * Slot 10: 76
 * 
 * OBS: Nenhuma colisão ocorreu nesta sequência! Todas as chaves encontraram slot livre na primeira sonda.
 */
public class Exercicio22Respostas {
}
