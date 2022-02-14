package br.com.superaGameStore.controller.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private String message;
    private String[] details;

    public ErrorResponse(String message, String[] details) {
        super();
        this.message = message;
        this.details = details;
    }
}