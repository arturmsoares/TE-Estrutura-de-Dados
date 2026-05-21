package bloco2;

/**
 * Exercício 2.1 - Rastreamento manual: sondagem linear e quadrática
 * 
 * RESPOSTAS PARTE A - Sondagem linear: h(k, i) = (h(k) + i) % M
 * 
 * Sequência: inserir(20), inserir(31), inserir(54), inserir(43), inserir(65), inserir(9)
 * Capacidade M = 11, função hash base: h(k) = k % 11
 * 
 * Tabela final após todas as inserções:
 * Slot 0: 54
 * Slot 1: 43
 * Slot 2: 65
 * Slot 3: 9
 * Slot 9: 20
 * Slot 10: 31
 * 
 * Detalhamento das últimas linhas:
 * 
 * Chave 65: h(65) = 65 % 11 = 10
 *   - Sonda i=0: slot 10 ocupado (tem 31)
 *   - Sonda i=1: slot 0 ocupado (tem 54)
 *   - Sonda i=2: slot 1 ocupado (tem 43)
 *   - Sonda i=3: slot 2 livre → insere em 2
 * 
 * Chave 9: h(9) = 9 % 11 = 9
 *   - Sonda i=0: slot 9 ocupado (tem 20)
 *   - Sonda i=1: slot 10 ocupado (tem 31)
 *   - Sonda i=2: slot 0 ocupado (tem 54)
 *   - Sonda i=3: slot 1 ocupado (tem 43)
 *   - Sonda i=4: slot 2 ocupado (tem 65)
 *   - Sonda i=5: slot 3 livre → insere em 3
 * 
 * ---
 * 
 * PERGUNTA 5: Qual é o fenômeno que ocorre quando chaves com o mesmo h(k) inicial formam um 
 * bloco contíguo? Como a sondagem quadrática atenua esse problema?
 * 
 * RESPOSTA:
 * O fenômeno chama-se CLUSTERING PRIMÁRIO (primary clustering)
 * 
 * Quando várias chaves colidem no mesmo hash inicial, elas formam um bloco contíguo de ocupação.
 * Qualquer nova chave cujo h(k) aponte para esse bloco, tentará os slots sequentes e aumentará
 * o tamanho do cluster. Isso degrada o desempenho exponencialmente.
 * 
 * Exemplo: se slots 5,6,7,8 estão ocupados e uma nova chave hash para 5, ela verificará 6,7,8,9...
 * Se o cluster está grande, demora muitas sondas para encontrar espaço.
 * 
 * SONDAGEM QUADRÁTICA reduz esse problema usando h(k, i) = (h(k) + i²) % M
 * Ao invés de avançar 1, 2, 3, 4... avança 1, 4, 9, 16...
 * Assim, as sondas "pulam" mais longe, tendo menos chance de permanecer dentro do cluster.
 * 
 * PORÉM, quadrática ainda sofre de SECONDARY CLUSTERING (agrupamentos menores e mais dispersos),
 * mas é bem melhor que linear.
 * 
 * ---
 * 
 * PERGUNTA 6: Calcule o fator de carga após todas as inserções. O valor está dentro do limite
 * recomendado para endereçamento aberto?
 * 
 * RESPOSTA:
 * Fator de carga λ = n/M = 6/11 ≈ 0.545 (54.5%)
 * 
 * SIM, está dentro do limite recomendado.
 * Para endereçamento aberto, o recomendado é:
 * - λ < 0.5 para desempenho excelente
 * - 0.5 ≤ λ < 0.7 para desempenho bom (como nosso caso)
 * - λ ≥ 0.7 risco alto de clustering e deve fazer rehashing
 * 
 * No nosso caso, λ ≈ 0.545 está na zona segura.
 */
public class Exercicio21Respostas {
}
