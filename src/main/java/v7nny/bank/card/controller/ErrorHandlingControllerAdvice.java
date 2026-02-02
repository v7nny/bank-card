package v7nny.bank.card.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import v7nny.bank.card.dto.ViolationDTO;
import java.util.List;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ViolationDTO>> onConstraintValidationException(ConstraintViolationException e) {
        List<ViolationDTO> errorMessages = e.getConstraintViolations().stream()
                .map(violation -> new ViolationDTO(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                )).toList();

        return ResponseEntity.status(400).body(errorMessages);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ViolationDTO>> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ViolationDTO> errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ViolationDTO(error.getField(), error.getDefaultMessage())).toList();

        return ResponseEntity.status(400).body(errorMessages);
    }
}