package bloco3;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Problema 3.2 - Índice invertido para busca de produtos
 * 
 * Um índice invertido para busca full-text em nomes de produtos.
 * 
 * RESPOSTAS:
 * 
 * 13. Qual é a complexidade de indexar() em função do número de palavras W do nome do produto?
 *     
 *     RESPOSTA: O(W)
 *     
 *     Análise:
 *     - split() em string de tamanho L: O(L)
 *     - Para cada palavra (W palavras):
 *       - toLowerCase(): O(comprimento da palavra) = O(L/W) em média
 *       - HashMap.get(): O(1) amortizado
 *       - List.add(): O(1) amortizado
 *     - Total: O(L) para split + O(W) × O(comprimento médio) + O(W) × O(1)
 *     - Simplificando: O(W) + O(L) = O(L+W) ≈ O(W) se W é predominante
 *     
 *     Na prática: O(W)
 * 
 * 14. Qual é a complexidade de buscarMultiplas() em função do número de palavras na consulta
 *     Q e do tamanho máximo de uma lista de resultados R?
 *     
 *     RESPOSTA: O(Q × R)
 *     
 *     Análise:
 *     - split() consulta: O(Q) palavras
 *     - Para cada palavra (Q palavras):
 *       - HashMap.get(): O(1)
 *       - List retorna até R elementos
 *       - retainAll() (interseção): O(R) no pior caso
 *     - Total: O(Q) × (O(1) + O(R)) = O(Q × R)
 *     
 *     Pior caso: primeira palavra tem R resultados, as outras também tem R,
 *     a interseção compara todos os R elementos de cada palavra
 * 
 * 15. HashMap<String, List<Long>> vs TreeMap<String, List<Long>>: qual escolher para este
 *     índice e por quê? Em que caso TreeMap seria preferível?
 *     
 *     RESPOSTA:
 *     
 *     ESCOLHER: HashMap
 *     Razões:
 *     - Busca é a operação principal: buscar() deve ser O(1), TreeMap seria O(log n)
 *     - Não precisa ordem: produtos não têm ordenação relevante por palavra
 *     - Performance: HashMap tem fator de carga melhor para leitura intensiva
 *     - Espaço: TreeMap tem overhead de árvore rubro-negra
 *     
 *     TREEMAP SERIA PREFERÍVEL EM CASOS:
 *     - Precise de busca por prefixo: indiceInvertido.subMap("cat", "cau")
 *     - Análise: quais palavras começam com "cat"
 *     - Relatórios: listar todas as palavras indexadas em ordem alfabética
 *     - Auto-complete: sugerir palavras que começam com digitação do usuário
 *     - Auditoria: listar palavras em ordem determinística para logs
 */
public class IndiceInvertido {
    // chave: palavra normalizada (minúscula, sem acentos)
    // valor: lista de IDs de produtos que contêm a palavra
    private final Map<String, List<Long>> indice = new HashMap<>();

    /**
     * Indexa o produto.
     * Normaliza o nome antes de dividir em palavras.
     */
    public void indexar(long idProduto, String nomeProduto) {
        if (nomeProduto == null || nomeProduto.isEmpty()) {
            return;
        }

        // Normalizar: lowercase e dividir por espaços
        String[] palavras = nomeProduto.toLowerCase().split("\\s+");

        for (String palavra : palavras) {
            if (palavra.isEmpty()) {
                continue;
            }

            // Obter ou criar lista de IDs para essa palavra
            List<Long> ids = indice.computeIfAbsent(palavra, k -> new ArrayList<>());

            // Adicionar ID do produto se não estiver na lista
            if (!ids.contains(idProduto)) {
                ids.add(idProduto);
            }
        }
    }

    /**
     * Retorna a lista de IDs que contêm a palavra, ou lista vazia se não encontrada.
     */
    public List<Long> buscar(String palavra) {
        if (palavra == null || palavra.isEmpty()) {
            return new ArrayList<>();
        }

        String palavraNormalizada = palavra.toLowerCase();
        List<Long> resultado = indice.get(palavraNormalizada);

        return resultado != null ? new ArrayList<>(resultado) : new ArrayList<>();
    }

    /**
     * Retorna os IDs que aparecem em TODAS as palavras da consulta (interseção).
     * Exemplo: buscar("cadeira gamer") → [3]
     */
    public List<Long> buscarMultiplas(String consulta) {
        if (consulta == null || consulta.isEmpty()) {
            return new ArrayList<>();
        }

        String[] palavras = consulta.toLowerCase().split("\\s+");

        if (palavras.length == 0) {
            return new ArrayList<>();
        }

        // Começar com resultado da primeira palavra
        List<Long> resultado = new ArrayList<>(buscar(palavras[0]));

        // Interseção com resultados das outras palavras
        for (int i = 1; i < palavras.length; i++) {
            List<Long> resultadoPalavra = buscar(palavras[i]);
            resultado.retainAll(resultadoPalavra);

            // Otimização: se resultado ficar vazio, pode parar
            if (resultado.isEmpty()) {
                break;
            }
        }

        return resultado;
    }

    public int totalPalavrasIndexadas() {
        return indice.size();
    }

    /**
     * Retorna todos os IDs de produtos indexados
     */
    public List<Long> todosProdutos() {
        List<Long> todos = new ArrayList<>();
        for (List<Long> ids : indice.values()) {
            for (Long id : ids) {
                if (!todos.contains(id)) {
                    todos.add(id);
                }
            }
        }
        return todos;
    }
}
