package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface MealRepository {
    // null if not found, when updated
    Meal save(int authUserId, Meal meal);

    // false if not found
    boolean delete(int authUserId, int id);

    // null if not found
    Meal get(int authUserId, int id);

    List<Meal> getAll(int authUserId);

    List<Meal> getFiltered(int authUserId,
                           LocalDate localDateFromIncl,
                           LocalDate localDateToIncl,
                           LocalTime localTimeFromIncl,
                           LocalTime localTimeToExcl);
}
