package com.excilys.cdb.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Victor Gambier
 *
 *         Using this class, we let Spring BasicErrorController handle the com.excilys.cdb.exception by overriding
 *         the status code. This applies to all REST Controllers.
 */

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ComputerNotFoundException.class)
    public ResponseEntity<Object> handleNoComputerFoundException(Exception e) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Invalid computer ID (" + e.getMessage() + ")");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<Object> handleNoCompanyFoundException() {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Invalid company ID");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PageNotFoundException.class)
    public ResponseEntity<Object> handleNoPageFoundException() {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Invalid page ID");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidNewEntryException.class)
    public ResponseEntity<Object> handleInvalidNewEntryException(Exception e) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
