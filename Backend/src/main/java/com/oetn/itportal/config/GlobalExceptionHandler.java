package com.oetn.itportal.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fields = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(e ->
            fields.put(((FieldError) e).getField(), e.getDefaultMessage()));
        Map<String, Object> res = new HashMap<>();
        res.put("timestamp", LocalDateTime.now()); res.put("status", 400);
        res.put("error", "Validation échouée"); res.put("fields", fields);
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        Map<String, Object> res = new HashMap<>();
        res.put("timestamp", LocalDateTime.now()); res.put("status", 403);
        res.put("error", "Accès refusé"); res.put("message", "Droits insuffisants.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        Map<String, Object> res = new HashMap<>();
        res.put("timestamp", LocalDateTime.now()); res.put("status", 400);
        res.put("error", "Erreur"); res.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        Map<String, Object> res = new HashMap<>();
        res.put("timestamp", LocalDateTime.now()); res.put("status", 500);
        res.put("error", "Erreur interne"); res.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}
