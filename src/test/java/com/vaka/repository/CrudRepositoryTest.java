package com.vaka.repository;


import com.vaka.domain.BaseEntity;
import com.vaka.util.ApplicationContext;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Iaroslav
 * @since 20.12.2014 15:39
 */
public abstract class CrudRepositoryTest<Entity extends BaseEntity> {

    @BeforeClass
    private void init(){
        ApplicationContext.init();
    }
    @Test
    public void testCreate() {
        Entity e = createEntity();
        Assert.assertNull(e.getId());
        Assert.assertNull(e.getCreatedDate());

        Entity created = getRepository().create(e);

        Assert.assertNotNull(created);
        Assert.assertNotNull(created.getId());
        Assert.assertNotNull(created.getCreatedDate());
    }

    @Test
    public void testRead() {
        Entity created = getRepository().create(createEntity());
        Entity read = getRepository().getById(created.getId());

        Assert.assertNotNull(read);
        Assert.assertEquals(read, created);

    }

    @Test
    public void testUpdate() {
        Entity oldEntity = getRepository().create(createEntity());
        Entity newEntity = createEntity();
        Entity updated = getRepository().update(oldEntity.getId(), newEntity);

        Assert.assertNotNull(updated);
        Assert.assertEquals(newEntity, updated);
    }

    @Test
    public void testDelete() {
        Entity e = getRepository().create(createEntity());

        boolean deleted = getRepository().delete(e.getId());
        Assert.assertTrue(deleted);

        Entity entity = getRepository().getById(e.getId());
        Assert.assertNull(entity);
    }

    protected abstract CrudRepository<Entity> getRepository();

    protected abstract Entity createEntity();
}
