package org.example.facade_service.controllers;

import org.example.facade_service.data.Message;
import org.example.facade_service.services.FacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@RestController
@RequestMapping("/api/facade")
public class FacadeController {
    private final FacadeService facadeService;
    public FacadeController(FacadeService facadeService) {
        this.facadeService = facadeService;
    }

    @PostMapping
    public ResponseEntity<?> postRequest(@RequestParam(value = "msg") String msg) {
        try {
            Message newMessage = facadeService.handleNewMessage(msg);
            return ResponseEntity.ok(newMessage);
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error " + e.getMessage());
        }
    }



    @GetMapping
    public ResponseEntity<?> getRequest() {
        StringBuilder builder = new StringBuilder();

        builder.append(facadeService.getAllMessages())
                .append("\n");
        builder.append(facadeService.requestToMessagingService())
                .append("\n");


        return ResponseEntity.ok(builder.toString());
    }

}
