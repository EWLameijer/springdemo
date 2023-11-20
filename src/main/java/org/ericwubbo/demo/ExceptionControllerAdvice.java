package org.ericwubbo.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(InconsistentIdException.class)
    public ResponseEntity<ProblemDetail> inconsistentIdExceptionHandler() {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "ids given by path and body are inconsistent, and the id of an item should not be changed");
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
