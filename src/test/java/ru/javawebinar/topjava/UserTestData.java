package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {

    public static TestMatcher<User> USER_MATCHER = TestMatcher.usingFieldsComparator("registered", "roles", "meals");

    public static final int NOT_FOUND = 10;
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User USER_WITH_MEALS;

    public static final List<Meal> MEALS_FOR_USER = new ArrayList<>() {{
        add(MEAL7);
        add(MEAL6);
        add(MEAL5);
        add(MEAL4);
        add(MEAL3);
        add(MEAL2);
        add(MEAL1);
    }};

    static {
        USER_WITH_MEALS = new User(USER_ID, "User", "user@yandex.ru", "password", MEALS_FOR_USER, Role.USER);
        MEAL1.setUser(USER_WITH_MEALS);
        MEAL2.setUser(USER_WITH_MEALS);
        MEAL3.setUser(USER_WITH_MEALS);
        MEAL4.setUser(USER_WITH_MEALS);
        MEAL5.setUser(USER_WITH_MEALS);
        MEAL6.setUser(USER_WITH_MEALS);
        MEAL7.setUser(USER_WITH_MEALS);
    }

    public static final User USER_WITHOUT_MEALS = new User(USER_ID, "User", "user@yandex.ru", "password", null, Role.USER);
    public static final User ADMIN_WITHOUT_MEALS = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", null, Role.ADMIN);

    public static User getNew() {
        return new User(
                null,
                "New",
                "new@gmail.com",
                "newPass",
                1555,
                false,
                new Date(),
                Collections.singleton(Role.USER),
                null);
    }

    public static User getUpdatedWithMeals() {
        User updated = new User(USER_WITH_MEALS);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        return updated;
    }

    public static User getUpdatedWithEmptyMeals() {
        User updated = new User(USER_WITHOUT_MEALS);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        return updated;
    }
}
