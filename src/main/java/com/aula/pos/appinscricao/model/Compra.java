package com.aula.pos.appinscricao.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_compras", indexes = {
        @Index(name = "idx_compra_cliente_data", columnList = "cliente_id, data_compra")
})
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "valor_usd", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorUSD;

    @Column(name = "cotacao_utilizada", nullable = false, precision = 19, scale = 6)
    private BigDecimal cotacaoUtilizada;

    @Column(name = "valor_convertido", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorConvertido;

    @Column(name = "taxa_servico", nullable = false, precision = 19, scale = 2)
    private BigDecimal taxaServico;

    @Column(name = "tributo_simulado", nullable = false, precision = 19, scale = 2)
    private BigDecimal tributoSimulado;

    @Column(name = "valor_final_brl", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorFinalBRL;

    @Column(name = "data_cotacao", nullable = false)
    private LocalDate dataCotacao;

    @Column(name = "data_compra", nullable = false)
    private LocalDateTime dataCompra;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    protected Compra() {
    }

    public Compra(
            BigDecimal valorUSD,
            BigDecimal cotacaoUtilizada,
            BigDecimal valorConvertido,
            BigDecimal taxaServico,
            BigDecimal tributoSimulado,
            BigDecimal valorFinalBRL,
            LocalDate dataCotacao,
            LocalDateTime dataCompra,
            Cliente cliente
    ) {
        this.valorUSD = valorUSD;
        this.cotacaoUtilizada = cotacaoUtilizada;
        this.valorConvertido = valorConvertido;
        this.taxaServico = taxaServico;
        this.tributoSimulado = tributoSimulado;
        this.valorFinalBRL = valorFinalBRL;
        this.dataCotacao = dataCotacao;
        this.dataCompra = dataCompra;
        this.cliente = cliente;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getValorUSD() {
        return valorUSD;
    }

    public BigDecimal getCotacaoUtilizada() {
        return cotacaoUtilizada;
    }

    public BigDecimal getValorConvertido() {
        return valorConvertido;
    }

    public BigDecimal getTaxaServico() {
        return taxaServico;
    }

    public BigDecimal getTributoSimulado() {
        return tributoSimulado;
    }

    public BigDecimal getValorFinalBRL() {
        return valorFinalBRL;
    }

    public LocalDate getDataCotacao() {
        return dataCotacao;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
