package com.vaka.hotel_manager.repository;


import com.vaka.hotel_manager.core.context.TestContextInitializer;
import com.vaka.hotel_manager.domain.entity.BaseEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author Iaroslav
 * @since 20.12.2014 15:39
 */
public abstract class CrudRepositoryTest<Entity extends BaseEntity> {

    @BeforeClass
    public static void init() throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        TestContextInitializer.initForRepositories();
    }
    @Before
    public void beforeTest() throws SQLException, ClassNotFoundException {
    }
    @Test
    public void testCreate() {
        Entity e = createEntity();
        Assert.assertNull(e.getId());

        Entity created = getRepository().create(e);
        Assert.assertNotNull(created.getId());
        e.setId(created.getId());
        Assert.assertEquals(e, created);
    }

    @Test
    public void testGetById() {
        Entity created = getRepository().create(createEntity());
        Optional<Entity> read = getRepository().getById(created.getId());

        Assert.assertTrue(read.isPresent());
        Assert.assertEquals(created, read.get());
    }

    @Test
    public void testUpdate() {
        Entity oldEntity = getRepository().create(createEntity());
        Entity newEntity = getEntityForUpdate(oldEntity);
        boolean updated = getRepository().update(oldEntity.getId(), newEntity);
        Optional<Entity> updatedEntity = getRepository().getById(newEntity.getId());
        newEntity.setCreatedDatetime(updatedEntity.get().getCreatedDatetime());

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

    protected Entity getEntityForUpdate(Entity entity){
        return createEntity();
    }
    protected abstract CrudRepository<Entity> getRepository();

    protected abstract Entity createEntity();
}
