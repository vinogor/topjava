package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private static final Sort SORT_DATE_TIME = Sort.by(Sort.Direction.DESC, "dateTime");

    private final CrudMealRepository mealRepo;
    private final CrudUserRepository userRepo;

    public DataJpaMealRepository(CrudMealRepository mealRepo, CrudUserRepository userRepo) {
        this.mealRepo = mealRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        User user = userRepo.getOne(userId); // exception если не существует
        meal.setUser(user);

        // create
        if (meal.isNew()) {
            return mealRepo.save(meal);
        }

        // update: null если такого meal нет или userId не соответствует
        return get(meal.getId(), userId) != null ? mealRepo.save(meal) : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepo.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return mealRepo.findByUserIdAndId(userId, id).orElse(null);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return mealRepo.findByUserIdAndIdWithUser(userId, id).orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepo.getAllByUserId(userId, SORT_DATE_TIME);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return mealRepo.findByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThan(userId, startDateTime, endDateTime, SORT_DATE_TIME);
    }
}
