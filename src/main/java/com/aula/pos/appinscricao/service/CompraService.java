package com.aula.pos.appinscricao.service;

import com.aula.pos.appinscricao.dto.CompraCalculoResponse;
import com.aula.pos.appinscricao.dto.CompraResponse;
import com.aula.pos.appinscricao.dto.LimiteMensalResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CompraService {
    private final CotacaoService cotacaoService;
    private final CompraCalculoService calculoService;
    private final CompraTransacaoService transacaoService;

    public CompraService(
            CotacaoService cotacaoService,
            CompraCalculoService calculoService,
            CompraTransacaoService transacaoService
    ) {
        this.cotacaoService = cotacaoService;
        this.calculoService = calculoService;
        this.transacaoService = transacaoService;
    }

    public CompraCalculoResponse simular(BigDecimal valorUSD) {
        return calculoService.calcular(valorUSD, cotacaoService.consultarCotacaoAtual());
    }

    public CompraResponse registrar(UUID clienteId, BigDecimal valorUSD) {
        transacaoService.validarCliente(clienteId);
        CompraCalculoResponse calculo = simular(valorUSD);
        return transacaoService.registrar(clienteId, calculo);
    }

    public List<CompraResponse> historico(UUID clienteId) {
        return transacaoService.historico(clienteId);
    }

    public LimiteMensalResponse consultarLimite(UUID clienteId) {
        return transacaoService.consultarLimite(clienteId);
    }
}
