package com.nttdata.exception;

import com.nttdata.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

// INTERCEPTADOR DE LAS EXCEPCIONES
@RestControllerAdvice
@Slf4j
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    // SI NO SE CONTROLA ALGUNA EXCEPCION SE PUEDE CREAR LA EXCEPCION PADRE DE TODAS
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleAllExceptions(Exception ex, WebRequest request) {
        // request.getDescription(false) => url abreviada del error
        BaseResponse er = BaseResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage()).build();
        return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Cuando hay una excepcion de tipo ModelNotFoundException la intercepta
    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<BaseResponse> handleModelNotFoundException(ModelNotFoundException ex, WebRequest request) {
        // request.getDescription(false) => url abreviada del err
        BaseResponse er = BaseResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage()).build();
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = ex.getBindingResult().getAllErrors().stream().map(e -> e.getDefaultMessage().concat(". ")).collect(Collectors.joining());
        log.error(message);
        BaseResponse er = BaseResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message).build();
        return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
    }
}
