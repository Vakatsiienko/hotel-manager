package com.vaka.hotel_manager.context;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.core.context.config.ApplicationContextConfig;
import com.vaka.hotel_manager.core.context.config.PersistenceConfig;
import com.vaka.hotel_manager.util.exception.ApplicationContextInitException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by Iaroslav on 12/8/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestContextInitializer {

    private static void init(ApplicationContextConfig config){
        try {
            String connectionProp = TestContextInitializer.class.getClassLoader().getResource("persistence.properties").getPath();
            URL repository = TestContextInitializer.class.getClassLoader().getResource("repository");
            File[] files = new File(repository.getFile()).listFiles();
            String[] filesPath = Arrays.stream(files)
                    .map(File::getPath)
                    .toArray(String[]::new);
            ApplicationContext.getInstance()
                    .init(config, new PersistenceConfig(connectionProp, filesPath));
        } catch (NullPointerException | IOException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new ApplicationContextInitException("Exception during context initialization, check sql files, or persistence props", ex);
        }
    }

    public static void initForRepositories() {
        init(new RepositoryTestContextConfig());
    }

    public static void initForServices() {
        init(new ServiceTestContextConfig());
    }
}
