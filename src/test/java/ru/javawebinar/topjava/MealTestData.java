package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int NOT_FOUND = 10;
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final Meal MEAL_NEW = new Meal(LocalDateTime.of(2021, Month.JANUARY, 6, 6, 6), "test_description", 500);

    public static final Meal MEAL_1 = new Meal(100004, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL_2 = new Meal(100003, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL_3 = new Meal(100002, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);

    public static final Meal MEAL_ADMIN_1 = new Meal(100010, LocalDateTime.of(2020, Month.JANUARY, 30, 21, 0), "Админ ужин", 1500);
    public static final Meal MEAL_ADMIN_2 = new Meal(100009, LocalDateTime.of(2020, Month.JANUARY, 30, 14, 0), "Админ ланч", 500);

    public static Meal getNew() {
        return new Meal(MEAL_NEW);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL_NEW);
        updated.setCalories(123);
        updated.setDescription("new description");
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
