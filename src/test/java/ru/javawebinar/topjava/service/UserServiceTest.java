package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;

public abstract class UserServiceTest extends ServiceTest {

    @Autowired
    protected UserService service;

    @Autowired
    private CacheManager cacheManager;

    @Before // инвалидируем кэш
    public void setUp() throws Exception {
        cacheManager.getCache("users").clear();
    }

    @Test
    public void create() throws Exception {
        User created = service.create(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    public void duplicateMailCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void delete() throws Exception {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    // если в User поле roles LAZY то так не подтянет его
    // но даже если LAZY можно сделать чтобы подтягивал через createNamedQuery, где явно прописан join
    // см 3й способ в JpaUserRepository#delete()
    @Test
    public void get() throws Exception {
        User user = service.get(USER_ID);
        USER_MATCHER.assertMatch(user, USER_WITH_MEALS);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void getByEmail() throws Exception {
        User user = service.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, ADMIN_WITHOUT_MEALS);
    }

    @Test
    public void update() throws Exception {
        User updated = getUpdatedWithEmptyMeals();

        service.update(updated);
        USER_MATCHER.assertMatch(service.get(USER_ID), getUpdatedWithMeals());
    }

    @Test
    public void getAll() throws Exception {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, ADMIN_WITHOUT_MEALS, USER_WITH_MEALS);
    }
}