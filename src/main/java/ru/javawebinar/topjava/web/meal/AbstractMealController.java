package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public abstract class AbstractMealController {

    @Autowired
    protected MealService mealService;

    protected List<MealTo> getMealTos(LocalDate startLocalDate, LocalDate endLocalDate, LocalTime startLocalTime, LocalTime endLocalTime) {
        List<Meal> mealsDateFiltered = mealService.getBetweenInclusive(startLocalDate, endLocalDate, SecurityUtil.authUserId());
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startLocalTime, endLocalTime);
    }

}
