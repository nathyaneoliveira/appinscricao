package com.aula.pos.appinscricao.exception;

import org.springframework.http.HttpStatus;

public class CompraLimiteExcedidoException extends ApiException {
    public CompraLimiteExcedidoException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "A compra excede o limite mensal de R$ 10.000,00");
    }
}
