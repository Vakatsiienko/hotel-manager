package com.vaka.hotel_manager.util;

import com.vaka.hotel_manager.util.exception.CreatingException;
import com.vaka.hotel_manager.util.exception.ParserException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Parser that read sql files and collect sql methods to map<class.methodName, query>
 *     files should be in format
 *     # class.methodName
 *     SELECT ...
 *     ...
 *     ...;
 *     # class.methodName
 *     ...;
 */
public class SqlParser {
    private Map<String, String> queryByClassAndMethodName = new HashMap<>();
    private String classAndMethodName;
    private StringJoiner query = new StringJoiner(" ");
    private boolean readyToNextQuery = true;
    private int count = 0;

    private void parseLine(String line) {
        count++;
        if (readyToNextQuery) {
            if (line.startsWith("#")) {
                classAndMethodName = line.substring(2);
                readyToNextQuery = false;
            } else
                throw new CreatingException("Incoming lines are not in appropriate format, check line: " + count);
        } else if (line.endsWith(";")) {
            query.add(line.substring(0, line.length() - 1));
            queryByClassAndMethodName.put(classAndMethodName, format(query.toString()));
            readyToNextQuery = true;
            query = new StringJoiner(" ");
        } else {
            query.add(line);
        }
    }

    private String format(String str) {
        String[] strArray = str.split(" ");
        StringJoiner joiner = new StringJoiner(" ");
        Arrays.stream(strArray).filter(s -> !"".equals(s)).forEach(joiner::add);
        return joiner.toString();
    }

    public void parseFiles(String... paths) {
        Arrays.stream(paths).forEach(fileName -> {
            try(BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName)))) {
                reader.lines().forEach(this::parseLine);
            } catch (IOException ex) {
                throw new ParserException(ex);
            }
        });
    }

    public Map<String, String> getQueryByClassAndMethodName() {
        if (readyToNextQuery)
            return new HashMap<>(queryByClassAndMethodName);
        else throw new ParserException("File isn't in appropriate format");
    }
}
