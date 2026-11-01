package com.aula.pos.appinscricao.service;

import com.aula.pos.appinscricao.dto.CompraCalculoResponse;
import com.aula.pos.appinscricao.dto.CompraResponse;
import com.aula.pos.appinscricao.dto.LimiteMensalResponse;
import com.aula.pos.appinscricao.exception.CompraLimiteExcedidoException;
import com.aula.pos.appinscricao.exception.ResourceNotFoundException;
import com.aula.pos.appinscricao.model.Cliente;
import com.aula.pos.appinscricao.model.Compra;
import com.aula.pos.appinscricao.repository.ClienteRepository;
import com.aula.pos.appinscricao.repository.CompraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class CompraTransacaoService {
    private static final BigDecimal LIMITE_MENSAL = new BigDecimal("10000.00");

    private final ClienteRepository clienteRepository;
    private final CompraRepository compraRepository;

    public CompraTransacaoService(ClienteRepository clienteRepository, CompraRepository compraRepository) {
        this.clienteRepository = clienteRepository;
        this.compraRepository = compraRepository;
    }

    @Transactional
    public CompraResponse registrar(UUID clienteId, CompraCalculoResponse calculo) {
        Cliente cliente = clienteRepository.findByIdForUpdate(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(clienteId));

        YearMonth mesAtual = YearMonth.from(calculo.dataCompra());
        LocalDateTime inicio = mesAtual.atDay(1).atStartOfDay();
        LocalDateTime fim = mesAtual.plusMonths(1).atDay(1).atStartOfDay();
        BigDecimal utilizado = obterTotalUtilizado(clienteId, inicio, fim);
        if (utilizado.add(calculo.valorFinalBRL()).compareTo(LIMITE_MENSAL) > 0) {
            throw new CompraLimiteExcedidoException();
        }

        Compra compra = new Compra(
                calculo.valorUSD(),
                calculo.cotacaoUtilizada(),
                calculo.valorConvertido(),
                calculo.taxaServico(),
                calculo.tributoSimulado(),
                calculo.valorFinalBRL(),
                calculo.dataCotacao(),
                calculo.dataCompra(),
                cliente
        );
        return toResponse(compraRepository.save(compra));
    }

    @Transactional(readOnly = true)
    public List<CompraResponse> historico(UUID clienteId) {
        exigirClienteExistente(clienteId);
        return compraRepository.findByClienteIdOrderByDataCompraDesc(clienteId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public LimiteMensalResponse consultarLimite(UUID clienteId) {
        exigirClienteExistente(clienteId);
        YearMonth mes = YearMonth.now();
        BigDecimal utilizado = obterTotalUtilizado(
                clienteId,
                mes.atDay(1).atStartOfDay(),
                mes.plusMonths(1).atDay(1).atStartOfDay()
        );
        return new LimiteMensalResponse(
                clienteId,
                mes,
                LIMITE_MENSAL,
                utilizado,
                LIMITE_MENSAL.subtract(utilizado)
        );
    }

    @Transactional(readOnly = true)
    public void validarCliente(UUID clienteId) {
        exigirClienteExistente(clienteId);
    }

    private void exigirClienteExistente(UUID clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException(clienteId);
        }
    }

    private BigDecimal obterTotalUtilizado(UUID clienteId, LocalDateTime inicio, LocalDateTime fim) {
        BigDecimal total = compraRepository.somarValorFinalNoPeriodo(clienteId, inicio, fim);
        return total == null ? BigDecimal.ZERO : total;
    }

    private CompraResponse toResponse(Compra compra) {
        return new CompraResponse(
                compra.getId(),
                compra.getCliente().getId(),
                compra.getValorUSD(),
                compra.getCotacaoUtilizada(),
                compra.getValorConvertido(),
                compra.getTaxaServico(),
                compra.getTributoSimulado(),
                compra.getValorFinalBRL(),
                compra.getDataCotacao(),
                compra.getDataCompra()
        );
    }
}
