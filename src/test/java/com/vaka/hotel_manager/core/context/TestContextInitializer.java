package com.vaka.hotel_manager.core.context;

import com.vaka.hotel_manager.core.context.config.BeanConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by Iaroslav on 12/8/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestContextInitializer {

    private static void init(BeanConfig... configs){

        new ApplicationContextHolder().setContext(ApplicationContext
                    .init(configs));
        //TODO add persistence properties from test resources
    }

    public static void initForRepositories() {
        init(new TestRepositoryContextConfig(), new TestPersistenceConfig());
    }

    public static void initForServices() {
        init(new TestServiceContextConfig());
    }
}
