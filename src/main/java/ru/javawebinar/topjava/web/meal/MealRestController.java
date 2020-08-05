package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.util.ValidationUtil.*;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        Objects.requireNonNull(meal, "meal must not be null");
        checkNew(meal);
        return service.create(authUserId(), new Meal(meal));
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(authUserId(), id);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        Objects.requireNonNull(meal, "meal must not be null");
        Meal cloneMeal = new Meal(meal);
        assureIdConsistent(cloneMeal, id);
        service.update(authUserId(), cloneMeal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(authUserId(), id);
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        List<Meal> meals = service.getAll(authUserId());
        return getTos(meals, DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getFiltered(String dateFromIncl, String dateToIncl,
                                    String timeFromIncl, String timeToExcl) {
        log.info("getFiltered");

        //  "Проверьте корректную обработку пустых значений date и time...
        LocalDate localDateFromIncl = isExist(dateFromIncl) ? LocalDate.parse(dateFromIncl) : LocalDate.MIN;
        LocalDate localDateToIncl = isExist(dateToIncl) ? LocalDate.parse(dateToIncl) : LocalDate.MAX;
        LocalTime localTimeFromIncl = isExist(timeFromIncl) ? LocalTime.parse(timeFromIncl) : LocalTime.MIN;
        //                                                  +1 nanos чтобы было include а не exclude при фильтрации
        LocalTime localTimeToExcl = isExist(timeToExcl) ? LocalTime.parse(timeToExcl) : LocalTime.MAX.plusNanos(1L);

        //  ... в частности, если все значения пустые, должен выводится весь список)"
        if (localDateFromIncl.equals(LocalDate.MIN)
                && localDateToIncl.equals(LocalDate.MAX)
                && localTimeFromIncl.equals(LocalTime.MIN)
                && localTimeToExcl.equals(LocalTime.MAX.plusNanos(1L))
        ) {
            return getAll();
        }

        return getFilteredByTimeTos(
                service.getFiltered(authUserId(), localDateFromIncl, localDateToIncl),
                DEFAULT_CALORIES_PER_DAY,
                localTimeFromIncl, localTimeToExcl.minusNanos(1L));
    }

    private boolean isExist(String str) {
        return str != null && !str.isEmpty();
    }
}