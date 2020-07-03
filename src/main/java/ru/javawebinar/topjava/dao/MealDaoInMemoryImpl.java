package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.MealsUtil.MEALS_HARDCODE;

public class MealDaoInMemoryImpl implements MealDao {

    private static final Logger log = LoggerFactory.getLogger(MealDaoInMemoryImpl.class);

    // volatile при ++ не помогает от многопоточности. Почему?
    // --> потому что ++ это не атомарное действие

    private final AtomicInteger lastId;
    private final Map<Integer, Meal> store;

    public MealDaoInMemoryImpl() {
        this.store = new ConcurrentSkipListMap<>();
        this.lastId = new AtomicInteger(0);
        init();
    }

    private void init() {
        Map<Integer, Meal> tempMap = MEALS_HARDCODE.stream()
                .collect(Collectors.toMap(Meal::getId, meal -> meal));
        this.store.putAll(tempMap);
        lastId.updateAndGet(c -> c + MEALS_HARDCODE.size());
        log.debug("lastId = " + lastId);
    }

    @Override
    public void create(Meal meal) {
        log.debug("create");
        lastId.incrementAndGet();
        int localLastId = this.lastId.get();
        meal.setId(localLastId);
        store.put(localLastId, meal);
        log.debug("lastId = " + localLastId);
    }

    @Override
    public Meal read(int id) {
        log.debug("read");
        return store.get(id);
    }

    @Override
    public void update(Meal meal) {
        log.debug("update");
        store.put(meal.getId(), new Meal(meal));
    }

    @Override
    public void delete(int id) {
        log.debug("delete");
        int localLastId = this.lastId.get();
        store.remove(id);
        log.debug("lastId = " + localLastId);
    }

    @Override
    public List<Meal> getAll() {
        log.debug("getAll");
        return new ArrayList<>(store.values());
    }
}
