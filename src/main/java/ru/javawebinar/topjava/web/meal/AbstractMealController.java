package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MealService mealService;

    protected List<MealTo> getMealTos(LocalDate startLocalDate, LocalDate endLocalDate, LocalTime startLocalTime, LocalTime endLocalTime) {
        List<Meal> mealsDateFiltered = mealService.getBetweenInclusive(startLocalDate, endLocalDate, SecurityUtil.authUserId());
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startLocalTime, endLocalTime);
    }

    protected int deleteById(int mealId) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", mealId, userId);
        mealService.delete(mealId, userId);
        return userId;
    }

    protected List<MealTo> getAllTos() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        return MealsUtil.getTos(mealService.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

}
