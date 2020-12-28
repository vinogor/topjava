package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceJpaTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceJpaTest {

    @Test
    public void getUserWithMeals() throws Exception {
        User user = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(user, USER);
        MealTestData.MEAL_MATCHER.assertMatch(user.getMeals(), MealTestData.USER_MEALS);
    }

    // TODO: исправить - удвоение сущностей в поле meals, DISTINCT не помог, сделал поле meals типом Set
    @Test
    public void getAdminWithMeals() throws Exception {
        User user = service.getWithMeals(ADMIN_ID);
        USER_MATCHER.assertMatch(user, ADMIN);
        MealTestData.MEAL_MATCHER.assertMatch(user.getMeals(), MealTestData.ADMIN_MEALS);
    }

    @Test
    public void getWithMealsNotFound() throws Exception {
        Assert.assertThrows(NotFoundException.class,
                () -> service.getWithMeals(1));
    }
}