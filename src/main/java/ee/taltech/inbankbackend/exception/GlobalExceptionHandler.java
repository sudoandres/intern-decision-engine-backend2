package ee.taltech.inbankbackend.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ee.taltech.inbankbackend.dto.DecisionResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Generic fallback handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DecisionResponseDTO> handleGenericException(Exception ex) {
        DecisionResponseDTO response = new DecisionResponseDTO();
        response.setLoanAmount(null);
        response.setLoanPeriod(null);
        response.setErrorMessage("An unexpected error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Thrown when the applicant age is under 18 or over expected life expectancy.
    @ExceptionHandler(InvalidAgeException.class)
    public ResponseEntity<DecisionResponseDTO> handleInvalidAgeException(InvalidAgeException ex) {
        DecisionResponseDTO response = new DecisionResponseDTO();
        response.setLoanAmount(null);
        response.setLoanPeriod(null);
        response.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Thrown when requested loan amount is invalid.
    @ExceptionHandler(InvalidLoanAmountException.class)
    public ResponseEntity<DecisionResponseDTO> handleInvalidLoanAmountException(InvalidLoanAmountException ex) {
        DecisionResponseDTO response = new DecisionResponseDTO();
        response.setLoanAmount(null);
        response.setLoanPeriod(null);
        response.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Thrown when requested loan period is invalid.
    @ExceptionHandler(InvalidLoanPeriodException.class)
    public ResponseEntity<DecisionResponseDTO> handleInvalidLoanPeriodException(InvalidLoanPeriodException ex) {
        DecisionResponseDTO response = new DecisionResponseDTO();
        response.setLoanAmount(null);
        response.setLoanPeriod(null);
        response.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Thrown when provided personal ID code is invalid.
    @ExceptionHandler(InvalidPersonalCodeException.class)
    public ResponseEntity<DecisionResponseDTO> handleInvalidPersonalCodeException(InvalidPersonalCodeException ex) {
        DecisionResponseDTO response = new DecisionResponseDTO();
        response.setLoanAmount(null);
        response.setLoanPeriod(null);
        response.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Thrown when no valid loan amount can be given.
    @ExceptionHandler(NoValidLoanException.class)
    public ResponseEntity<DecisionResponseDTO> handleNoValidLoanException(NoValidLoanException ex) {
        DecisionResponseDTO response = new DecisionResponseDTO();
        response.setLoanAmount(null);
        response.setLoanPeriod(null);
        response.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DecisionResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .findFirst()
            .orElse("Invalid request");

        DecisionResponseDTO response = new DecisionResponseDTO();
        response.setLoanAmount(null);
        response.setLoanPeriod(null);
        response.setErrorMessage(errorMessage);

        return ResponseEntity.badRequest().body(response);
    }

}
