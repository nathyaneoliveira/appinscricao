package com.aula.pos.appinscricao.client;

import com.aula.pos.appinscricao.dto.AwesomeApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "awesomeApiClient", url = "${integrations.awesomeapi.url}")
public interface AwesomeApiClient {
    @GetMapping("/json/last/USD-BRL")
    AwesomeApiResponse buscarCotacaoUsdBrl();
}
