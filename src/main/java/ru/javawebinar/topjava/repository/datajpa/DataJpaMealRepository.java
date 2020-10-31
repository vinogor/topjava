package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private static final Sort SORT_DATE_TIME = Sort.by(Sort.Direction.DESC, "dateTime");

    private final CrudMealRepository mealRepo;
    private final CrudUserRepository userRepo;

    public DataJpaMealRepository(CrudMealRepository mealRepo, CrudUserRepository userRepo) {
        this.mealRepo = mealRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        User user = userRepo.getOne(userId); // exception если не существует
        meal.setUser(user);

        // create
        if (meal.isNew()) {
            return mealRepo.save(meal);
        }

        Optional<Meal> mealById = mealRepo.findById(meal.getId()); // уже точно не null

        // update: null если такого meal нет или userId не соответствует
        return isMealExist(userId, mealById) ? mealRepo.save(meal) : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepo.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Optional<Meal> mealById = mealRepo.findByUserAndId(id, userId);
        return mealById.orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        User user = userRepo.getOne(userId);
        return mealRepo.getAllByUser(user, SORT_DATE_TIME);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        User user = userRepo.getOne(userId);
        return mealRepo.findByUserAndDateTimeGreaterThanEqualAndDateTimeLessThan(user, startDateTime, endDateTime, SORT_DATE_TIME);
    }

    private boolean isMealExist(int userId, Optional<Meal> mealById) {
        return mealById.isPresent() && mealById.get().getUser().getId() == userId;
    }
}
