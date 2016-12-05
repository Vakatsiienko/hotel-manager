package com.vaka.repository;


import com.vaka.DBTestUtil;
import com.vaka.context.config.ApplicationContextConfig;
import com.vaka.context.config.PersistenceConfig;
import com.vaka.domain.BaseEntity;
import com.vaka.context.ApplicationContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author Iaroslav
 * @since 20.12.2014 15:39
 */
public abstract class CrudRepositoryTest<Entity extends BaseEntity> {

    @BeforeClass
    public static void init() throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        ApplicationContext.getInstance().init(new ApplicationContextConfig(), new PersistenceConfig());
//        DBTestUtil.reset();
//        Thread.sleep(10000);
    }
    @Before
    public void beforeTest() throws SQLException, ClassNotFoundException {
    }
    @Test
    public void testCreate() {
        Entity e = createEntity();
        Assert.assertNull(e.getId());
        Entity created = getRepository().create(e);

        Entity createdNext = getRepository().create(createEntity());
        Optional<Entity> byId = getRepository().getById(createdNext.getId());

        Assert.assertFalse(created.getId().equals(createdNext.getId()));
        Assert.assertNotNull(created.getId());
        Assert.assertEquals(e, created);
        Assert.assertEquals(byId.get(), createdNext);
    }

    @Test
    public void testGetById() {
        Entity created = getRepository().create(createEntity());
        Optional<Entity> read = getRepository().getById(created.getId());

        Assert.assertNotNull(read);
        Assert.assertEquals(read.get(), created);

    }

    @Test
    public void testUpdate() {
        Entity oldEntity = getRepository().create(createEntity());
        Entity newEntity = createEntity();
        boolean updated = getRepository().update(oldEntity.getId(), newEntity);
        Optional<Entity> updatedEntity = getRepository().getById(newEntity.getId());

        Assert.assertTrue(updated);
        Assert.assertEquals(newEntity, updatedEntity.get());
    }

    @Test
    public void testDelete() {
        Entity e = getRepository().create(createEntity());

        boolean deleted = getRepository().delete(e.getId());
        Assert.assertTrue(deleted);

        Optional<Entity> entity = getRepository().getById(e.getId());
        Assert.assertFalse(entity.isPresent());
    }

    protected abstract CrudRepository<Entity> getRepository();

    protected abstract Entity createEntity();
}
