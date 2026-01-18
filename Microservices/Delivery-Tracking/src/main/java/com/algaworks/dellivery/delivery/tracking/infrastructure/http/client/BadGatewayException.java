package com.algaworks.dellivery.delivery.tracking.infrastructure.http.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class BadGatewayException extends RuntimeException{
    public BadGatewayException(Throwable cause) {
        super(cause);
    }

    public BadGatewayException() {
    }
}
