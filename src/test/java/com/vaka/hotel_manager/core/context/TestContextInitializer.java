package com.vaka.hotel_manager.core.context;

import com.vaka.hotel_manager.core.context.config.ApplicationContextConfig;
import com.vaka.hotel_manager.core.context.config.PersistenceConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by Iaroslav on 12/8/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestContextInitializer {

    private static void init(ApplicationContextConfig config){

        new ApplicationContextHolder().setContext(ApplicationContext
                    .init(config, new PersistenceConfig()));
        //TODO add persistence properties from test resources
    }

    public static void initForRepositories() {
        init(new RepositoryTestContextConfig());
    }

    public static void initForServices() {
        init(new ServiceTestContextConfig());
    }
}
