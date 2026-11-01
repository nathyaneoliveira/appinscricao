package com.aula.pos.appinscricao.service;

import com.aula.pos.appinscricao.dto.CompraCalculoResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class CompraCalculoService {
    private static final BigDecimal LIMITE_TAXA_REDUZIDA_USD = new BigDecimal("100");
    private static final BigDecimal TAXA_REDUZIDA = new BigDecimal("0.02");
    private static final BigDecimal TAXA_PADRAO = new BigDecimal("0.05");
    private static final BigDecimal TRIBUTO_SIMULADO = new BigDecimal("0.10");

    public CompraCalculoResponse calcular(
            BigDecimal valorUSD,
            CotacaoService.CotacaoAtual cotacao
    ) {
        BigDecimal valorConvertido = valorUSD.multiply(cotacao.valor()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal percentualServico = valorUSD.compareTo(LIMITE_TAXA_REDUZIDA_USD) >= 0
                ? TAXA_REDUZIDA
                : TAXA_PADRAO;
        BigDecimal taxaServico = valorConvertido.multiply(percentualServico).setScale(2, RoundingMode.HALF_UP);
        BigDecimal tributo = valorConvertido.multiply(TRIBUTO_SIMULADO).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = valorConvertido.add(taxaServico).add(tributo).setScale(2, RoundingMode.HALF_UP);

        return new CompraCalculoResponse(
                valorUSD,
                cotacao.valor(),
                valorConvertido,
                taxaServico,
                tributo,
                total,
                cotacao.dataHora().toLocalDate(),
                LocalDateTime.now()
        );
    }
}
