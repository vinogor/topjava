package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javawebinar.topjava.util.MealsUtil.MEALS;

class MealRestControllerTest {

    private static ConfigurableApplicationContext appCtx;
    private static MealRestController controller;
    private static InMemoryMealRepository repository;

    @BeforeAll
    static void initAll() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
        repository = appCtx.getBean(InMemoryMealRepository.class);
    }

    @AfterAll
    static void tearDownAll() {
        appCtx.close();
    }

    @BeforeEach
    void setUp() {
        repository.cleanAll();
        MEALS.forEach(m -> {
            m.setId(null); // т.к. при втором проходе id в MEALS уже не null
            repository.save(1, m);
        });
    }

    @Test
    void create() {
        Meal meal = new Meal(LocalDateTime.of(2222, Month.JANUARY, 30, 10, 0), "qqqqqqq", 500);

        Meal result = controller.create(meal);

        assertNotEquals(meal, result);
        assertNotEquals(null, result.getId());
        assertEquals(meal.getDateTime(), result.getDateTime());
        assertEquals(meal.getDescription(), result.getDescription());
        assertEquals(meal.getCalories(), result.getCalories());
    }

    @Test
    void createNotNew() {
        Meal meal = new Meal(1, LocalDateTime.of(2222, Month.JANUARY, 30, 10, 0), "qqqqqqq", 500);

        assertThrows(IllegalArgumentException.class, () -> {
            controller.create(meal);
        });
    }

    @Test
    void createNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.create(null);
        });
    }

    @Test
    void get() {
        Meal meal = controller.get(1);

        assertEquals(1, meal.getId());
    }

    @Test
    void getNotFoundEx() {
        assertThrows(NotFoundException.class, () -> {
            controller.get(0);
        });
    }

    @Test
    void updateNotConsistent() {
        Meal meal = new Meal(1, LocalDateTime.of(2222, Month.JANUARY, 30, 10, 0), "qqqqqqq", 500);

        assertThrows(IllegalArgumentException.class, () -> {
            controller.update(meal, 2);
        });
    }

    @Test
    void updateWithoutId() {
        Meal meal = new Meal(LocalDateTime.of(2222, Month.JANUARY, 30, 10, 0), "qweqwe", 500);

        controller.update(meal, 2);
        Meal result = controller.get(2);

        assertNotEquals(meal, result);
        assertNotEquals(null, result.getId());
        assertEquals(2, result.getId());
        assertEquals(meal.getDateTime(), result.getDateTime());
        assertEquals(meal.getDescription(), result.getDescription());
        assertEquals(meal.getCalories(), result.getCalories());
    }

    @Test
    void updateWithId() {
        Meal meal = new Meal(2, LocalDateTime.of(2222, Month.JANUARY, 30, 10, 0), "qweqwe", 500);

        controller.update(meal, 2);
        Meal result = controller.get(2);

        assertEquals(meal, result);
        assertEquals(meal.getId(), result.getId());
        assertEquals(meal.getDateTime(), result.getDateTime());
        assertEquals(meal.getDescription(), result.getDescription());
        assertEquals(meal.getCalories(), result.getCalories());
    }

    @Test
    void updateNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.update(null, 1);
        });
    }

    @Test
    void delete() {
        controller.delete(3);
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> {
            controller.delete(0);
        });
    }

    @Test
    void getAll() {
        List<MealTo> meals = controller.getAll();

        assertEquals(meals.size(), MEALS.size());
        assertSortedByDateTime(meals);
    }

    @Test
    void getFiltered() {
        List<MealTo> meals = controller.getFiltered(
                LocalDate.of(2020, Month.JANUARY, 30).toString(),
                LocalDate.of(2020, Month.JANUARY, 31).toString(),
                LocalTime.of(10, 0).toString(),
                LocalTime.of(20, 0).toString() // исключая!
        );

        assertEquals(MEALS.size() - 3, meals.size());
        assertSortedByDateTime(meals);
    }

    @Test
    void getFilteredWith1Null_1() {
        List<MealTo> meals = controller.getFiltered(
                null,
                LocalDate.of(2020, Month.JANUARY, 31).toString(),
                LocalTime.of(10, 0).toString(),
                LocalTime.of(20, 0).toString()
        );
        assertEquals(MEALS.size() - 3, meals.size());
        assertSortedByDateTime(meals);
    }

    @Test
    void getFilteredWith1Null_2() {
        List<MealTo> meals = controller.getFiltered(
                LocalDate.of(2020, Month.JANUARY, 30).toString(),
                LocalDate.of(2020, Month.JANUARY, 31).toString(),
                null,
                LocalTime.of(20, 0).toString()
        );
        assertEquals(MEALS.size() - 2, meals.size());
        assertSortedByDateTime(meals);
    }

    @Test
    void getFilteredWith1Null_3() {
        List<MealTo> meals = controller.getFiltered(
                LocalDate.of(2020, Month.JANUARY, 30).toString(),
                null,
                LocalTime.of(10, 0).toString(),
                LocalTime.of(20, 0).toString()
        );
        assertEquals(MEALS.size() - 3, meals.size());
        assertSortedByDateTime(meals);
    }

    @Test
    void getFilteredWith1Null_4() {
        List<MealTo> meals = controller.getFiltered(
                LocalDate.of(2020, Month.JANUARY, 30).toString(),
                LocalDate.of(2020, Month.JANUARY, 31).toString(),
                LocalTime.of(10, 0).toString(),
                null
        );
        assertEquals(MEALS.size() - 1, meals.size());
        assertSortedByDateTime(meals);
    }

    @Test
    void getFilteredWithAllNulls() {
        List<MealTo> meals = controller.getFiltered(
                null,
                null,
                null,
                null
        );
        assertEquals(meals.size(), MEALS.size());
        assertSortedByDateTime(meals);
    }

    private void assertSortedByDateTime(List<MealTo> meals) {
        for (int i = 1; i < meals.size(); i++) {
            assertTrue(meals.get(i - 1).getDateTime().compareTo(meals.get(i).getDateTime()) >= 0);
        }
    }
}