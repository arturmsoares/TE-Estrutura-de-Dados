package estruturadedados.ExPilhasFilas;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    private static final int MAX_REQS = 10;
    private static final long JANELA_MS = 60_000L;

    // ConcurrentHashMap: leituras e escritas thread-safe sem bloquear o mapa
    // inteiro
    private final Map<String, Deque<Long>> historico = new ConcurrentHashMap<>();

    public boolean permitir(String userId) {
        long agora = System.currentTimeMillis();
        Deque<Long> fila = historico.computeIfAbsent(userId, k -> new ArrayDeque<>());

        // Remove timestamps fora da janela de 1 minuto
        // peekFirst() antes de pollFirst(): evita remover sem verificar
        while (!fila.isEmpty() && agora - fila.peekFirst() > JANELA_MS) {
            fila.pollFirst(); // O(1)
        }

        if (fila.size() < MAX_REQS) {
            fila.addLast(agora); // O(1)
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        RateLimiterService limiter = new RateLimiterService();
        String usuario = "artur_machado";

        for (int i = 1; i <= 15; i++) {
            boolean permitido = limiter.permitir(usuario);
            System.out.println("Requisição " + i + ": " + (permitido ? "OK" : "BLOQUEADO"));
        }
    }
}

/*  (a) Por que peekFirst() é chamado antes de pollFirst()? 

Porque é preciso rimeiro checar se o timestamp mais antigo já expirou. 
Se chamássemos pollFirst() (ou dequeue()) direto, removeríamos o elemento mesmo que ele 
ainda estivesse dentro do tempo permitido

(b) Por que ConcurrentHashMap em vez de HashMap? 

O ConcurrentHashMap permite que múltiplas threads leiam e escrevam no mapa de históricos 
sem corromper os dados e sem travar o sistema inteiro, garantindo a thread-safety

(c) O que acontece com os dados de usuários que pararam de fazer requisições — 
o mapa cresce indefinidamente?

Sim, o mapa pode crescer indefinidamente se muitos usuários fizerem requisições e depois pararem.
 */