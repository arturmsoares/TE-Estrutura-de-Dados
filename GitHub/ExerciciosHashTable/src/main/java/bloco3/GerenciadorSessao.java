package bloco3;

import bloco1.TabelaHashEncadeada;

/**
 * Problema 3.1 - Cache de sessões HTTP
 * 
 * Um sistema de autenticação que armazena tokens de sessão em memória com acesso O(1).
 * Usa TabelaHashEncadeada<String, Usuario> para armazenar sessões.
 * 
 * RESPOSTAS:
 * 
 * 10. Qual é a complexidade esperada (caso médio) de registrar(), buscar() e invalidar() com
 *     encadeamento separado e fator de carga λ ≤ 0,75?
 *     
 *     RESPOSTA: O(1) amortizado
 *     
 *     Com λ ≤ 0.75:
 *     - Tamanho médio de cada cadeia = λ = 0.75
 *     - Operações: procurar, inserir e remover na cadeia levam O(λ) = O(0.75) = O(1)
 *     - Para chaves bem distribuídas, a complexidade é O(1) amortizado
 * 
 * 11. Por que String é uma boa escolha como chave de tabela hash em Java? Quais propriedades 
 *     da classe String garantem isso?
 *     
 *     RESPOSTA: String é excelente escolha porque:
 *     - Imutável: uma vez criada, não muda. Garante que hashCode() nunca muda
 *     - hashCode() bem distribuído: usa algoritmo que distribui bem os bits
 *     - equals() implementado corretamente: compara conteúdo, não referência
 *     - Contrato hashCode/equals respeitado: se s1.equals(s2) então s1.hashCode() == s2.hashCode()
 *     - Thread-safe: por ser imutável, múltiplas threads podem acessar sem sincronização
 * 
 * 12. Em produção, esse cache em memória apresentaria qual problema crítico em ambientes 
 *     com múltiplos servidores? Qual tecnologia resolveria isso?
 *     
 *     RESPOSTA:
 *     PROBLEMA: Falta de compartilhamento entre servidores
 *     - Cada servidor tem seu próprio cache em memória local
 *     - Se servidor A registra sessão com token T, servidor B não conhece T
 *     - Requisição pode ser roteada para servidor B, que invalida a sessão
 *     
 *     SOLUÇÃO: Cache distribuído (Redis, Memcached, etc)
 *     - Redis: cache em memória compartilhado, acesso O(1), expiration automática
 *     - Permite múltiplos servidores compartilharem sessões
 *     - Garante consistência: uma única fonte da verdade para sessões
 */
public class GerenciadorSessao {
    private final TabelaHashEncadeada<String, Usuario> sessoes;

    public GerenciadorSessao() {
        // Capacidade inicial: 64
        // Justificativa: para um sistema pequeno/médio com ~50 sessões simultâneas,
        // λ = 50/64 ≈ 0.78 (próximo ao limite de 0.75 para encadeamento separado)
        // Se crescer além, HashMap poderia fazer rehashing, mas usamos tamanho fixo aqui
        this.sessoes = new TabelaHashEncadeada<>(64);
    }

    /**
     * Registra nova sessão.
     * Lança IllegalArgumentException se token for nulo ou vazio.
     */
    public void registrar(String token, Usuario usuario) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token não pode ser nulo ou vazio");
        }
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario não pode ser nulo");
        }
        sessoes.put(token, usuario);
    }

    /**
     * Retorna o Usuario ou null se a sessão não existir.
     */
    public Usuario buscar(String token) {
        return sessoes.get(token);
    }

    /**
     * Remove a sessão.
     * Retorna true se existia, false se já estava expirada/inválida.
     */
    public boolean invalidar(String token) {
        return sessoes.remove(token);
    }

    /**
     * Retorna true se o token ainda é válido.
     */
    public boolean eValido(String token) {
        return sessoes.get(token) != null;
    }

    public int totalSessoes() {
        return sessoes.tamanho();
    }
}
