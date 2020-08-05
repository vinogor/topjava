package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private static final Logger log = LoggerFactory.getLogger(MealService.class);

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(int authUserId, Meal meal) {
        log.info("create {}", meal);
        return repository.save(authUserId, meal);
    }

    public Meal get(int authUserId, int id) {
        log.info("get {}", id);
        return checkNotFoundWithId(repository.get(authUserId, id), id);
    }

    public void update(int authUserId, Meal meal) {
        log.info("update {}", meal);
        checkNotFoundWithId(repository.save(authUserId, meal), meal.getId());
    }

    public void delete(int authUserId, int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(authUserId, id), id);
    }

    public List<Meal> getAll(int authUserId) {
        log.info("getAll");
        return repository.getAll(authUserId);
    }

    public List<Meal> getFiltered(int authUserId, LocalDate localDateFromIncl, LocalDate localDateToIncl) {
        log.info("getFiltered");
        return repository.getFilteredByDate(authUserId, localDateFromIncl, localDateToIncl
        );
    }
}