package br.com.superaGameStore.controller.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {

        String[] details = ex.getLocalizedMessage().split("\n");
        ErrorResponse error = new ErrorResponse("server Error", details);

        return new ResponseEntity<Object>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<Object> handleNotFoundException(RecordNotFoundException ex) {

        String[] details = ex.getLocalizedMessage().split("\n");
        ErrorResponse error = new ErrorResponse("record Not Found", details);

        return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public final ResponseEntity<Object> handleMethodNotAllowedException(MethodNotAllowedException ex) {

        String[] details = ex.getLocalizedMessage().split("\n");
        ErrorResponse error = new ErrorResponse("method now allowed", details);

        return new ResponseEntity<Object>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        String[] details = ex.getLocalizedMessage().split(";");
        ErrorResponse error = new ErrorResponse("data integrity violation", details);

        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        String[] details = ex.getLocalizedMessage().split("\n");
        ErrorResponse error = new ErrorResponse("invalid format", details);

        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }
}