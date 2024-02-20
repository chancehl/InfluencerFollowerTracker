package com.chancehl.InfluencerFollowerTracker.handlers;

import com.chancehl.InfluencerFollowerTracker.models.MissingEntityException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MissingEntityExceptionHandler {
    @ExceptionHandler(MissingEntityException.class)
    public ResponseEntity<String> handleMissingEntityException(MissingEntityException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
}
