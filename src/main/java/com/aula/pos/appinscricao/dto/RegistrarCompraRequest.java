package com.aula.pos.appinscricao.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record RegistrarCompraRequest(
        @NotNull(message = "O id do cliente é obrigatório")
        UUID clienteId,
        @NotNull(message = "O valor em USD é obrigatório")
        @Positive(message = "O valor em USD deve ser maior que zero")
        @Digits(integer = 15, fraction = 2, message = "O valor em USD deve ter no máximo 2 casas decimais")
        BigDecimal valorUSD
) {
}
