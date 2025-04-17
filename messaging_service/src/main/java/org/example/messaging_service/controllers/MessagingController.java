package org.example.messaging_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.example.messaging_service.storage.MessageStorage.getMessages;

@RestController
@RequestMapping("/api/messaging")
public class MessagingController {


    @GetMapping("/")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(getMessages());
    }
}
