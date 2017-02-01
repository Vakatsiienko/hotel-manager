package com.vaka.hotel_manager.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Thread unsafe json parser
 */
public class JSONParser {
    private Map<String, String> jsonMap = new HashMap<>();
    private boolean readingKey;
    private boolean readingValue;
    private StringBuilder keyBuilder = new StringBuilder();
    private StringBuilder valueBuilder = new StringBuilder();
    private String key;
    private String value;

    public Map<String, String> parse(InputStream inputStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            while (reader.ready()) {
                int ascii = reader.read();
                if (key == null)
                    readKey(ascii);
                else if (value == null)
                    readValue(ascii);
                else {
                    throw new IllegalStateException("JSON isn't in supported format");
                }
            }
            return jsonMap;
        } finally {
            reset();
        }
    }

    private void reset() {
        readingKey = false;
        readingValue = false;
        jsonMap = new HashMap<>();
        keyBuilder.setLength(0);
        valueBuilder.setLength(0);
        key = null;
        value = null;
    }



    private void readKey(int ascii) {
        char ch = (char) ascii;
        if (readingKey) {
            if (ch != '"') {
                keyBuilder.append(ch);
            } else {
                key = keyBuilder.toString();
                keyBuilder.setLength(0);
                readingKey = false;
            }
        } else if (ch == '"')
            readingKey = true;
    }

    private void readValue(int ascii) {
        char ch = (char) ascii;
        if (readingValue) {
            if (ch != '"' && ch != ',' && ch != '}') {
                valueBuilder.append(ch);
            } else {
                value = valueBuilder.toString();
                valueBuilder.setLength(0);
                readingValue = false;
                jsonMap.put(key, value);
                key = null;
                value = null;
            }
        } else if (ch != ':' && ch != '"' && ch != '[') {
            readingValue = true;
            readValue((int) ch);
        } else if (ch == '[')
            throw new UnsupportedOperationException("Parser can't parse array");
    }
}
