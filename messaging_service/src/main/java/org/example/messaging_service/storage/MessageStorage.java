package org.example.messaging_service.storage;

import java.util.ArrayList;

public class MessageStorage {
    private static ArrayList<String> messages;

    private MessageStorage() {}

    static {
        messages = new ArrayList<>();
    }

    public static ArrayList<String> getMessages() {
        return messages;
    }


    public static void addMessage(String message) {
        messages.add(message);
    }

}
