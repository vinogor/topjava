package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(
        scripts = {"classpath:db/initDB.sql", "classpath:db/populateDB.sql"},
        config = @SqlConfig(encoding = "UTF-8")
)
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    private MealRepository repository;

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);

        assertMatch(created, newMeal);
        assertMatch(repository.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateMealCreate() {
        assertThrows(DataAccessException.class, () ->
                {
                    service.create(getNew(), USER_ID);
                    getNew();
                    service.create(getNew(), USER_ID);
                }
        );
    }

    @Test
    public void delete() {
        Integer id = service.create(getNew(), USER_ID).getId();
        service.delete(id, USER_ID);
        assertNull(repository.get(id, USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void get() {
        Meal newMeal = service.create(getNew(), USER_ID);
        Integer id = newMeal.getId();
        Meal meal = service.get(id, USER_ID);
        assertMatch(newMeal, meal);
    }


    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getFromWrongUserNotFound() {
        Meal newMeal = service.create(getNew(), USER_ID);
        Integer id = newMeal.getId();
        assertThrows(NotFoundException.class, () -> service.get(id, ADMIN_ID));
    }

    @Test
    public void update() {
        Meal createdMeal = service.create(getNew(), USER_ID);
        Integer id = createdMeal.getId();
        Meal updated = getUpdated();
        updated.setId(id);

        service.update(updated, USER_ID);

        assertMatch(service.get(id, USER_ID), updated);
    }

    @Test
    public void updateFromWrongUser() {
        Meal createdMeal = service.create(getNew(), USER_ID);
        Integer id = createdMeal.getId();
        Meal updated = getUpdated();
        updated.setId(id);

        service.update(updated, ADMIN_ID);

        assertThrows(NotFoundException.class, () -> assertEquals(service.get(id, USER_ID), updated));
    }

    @Test
    public void getAllDescByDate() {
        List<Meal> result = service.getAll(ADMIN_ID);
        assertMatch(result, MEAL_ADMIN_1, MEAL_ADMIN_2);
    }

    @Test
    public void getBetweenInclusiveDescByDate() {
        List<Meal> result = service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30),
                USER_ID
        );
        assertMatch(result, MEAL_1, MEAL_2, MEAL_3);
    }
}