package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoInMemoryImpl implements MealDao {

    private static final Logger log = LoggerFactory.getLogger(MealDaoInMemoryImpl.class);

    // volatile при ++ не помогает от многопоточности. Почему?
    // --> потому что ++ это не атомарное действие

    private final AtomicInteger lastId;
    private final Map<Integer, Meal> store;

    public MealDaoInMemoryImpl() {
        this.store = new ConcurrentSkipListMap<>();
        this.lastId = new AtomicInteger(0);
    }

    @Override
    public synchronized void create(Meal meal) {
        log.debug("create");
        meal.setId(lastId.get() + 1);
        store.put(lastId.get() + 1, meal);
        lastId.incrementAndGet();
        log.debug("lastId = " + lastId);
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
        store.remove(id);
        log.debug("lastId = " + lastId);
    }

    @Override
    public List<Meal> getAll() {
        log.debug("getAll");
        return new ArrayList<>(store.values());
    }
}
