package com.example.demo.exceptions;

import com.example.demo.dtos.errorDTOs.ErrorDTO;
import com.example.demo.dtos.errorDTOs.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionsHandler  {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ValidationError> validationErrors = new ArrayList<>();
        ErrorDTO errors = new ErrorDTO();

        errors.setCode(HttpStatus.BAD_REQUEST);
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.add(new ValidationError(fieldName, errorMessage));
        });
        errors.setErrors(validationErrors);
        errors.setMessage(ex.getClass().getSimpleName());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(HttpStatus.NOT_FOUND);
        errorDTO.setMessage(ex.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(ex.getClass().getSimpleName());
        errorDTO.setErrors(errors);

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorDTO> handleValidationExceptions(HttpRequestMethodNotSupportedException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        List<String> errors = new ArrayList<>();

        errorDTO.setCode(HttpStatus.METHOD_NOT_ALLOWED);
        errors.add(ex.getMessage());
        errorDTO.setErrors(errors);

        return new ResponseEntity<>(errorDTO, errorDTO.getCode());
    }

    @ExceptionHandler({NoStockException.class})
    public ResponseEntity<ErrorDTO> handleNoStockException(NoStockException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        List<String> errors = new ArrayList<>();

        errorDTO.setCode(HttpStatus.NOT_FOUND);
        errors.add(ex.getMessage());
        errorDTO.setErrors(errors);

        return new ResponseEntity<>(errorDTO, errorDTO.getCode());
    }

}
