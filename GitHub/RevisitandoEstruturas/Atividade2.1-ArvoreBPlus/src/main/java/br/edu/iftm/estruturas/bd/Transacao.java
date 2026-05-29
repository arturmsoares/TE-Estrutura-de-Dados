package br.edu.iftm.estruturas.bd;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade Transacao com índices B+ para demonstração
 * 
 * Atividade 2.1 - Trilha 2: Estruturas em Bancos de Dados
 * Instituto Federal do Triângulo Mineiro - Campus Uberlândia Centro
 */
@Entity
@Table(
    name = "transacoes",
    indexes = {
        // Cria árvore B+ na coluna conta_id — busca O(log n)
        @Index(name = "idx_conta_id", columnList = "conta_id"),
        // Índice composto — otimiza queries com WHERE conta_id AND data_transacao
        @Index(name = "idx_conta_data", columnList = "conta_id, data_transacao")
    }
)
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conta_id", nullable = false)
    private Long contaId;

    @Column(name = "data_transacao")
    private LocalDateTime dataTransacao;

    @Column(name = "valor", precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @Column(name = "tipo")
    private String tipo; // "DÉBITO" ou "CRÉDITO"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Constructors
    public Transacao() {
    }

    public Transacao(Long contaId, LocalDateTime dataTransacao, BigDecimal valor, String descricao, String tipo) {
        this.contaId = contaId;
        this.dataTransacao = dataTransacao;
        this.valor = valor;
        this.descricao = descricao;
        this.tipo = tipo;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContaId() {
        return contaId;
    }

    public void setContaId(Long contaId) {
        this.contaId = contaId;
    }

    public LocalDateTime getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDateTime dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Transacao{" +
                "id=" + id +
                ", contaId=" + contaId +
                ", dataTransacao=" + dataTransacao +
                ", valor=" + valor +
                ", descricao='" + descricao + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
