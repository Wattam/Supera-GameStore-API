package br.com.superaGameStore.controller.exception;

public class MethodNotAllowedException extends RuntimeException {

    public MethodNotAllowedException(String exception) {
        super(exception);
    }
}
