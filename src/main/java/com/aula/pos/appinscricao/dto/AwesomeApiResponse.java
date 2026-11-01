package com.aula.pos.appinscricao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AwesomeApiResponse(
        @JsonProperty("USDBRL") CotacaoUsdBrl USDBRL
) {
    public record CotacaoUsdBrl(String ask, String create_date) {
    }
}
