package ru.javawebinar.topjava.service.meal;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = {"datajpa"})
public class DataJpaMealServiceTest extends MealServiceTest {

    @Override
    @Test
    public void get() throws Exception {
        Meal meal = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(meal, ADMIN_MEAL1);

        User user = meal.getUser();
        USER_MATCHER.assertMatch(user, ADMIN_WITH_MEALS);
    }
}
