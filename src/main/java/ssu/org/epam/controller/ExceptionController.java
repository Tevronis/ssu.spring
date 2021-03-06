package ssu.org.epam.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ssu.org.epam.model.ExceptionEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ssu.org.epam.exception.OfficeException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

        @ExceptionHandler(OfficeException.class)
        public ResponseEntity<ExceptionEntity> validationException(OfficeException exception) {
            ExceptionEntity exc = new ExceptionEntity(exception.getMessage(), System.currentTimeMillis());
            return new ResponseEntity(exc, HttpStatus.BAD_GATEWAY);
        }

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                      HttpHeaders headers,
                                                                      HttpStatus status,
                                                                      WebRequest request) {
            List<String> errors = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            String msg = String.join("\n", errors);
            return new ResponseEntity<>(new ExceptionEntity(msg, System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
        }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(new ExceptionEntity(e.getMessage(), System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }
}
