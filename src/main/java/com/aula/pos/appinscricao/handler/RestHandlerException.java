package com.aula.pos.appinscricao.handler;

import com.aula.pos.appinscricao.dto.ErrorResponse;
import com.aula.pos.appinscricao.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class RestHandlerException {
    private static final Logger logger = LoggerFactory.getLogger(RestHandlerException.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus()).body(build(ex.getStatus(), ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors()
                .stream().map(e -> new ErrorResponse.FieldErrorResponse(e.getField(), e.getDefaultMessage())).toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(build(HttpStatus.BAD_REQUEST, "Dados de entrada inválidos", errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBody(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(build(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido", null));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var errors = List.of(new ErrorResponse.FieldErrorResponse(
                ex.getName(),
                "O valor informado possui formato inválido"
        ));
        return ResponseEntity.badRequest()
                .body(build(HttpStatus.BAD_REQUEST, "Parâmetro de requisição inválido", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        logger.error("Erro não tratado durante o processamento da requisição", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(build(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno", null));
    }

    private ErrorResponse build(
            HttpStatus status,
            String description,
            List<ErrorResponse.FieldErrorResponse> errors
    ) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                "HTTP_" + status.value(),
                description,
                errors
        );
    }
}
