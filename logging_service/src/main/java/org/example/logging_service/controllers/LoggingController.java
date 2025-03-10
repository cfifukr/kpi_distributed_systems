package org.example.logging_service.controllers;

import org.example.logging_service.data.Message;
import org.example.logging_service.services.LoggingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logging")
public class LoggingController {
    private final LoggingService loggingService;

    public LoggingController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }


    @GetMapping
    public ResponseEntity<?> getAllMessages() {
        return ResponseEntity.ok(loggingService.getAllMessages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMessage(@PathVariable String id) {
        Message message = loggingService.getMessage(id);

        if(message == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(message);
    }

    @PostMapping
    public ResponseEntity<?> saveMessage(@RequestBody Message message,
                                         @RequestHeader("Idempotency-Key") String key) {
        boolean res = loggingService.saveMessage(message);

         if (loggingService.isNotDuplicated(key)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate message ignored");
         }

         if (res) {
             return ResponseEntity.ok().build();
         }
         return ResponseEntity.badRequest().build();
    }

}
