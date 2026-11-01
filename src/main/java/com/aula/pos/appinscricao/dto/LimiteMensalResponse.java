package com.aula.pos.appinscricao.dto;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

public record LimiteMensalResponse(
        UUID clienteId,
        YearMonth mes,
        BigDecimal limiteMensalBRL,
        BigDecimal totalUtilizadoBRL,
        BigDecimal saldoDisponivelBRL
) {
}
