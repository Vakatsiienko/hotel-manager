package com.vaka.hotel_manager;

import com.ibatis.common.jdbc.ScriptRunner;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by Iaroslav on 12/4/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DBTestUtil {
    public static void reset() throws SQLException, ClassNotFoundException, IOException {
        String aSQLScriptFilePath = DBTestUtil.class.getClassLoader().getResource("init.sql").getPath();
        Properties props = new Properties();
        props.load(DBTestUtil.class.getClassLoader().getResourceAsStream("testPersistence.properties"));

        Class.forName(props.getProperty("dataSource.driver"));
        // Create MySql Connection
        try (Connection con = DriverManager.getConnection(
                props.getProperty("dataSource.url"), props.getProperty("dataSource.username"), props.getProperty("dataSource.password"));
             Statement stmt = null;
             // Give the input file to Reader
             Reader reader = new BufferedReader(
                     new FileReader(aSQLScriptFilePath))) {
            ScriptRunner sr = new ScriptRunner(con, false, false);

            // Execute script
            sr.runScript(reader);

        } catch (Exception e) {
            System.err.println("Failed to Execute" + aSQLScriptFilePath
                    + " The error is " + e.getMessage());
        }
    }
}
