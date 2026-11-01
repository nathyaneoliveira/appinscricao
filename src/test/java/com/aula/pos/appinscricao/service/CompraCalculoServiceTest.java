package com.aula.pos.appinscricao.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompraCalculoServiceTest {
    private final CompraCalculoService service = new CompraCalculoService();
    private final CotacaoService.CotacaoAtual cotacao = new CotacaoService.CotacaoAtual(
            new BigDecimal("5.4000"),
            LocalDateTime.of(2026, 10, 16, 17, 0)
    );

    @Test
    void calculaCompraDeCemDolaresOuMaisComTaxaDeDoisPorCento() {
        var resultado = service.calcular(new BigDecimal("200.00"), cotacao);

        assertEquals(new BigDecimal("1080.00"), resultado.valorConvertido());
        assertEquals(new BigDecimal("21.60"), resultado.taxaServico());
        assertEquals(new BigDecimal("108.00"), resultado.tributoSimulado());
        assertEquals(new BigDecimal("1209.60"), resultado.valorFinalBRL());
        assertEquals(cotacao.dataHora().toLocalDate(), resultado.dataCotacao());
    }

    @Test
    void calculaCompraAbaixoDeCemDolaresComTaxaDeCincoPorCento() {
        var resultado = service.calcular(new BigDecimal("99.99"), cotacao);

        assertEquals(new BigDecimal("539.95"), resultado.valorConvertido());
        assertEquals(new BigDecimal("27.00"), resultado.taxaServico());
        assertEquals(new BigDecimal("54.00"), resultado.tributoSimulado());
        assertEquals(new BigDecimal("620.95"), resultado.valorFinalBRL());
    }

    @Test
    void aplicaTaxaReduzidaExatamenteNoLimiteDeCemDolares() {
        var resultado = service.calcular(new BigDecimal("100.00"), cotacao);

        assertEquals(new BigDecimal("10.80"), resultado.taxaServico());
    }
}
