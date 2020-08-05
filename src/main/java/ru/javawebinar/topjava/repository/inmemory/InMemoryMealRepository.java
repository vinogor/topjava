package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenInclude;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    //          authUserId   mealId   meal
    private Map<Integer, Map<Integer, Meal>> storage = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        log.info("start initializing");
        MealsUtil.MEALS.forEach(m -> save(1, m));
        log.info("end initializing");
    }

    public void cleanAll() {
        log.info("cleanAll");
        storage = new ConcurrentHashMap<>();
        counter = new AtomicInteger(0);
    }

    @Override
    public Meal save(int authUserId, Meal meal) {

        // handle case: create
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            log.info("save {}", meal);
            storage.computeIfAbsent(authUserId, k -> new ConcurrentHashMap<>()).put(meal.getId(), meal);
            return new Meal(meal);
        }

        // handle case: update, but not present in storage
        log.info("update {}", meal);
        if (!isKeyContains(authUserId)) {
            return null;
        }
        Meal result = storage.get(authUserId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        return result == null ? null : new Meal(result);
    }

    @Override
    public Meal get(int authUserId, int id) {
        log.info("get {}", id);

        if (!isKeyContains(authUserId)) {
            return null;
        }
        Meal result = storage.get(authUserId).get(id);
        return result == null ? null : new Meal(result);
    }

    @Override
    public boolean delete(int authUserId, int id) {
        log.info("delete {}", id);

        if (!isKeyContains(authUserId)) {
            return false;
        }
        return storage.get(authUserId).remove(id) != null;
    }

    @Override
    public List<Meal> getAll(int authUserId) {
        log.info("getAll");

        if (!isKeyContains(authUserId)) {
            return Collections.emptyList();
        }
        return toSortedReverseByDateTimeList(storage.get(authUserId).values().stream().map(Meal::new));
    }

    @Override
    public List<Meal> getFilteredByDate(int authUserId, LocalDate localDateFromIncl, LocalDate localDateToIncl) {
        log.info("getFiltered");
        if (!isKeyContains(authUserId)) {
            return Collections.emptyList();
        }
        return toSortedReverseByDateTimeList(storage.get(authUserId).values().stream()
                // "Фильтрацию по ДАТАМ (без учёта времени) сделать в репозитории"
                .filter(m -> isBetweenInclude(m.getDate(), localDateFromIncl, localDateToIncl))
                .map(Meal::new)
        );
    }

    private boolean isKeyContains(int authUserId) {
        return storage.containsKey(authUserId);
    }

    // "список еды возвращать отсортированный в обратном порядке по датам"
    private List<Meal> toSortedReverseByDateTimeList(Stream<Meal> mealStream) {
        return mealStream.sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime())).collect(Collectors.toList());
    }
}

