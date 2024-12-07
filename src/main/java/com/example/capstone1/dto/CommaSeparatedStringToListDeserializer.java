package com.example.capstone1.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CommaSeparatedStringToListDeserializer extends JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String text = parser.getText();
        return Arrays.asList(text.split("\\s*,\\s*")); // 쉼표로 분리하고 공백 제거
    }
}
