package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.context.TestContextInitializer;
import com.vaka.hotel_manager.domain.BaseEntity;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.CrudRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Iaroslav
 * @since 22.12.2014 20:50
 */
public abstract class CrudServiceTest<Entity extends BaseEntity> {

    @BeforeClass
    public static void init() {
        TestContextInitializer.initForServices();
    }

    @Test
    public void testCreate() {
        beforeGeneratingEntity();
        Entity e = createEntity();
        CrudRepository<Entity> repository = getMockedRepository();
        getService().create(getManager(), e);

        verify(repository).create(e);
    }

    @Test
    public void testGeneratingCreatedDatetimeOnCreate() {
        Entity e = createEntity();
        e.setCreatedDatetime(LocalDateTime.MIN);
        LocalDateTime oldDateTime = e.getCreatedDatetime();
        getService().create(getManager(), e);

        Assert.assertNotEquals(e.getCreatedDatetime(), oldDateTime);
    }

    @Test
    public void testGetById() {

        Entity created = createEntity();
        CrudRepository<Entity> repository = getMockedRepository();
        when(repository.getById(1)).thenReturn(Optional.of(created));

        Optional<Entity> read = getService().getById(getManager(), 1);
        Assert.assertNotNull(read);
        Assert.assertEquals(read.get(), created);
    }

    @Test
    public void testUpdateTrue() {
        Entity oldEntity = createEntity();
        Entity newEntity = createEntity();
        CrudRepository<Entity> repository = getMockedRepository();
        User loggedUser = getManager();
        when(repository.update(oldEntity.getId(), newEntity)).thenReturn(true);
        when(repository.getById(oldEntity.getId())).thenReturn(Optional.of(oldEntity));

        boolean b = getService().update(loggedUser, oldEntity.getId(), newEntity);
        Assert.assertTrue(b);
    }
    @Test
    public void testUpdateFalse() {
        Entity oldEntity = createEntity();
        Entity newEntity = createEntity();
        CrudRepository<Entity> repository = getMockedRepository();
        User loggedUser = getManager();
        when(repository.update(oldEntity.getId(), newEntity)).thenReturn(false);
        when(repository.getById(oldEntity.getId())).thenReturn(Optional.of(oldEntity));

        boolean b = getService().update(loggedUser, oldEntity.getId(), newEntity);
        Assert.assertFalse(b);

    }

    @Test
    public void testDeleteTrue() {
        CrudRepository<Entity> dao = getMockedRepository();
        Entity e = createEntity();
        when(dao.delete(e.getId())).thenReturn(true);

        boolean b = getService().delete(getManager(), e.getId());
        Assert.assertTrue(b);
    }

    @Test
    public void testDeleteFalse() {
        CrudRepository<Entity> dao = getMockedRepository();
        Entity e = createEntity();
        when(dao.delete(e.getId())).thenReturn(false);

        boolean b = getService().delete(getManager(), e.getId());
        Assert.assertFalse(b);
    }

    protected User getManager() {
        User user = new User();
        user.setId(-1);
        user.setRole(Role.MANAGER);
        return user;
    }

    protected void beforeGeneratingEntity() {
    }
    protected abstract CrudService<Entity> getService();

    protected abstract CrudRepository<Entity> getMockedRepository();

    protected abstract Entity createEntity();
}
