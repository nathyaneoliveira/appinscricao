package com.aula.pos.appinscricao.service;

import com.aula.pos.appinscricao.client.AwesomeApiClient;
import com.aula.pos.appinscricao.dto.AwesomeApiResponse;
import com.aula.pos.appinscricao.exception.CotacaoIndisponivelException;
import com.aula.pos.appinscricao.exception.CotacaoInvalidaException;
import feign.FeignException;
import feign.RetryableException;
import feign.codec.DecodeException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.concurrent.TimeoutException;

@Service
public class CotacaoService {
    private static final DateTimeFormatter DATA_HORA_AWESOME_API =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").withResolverStyle(ResolverStyle.STRICT);

    private final AwesomeApiClient awesomeApiClient;

    public CotacaoService(AwesomeApiClient awesomeApiClient) {
        this.awesomeApiClient = awesomeApiClient;
    }

    public CotacaoAtual consultarCotacaoAtual() {
        AwesomeApiResponse response;
        try {
            response = awesomeApiClient.buscarCotacaoUsdBrl();
        } catch (RetryableException ex) {
            throw new CotacaoIndisponivelException(isTimeout(ex)
                    ? HttpStatus.GATEWAY_TIMEOUT
                    : HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DecodeException ex) {
            throw new CotacaoInvalidaException();
        } catch (FeignException ex) {
            throw new CotacaoIndisponivelException(HttpStatus.SERVICE_UNAVAILABLE);
        }

        if (response == null || response.USDBRL() == null) {
            throw new CotacaoInvalidaException();
        }

        String ask = response.USDBRL().ask();
        String createDate = response.USDBRL().create_date();
        if (ask == null || ask.isBlank() || createDate == null || createDate.isBlank()) {
            throw new CotacaoInvalidaException();
        }

        try {
            BigDecimal valor = new BigDecimal(ask);
            LocalDateTime dataHora = LocalDateTime.parse(createDate, DATA_HORA_AWESOME_API);
            if (valor.signum() <= 0) {
                throw new CotacaoInvalidaException();
            }
            return new CotacaoAtual(valor, dataHora);
        } catch (NumberFormatException | DateTimeParseException ex) {
            throw new CotacaoInvalidaException();
        }
    }

    private boolean isTimeout(Throwable error) {
        Throwable current = error;
        while (current != null) {
            if (current instanceof SocketTimeoutException || current instanceof TimeoutException) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    public record CotacaoAtual(BigDecimal valor, LocalDateTime dataHora) {
    }
}
