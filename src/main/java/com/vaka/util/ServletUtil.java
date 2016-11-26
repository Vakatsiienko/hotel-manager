package com.vaka.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaka.domain.ReservationRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public final class ServletUtil {
    private ServletUtil() {
    }

    /**
     * Read a JSON object from BufferedReader and parse him to given Class.
     * You must be sure that BufferedReader contains exactly this object Class and only one object.<br>
     * {@link ObjectMapper}, {@link ObjectMapper#readValue(String, Class)}
     * @throws IOException
     * @throws JsonParseException if underlying input contains invalid content of type JsonParser supports (JSON for default case)
     * @throws JsonMappingException if the input JSON structure does not match structure expected for result type (or has other mismatch issues)
     */
    public static <T> T readAndParseObject(BufferedReader reader, Class<T> objectClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObject = reader.lines()
                .collect(Collectors.joining(System.lineSeparator()));
        return objectMapper.readValue(jsonObject, objectClass);

    }
}
