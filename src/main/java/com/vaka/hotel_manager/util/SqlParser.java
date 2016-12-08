package com.vaka.hotel_manager.util;

import com.vaka.hotel_manager.util.exception.CreatingException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iaroslav on 12/5/2016.
 */
public class SqlParser {
    private String[] paths;
    private Map<String, String> queryByClassAndMethodName = new HashMap<>();
    private String classAndMethodName;
    String query = "";
    private boolean readyToNextQuery = true;
    int count = 0;

    public SqlParser(String... paths) {
        this.paths = paths;
    }

    public void parseLine(String line) {
        count++;
        if (readyToNextQuery) {
            if (line.startsWith("#")) {
                classAndMethodName = line.substring(2);
                readyToNextQuery = false;
            } else
                throw new CreatingException("Incoming lines are not in appropriate format, check line: " + count);
        } else if (line.endsWith(";")) {
            query = String.join(" ", query, line.substring(0, line.length() - 1));
            queryByClassAndMethodName.put(classAndMethodName, query);
            readyToNextQuery = true;
            query = "";
        } else {
            query = String.join(" ", query, line);
        }
    }

    public Map<String, String> createAndGetQueryByClassAndMethodName() {
        Arrays.stream(paths).forEach(fileName -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                reader.lines().forEach(this::parseLine);
            } catch (FileNotFoundException ex) {
                throw new CreatingException(ex);
            }
        });
        if (readyToNextQuery)
            return Collections.unmodifiableMap(queryByClassAndMethodName);
        else throw new CreatingException("File isn't in appropriate format");
    }
}
