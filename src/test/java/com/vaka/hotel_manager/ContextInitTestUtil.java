package com.vaka.hotel_manager;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.context.config.ApplicationContextConfig;
import com.vaka.hotel_manager.context.config.PersistenceConfig;
import com.vaka.hotel_manager.util.exception.ApplicationContextInitException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by Iaroslav on 12/8/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextInitTestUtil {

    @Getter
    private static boolean initIsDone;

    public static void init() throws IOException {
        try {
            String connectionProp = ContextInitTestUtil.class.getClassLoader().getResource("testPersistence.properties").getPath();
            URL repository = ContextInitTestUtil.class.getClassLoader().getResource("repository");
            File[] files = new File(repository.getFile()).listFiles();
            String[] filesPath = Arrays.stream(files)
                    .map(File::getPath)
                    .toArray(String[]::new);
            ApplicationContext.getInstance()
                    .init(new ApplicationContextConfig(), new PersistenceConfig(connectionProp, filesPath));
        } catch (NullPointerException | IOException ex) {
            throw new ApplicationContextInitException("Exception during context initialization, check sql files, or persistence props", ex);
        }
        initIsDone = true;
    }


}
