package com.vaka;

import com.ibatis.common.jdbc.ScriptRunner;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Iaroslav on 12/4/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DBTestUtil {
    public static void reset() throws SQLException, ClassNotFoundException {
        String aSQLScriptFilePath = "C:\\Users\\Iaroslav\\IdeaProjects\\hotel-manager\\src\\main\\resources\\init.sql";

        // Create MySql Connection
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/hotel_manager", "root", "root");
        Statement stmt = null;

        try {
            // Initialize object for ScripRunner
            ScriptRunner sr = new ScriptRunner(con, false, false);

            // Give the input file to Reader
            Reader reader = new BufferedReader(
                    new FileReader(aSQLScriptFilePath));

            // Exctute script
            sr.runScript(reader);

        } catch (Exception e) {
            System.err.println("Failed to Execute" + aSQLScriptFilePath
                    + " The error is " + e.getMessage());
        }
    }
}
