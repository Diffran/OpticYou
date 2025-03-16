package cat.ioc.opticyou.exception;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * Gestor global d'excepcions per capturar i manejar excepcions.
 * Retorna una resposta amb un missatge d'error personalitzat.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MissatgeError> EntityNotFoundException(EntityNotFoundException ex, WebRequest request){
        MissatgeError missatgeError = new MissatgeError(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<MissatgeError>(missatgeError, HttpStatus.NOT_FOUND);
    }
}
