package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {

    void create(Meal meal);

    Meal read(int id);

    void update(Meal meal);

    void delete(int id);

    List<Meal> getAll();

}
