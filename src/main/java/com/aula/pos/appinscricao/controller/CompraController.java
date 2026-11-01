package com.aula.pos.appinscricao.controller;

import com.aula.pos.appinscricao.dto.*;
import com.aula.pos.appinscricao.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class CompraController {
    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping("/compras/simular")
    public CompraCalculoResponse simular(@Valid @RequestBody SimulacaoCompraRequest request) {
        return compraService.simular(request.valorUSD());
    }

    @PostMapping("/compras")
    @ResponseStatus(HttpStatus.CREATED)
    public CompraResponse registrar(@Valid @RequestBody RegistrarCompraRequest request) {
        return compraService.registrar(request.clienteId(), request.valorUSD());
    }

    @GetMapping("/clientes/{id}/compras")
    public List<CompraResponse> historico(@PathVariable UUID id) {
        return compraService.historico(id);
    }

    @GetMapping("/clientes/{id}/limite")
    public LimiteMensalResponse limite(@PathVariable UUID id) {
        return compraService.consultarLimite(id);
    }
}
