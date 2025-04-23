package org.example.kafka_proxy.logic;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/brokers")
public class Controller {

    @Autowired
    private EurekaRegister eurekaManager;

    @GetMapping
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(EurekaRegister.registeredBrokers);
    }
    @PostMapping("/register")
    public String registerBroker(@RequestParam String broker) {
        String[] parts = broker.split(":");
        if (parts.length != 2) return "Invalid broker format. Use host:port";

        String host = parts[0];
        int port = Integer.parseInt(parts[1]);

        System.out.println("Registering broker: " + broker);
        eurekaManager.registerBroker(host, port);
        return "Broker registered: " + broker;
    }

    @DeleteMapping("/unregister")
    public String unregisterBroker(@RequestParam String broker) {
        String[] parts = broker.split(":");
        if (parts.length != 2) return "Invalid broker format. Use host:port";

        String instanceId = broker;
        System.out.println("Unregistering broker: " + broker);
        eurekaManager.unregisterBroker(instanceId);
        return "Broker unregistered: " + broker;
    }
}
