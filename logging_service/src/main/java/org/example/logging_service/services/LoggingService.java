package org.example.logging_service.services;

import org.example.logging_service.data.DataMapSingleton;
import org.example.logging_service.data.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoggingService{


    private DataMapSingleton dataMapSingleton = DataMapSingleton.getInstance();


    public boolean isNotDuplicated(String id){
        if(dataMapSingleton.getData().containsKey(id)){
            return false;
        }
        return true;

    }
    public boolean saveMessage(Message message) {
        Map<String, String> map = dataMapSingleton.getData();
        if(map.containsKey(message.id())){
            return false;
        }
        map.put(message.id(), message.message());
        System.out.println(" '" + message.message() + "' - saved to map");
        return true;
    }

    public Message getMessage(String id) {
        Map<String, String> map = dataMapSingleton.getData();
        if(map.containsKey(id)){
            return new Message(id, map.get(id));
        }
        return null;
    }


    public List<String> getAllMessages() {
        Map<String, String> map = dataMapSingleton.getData();

        return map.values().stream().toList();
    }

}
