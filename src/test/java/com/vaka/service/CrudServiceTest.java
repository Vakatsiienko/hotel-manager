package com.vaka.service;

import com.vaka.domain.BaseEntity;
import com.vaka.repository.CrudRepository;

/**
 * @author Iaroslav
 * @since 22.12.2014 20:50
 */
public abstract class CrudServiceTest<Entity extends BaseEntity> {
//    @Test
//    public void testCreate() {
//        final Entity e = createEntity();
//        getService().create(e);
//
//        new Verifications() {{
//            getMockedRepository().create(e);
//        }};
//    }
//
//    @Test
//    public void testGetById() {
//        final Entity created = createEntity();
//        final BaseDao<Entity> dao = getMockedRepository();
//
//        new Expectations() {{
//            dao.read(1);
//            returns(created);
//        }};
//
//        Entity read = getService().read(1);
//
//        Assert.assertNotNull(read);
//        Assert.assertEquals(read, created);
//    }
//
//    @Test
//    public void testUpdate() {
//        final Entity oldEntity = createEntity();
//        final Entity newEntity = createEntity();
//        Entity make = newEntity;
//        make.setCreatedDatetime(oldEntity.getCreatedDatetime());
//        final Entity entity = make;
//        final BaseDao<Entity> dao = getMockedRepository();
//        new Expectations() {{
//            dao.update(oldEntity.getId(), newEntity);
//            returns(entity);
//        }};
//        Entity e = getService().update(oldEntity.getId(), newEntity);
//        Assert.assertEquals(e, entity);
//    }
//
//    @Test
//    public void testDelete() {
//        final BaseDao<Entity> dao = getMockedRepository();
//        final Entity e = createEntity();
//        final boolean deleted = true;
//        new Expectations(){{
//            dao.delete(e.getId());
//            returns(deleted);
//        }};
//        boolean b = getService().delete(e.getId());
//        Assert.assertEquals(b, deleted);
//    }

    protected abstract CrudService<Entity> getService();

    protected abstract CrudRepository<Entity> getMockedRepository();

    protected abstract Entity createEntity();
}
