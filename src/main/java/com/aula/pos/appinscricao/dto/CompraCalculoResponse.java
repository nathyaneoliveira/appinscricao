package com.aula.pos.appinscricao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CompraCalculoResponse(
        BigDecimal valorUSD,
        BigDecimal cotacaoUtilizada,
        BigDecimal valorConvertido,
        BigDecimal taxaServico,
        BigDecimal tributoSimulado,
        BigDecimal valorFinalBRL,
        LocalDate dataCotacao,
        LocalDateTime dataCompra
) {
}
