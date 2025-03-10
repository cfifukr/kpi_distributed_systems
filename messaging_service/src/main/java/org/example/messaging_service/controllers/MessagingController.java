package org.example.messaging_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messaging")
public class MessagingController {

    //поки виступає у ролі заглушки, при звернені до нього повертає статичне повідомлення
    private final String MESSAGE = "not implemented yet";

    @RequestMapping("/**")
    public ResponseEntity<String> stub() {
        return ResponseEntity.ok(MESSAGE);
    }
}
