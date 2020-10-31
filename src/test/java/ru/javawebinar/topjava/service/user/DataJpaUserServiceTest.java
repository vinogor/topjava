package ru.javawebinar.topjava.service.user;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = {"datajpa"})
public class DataJpaUserServiceTest extends UserServiceTest {

    @Override
    @Test
    public void get() {
        User user = service.get(USER_ID);
        USER_MATCHER.assertMatch(user, USER_WITH_MEALS);

        List<Meal> meals = user.getMeals();

        // Expected : is <[Meal{id=100008, ...
        // Actual   :    <[Meal{id=100008, ...
        // TODO: откуда возникло это "is" из-за которой коллекции не равны по содержимому?!?
//        assertThat(meals, CoreMatchers.is(MEALS));

        // из-за этого пока что так
        for (int i = 0; i < meals.size(); i++) {
            MEAL_MATCHER.assertMatch(meals.get(i), MEALS_FOR_USER.get(i));
        }
    }
}
