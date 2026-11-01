package com.aula.pos.appinscricao.exception;

import org.springframework.http.HttpStatus;

public class CotacaoIndisponivelException extends ApiException {
    public CotacaoIndisponivelException(HttpStatus status) {
        super(status, "Não foi possível consultar a cotação USD/BRL");
    }
}
