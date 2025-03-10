package org.example.logging_service.data;


import java.util.HashMap;
import java.util.Map;

public class DataMapSingleton {
    private static volatile DataMapSingleton instance;
    private final Map<String, String> data = new HashMap<>();

    private DataMapSingleton() {
    }

    public static DataMapSingleton getInstance() {
        if (instance == null) {
            synchronized (DataMapSingleton.class) {
                if (instance == null) {
                    instance = new DataMapSingleton();
                }
            }
        }
        return instance;
    }

    public Map<String, String> getData() {
        return data;
    }
}
