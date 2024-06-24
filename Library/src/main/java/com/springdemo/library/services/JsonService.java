package com.springdemo.library.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class JsonService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Method to deserialize JSON to an object
    public  <T> T deserialize(String jsonString, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, clazz);
    }

    public <T> T deserialize(String jsonString, TypeReference<T> typeReference) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, typeReference);
    }

    // Serialize an object to JSON
    public String serialize(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
