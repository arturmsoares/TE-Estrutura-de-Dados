package bloco3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Problema 3.3 - Rate limiting por IP com janela deslizante
 * 
 * Uma API REST que limita requisições por IP: máximo 100 requisições por minuto por cliente.
 * Estratégia: janela deslizante simplificada com contador e timestamp.
 * 
 * RESPOSTAS:
 * 
 * 16. Qual é a complexidade de permitir() no caso médio? E de limparExpirados() em função do
 *     número N de IPs ativos?
 *     
 *     RESPOSTA:
 *     
 *     permitir(): O(1) amortizado
 *     - HashMap.get(): O(1) amortizado
 *     - Comparação de timestamps: O(1)
 *     - Incremento de contador: O(1)
 *     - HashMap.put(): O(1) amortizado
 *     - Total: O(1)
 *     
 *     limparExpirados(): O(N)
 *     - Itera sobre N entradas do HashMap
 *     - Para cada entrada: comparação de timestamp O(1) e remoção O(1)
 *     - Total: O(N)
 * 
 * 17. Por que não se deve usar Map.entrySet().forEach() com map.remove() dentro do lambda
 *     para limpar entradas? Qual exceção seria lançada?
 *     
 *     RESPOSTA:
 *     
 *     EXCEÇÃO: ConcurrentModificationException
 *     
 *     RAZÃO:
 *     - forEach() usa um Iterator interno sobre o Map
 *     - Chamar map.remove() dentro do lambda modifica a estrutura do Map
 *     - O Iterator detecta modificação estrutural e lança ConcurrentModificationException
 *     - Isso é um mecanismo de segurança do Java Collections Framework
 *     
 *     SOLUÇÃO:
 *     1. Usar iterator.remove() explicitamente:
 *        for (Iterator<Map.Entry<...>> it = map.entrySet().iterator(); it.hasNext();) {
 *            if (condicao) it.remove();
 *        }
 *     
 *     2. Usar removeIf():
 *        map.entrySet().removeIf(e -> condicao);
 *     
 *     3. Criar lista de chaves a remover e remover depois:
 *        List<String> paraRemover = new ArrayList<>();
 *        for (Map.Entry<...> e : map.entrySet()) {
 *            if (condicao) paraRemover.add(e.getKey());
 *        }
 *        paraRemover.forEach(map::remove);
 * 
 * 18. Em um sistema com 1 milhão de IPs distintos por hora, a tabela pode crescer
 *     indefinidamente. Além de limparExpirados(), cite uma estrutura de dados do JCF que gera
 *     expiração automática por tempo de acesso e que costuma ser base para implementações
 *     de LRU cache.
 *     
 *     RESPOSTA: LinkedHashMap com accessOrder=true
 *     
 *     LinkedHashMap<String, Janela> com:
 *     - new LinkedHashMap<>(16, 0.75f, true) // true = accessOrder
 *     - Mantém ordem de inserção/acesso
 *     - Pode implementar removeEldestEntry() para limpar automaticamente
 *     
 *     Exemplo:
 *     Map<String, Janela> contadores = new LinkedHashMap<String, Janela>(16, 0.75f, true) {
 *         protected boolean removeEldestEntry(Map.Entry eldest) {
 *             long agora = System.currentTimeMillis();
 *             return agora - ((Janela)eldest.getValue()).inicioMs > 5*60_000;
 *         }
 *     };
 *     
 *     ALTERNATIVAS:
 *     - Guava's Cache: com expireAfterAccess()
 *     - Caffeine: cache moderno com expiração configurável
 *     - Cache2k: cache com políticas sofisticadas de expiração
 */
public class RateLimiter {
    private static final int LIMITE = 100;
    private static final long JANELA_MS = 60_000L; // 1 minuto em milissegundos

    // chave: IP do cliente (String)
    // valor: registro com contador e início da janela
    private final Map<String, Janela> contadores = new HashMap<>();

    /**
     * Retorna true se a requisição for permitida, false se o limite foi atingido.
     */
    public boolean permitir(String ip) {
        long agora = System.currentTimeMillis();
        Janela janela = contadores.get(ip);

        if (janela == null || agora - janela.inicioMs >= JANELA_MS) {
            // Janela expirou ou IP novo — reinicia a janela
            contadores.put(ip, new Janela(agora, 1));
            return true;
        }

        if (janela.contador < LIMITE) {
            janela.contador++;
            return true;
        }

        return false; // limite atingido
    }

    /**
     * Remove entradas de IPs cuja janela já expirou.
     * Útil para evitar crescimento ilimitado da tabela em produção.
     */
    public void limparExpirados() {
        long agora = System.currentTimeMillis();

        // Usar removeIf para evitar ConcurrentModificationException
        contadores.entrySet().removeIf(entry -> {
            Janela janela = entry.getValue();
            return agora - janela.inicioMs >= JANELA_MS;
        });
    }

    /**
     * Retorna número de IPs ativos (com janela não expirada)
     */
    public int totalIPsAtivos() {
        return contadores.size();
    }

    /**
     * Retorna número de requisições do IP
     */
    public int contadorIP(String ip) {
        Janela janela = contadores.get(ip);
        if (janela == null) {
            return 0;
        }

        long agora = System.currentTimeMillis();
        if (agora - janela.inicioMs >= JANELA_MS) {
            // Janela expirou
            return 0;
        }

        return janela.contador;
    }

    private static class Janela {
        long inicioMs;
        int contador;

        Janela(long inicioMs, int contador) {
            this.inicioMs = inicioMs;
            this.contador = contador;
        }
    }
}
