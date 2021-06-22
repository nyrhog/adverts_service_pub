package com.project.exceptionHandler;

import com.project.dto.ExceptionDto;
import com.project.exception.RateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerClass {
    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<ExceptionDto> entityNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ExceptionDto()
                .setException(ex.getClass().getSimpleName())
                .setMessage(ex.getLocalizedMessage())
                , HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(RateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ExceptionDto> rateExceptionHandler(RateException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ExceptionDto()
                .setException(ex.getClass().getSimpleName())
                .setMessage(ex.getLocalizedMessage())
                , HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ExceptionDto> validationException(MethodArgumentNotValidException ex) {

        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }

        log.error("Validation exception: " + details);
        return new ResponseEntity<>(new ExceptionDto()
                .setException("Validation exception")
                .setMessage(details.toString())
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    private ResponseEntity<ExceptionDto> handleAuthenticationException(AuthenticationException ex) {
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(new ExceptionDto()
                .setException(ex.getClass().getSimpleName())
                .setMessage(ex.getLocalizedMessage())
                , HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ExceptionDto> handleUnexpectedException(Exception ex) {
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(new ExceptionDto()
                .setException(ex.getClass().getSimpleName())
                .setMessage(ex.getLocalizedMessage())
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
