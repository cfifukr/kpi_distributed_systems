package org.example.logging_service.services;

import com.hazelcast.map.IMap;
import org.example.logging_service.data.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LoggingService{

    private final Map<String, String> messagesMap;

    public LoggingService(IMap<String,String> map){
        this.messagesMap = map;
    }


    public boolean isNotDuplicated(String id){
        if(messagesMap.containsKey(id)){
            return false;
        }
        return true;

    }
    public boolean saveMessage(Message message) {
        if(messagesMap.containsKey(message.id())){
            return false;
        }
        messagesMap.put(message.id(), message.message());
        System.out.println(" '" + message.message() + "' - saved to map");
        return true;
    }

    public Message getMessage(String id) {
        if(messagesMap.containsKey(id)){
            return new Message(id, messagesMap.get(id));
        }
        return null;
    }


    public List<String> getAllMessages() {

        return messagesMap.values().stream().toList();
    }

}
