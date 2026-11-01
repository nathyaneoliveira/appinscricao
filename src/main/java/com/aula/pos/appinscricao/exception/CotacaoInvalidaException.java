package com.aula.pos.appinscricao.exception;

import org.springframework.http.HttpStatus;

public class CotacaoInvalidaException extends ApiException {
    public CotacaoInvalidaException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "A resposta da cotação USD/BRL é inválida");
    }
}
