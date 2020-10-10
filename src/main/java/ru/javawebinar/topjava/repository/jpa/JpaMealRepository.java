package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    // "При записи в базу через namedQuery валидация энтити не работает, только валидация в БД."
    public Meal save(Meal meal, int userId) {

        // User userRef = em.getReference(User.class, userId); // если не найдено то EntityNotFoundException -> не подходит, т.к. надо без try-catch

        // create:

        if (meal.isNew()) {
            // проверка а существует ли вообще такой юзер
            User user = em.find(User.class, userId); // ищет в контексте. А в самой базе?
            if (user == null) {
                return null;
            }
            meal.setUser(user);
            em.persist(meal);
            return meal;
        }

        // update:

        // "em.merge - при отсутствии старой записи (несуществующий id) создает новую (БАГА!), т. е. в Jpa[Entity]Repository нарушается логика"
        // вдруг у meal, который хотим сохранить, задан несуществующий id и/или не соответствующий userId
        Meal mealFromDb = getMealFromDb(meal.getId(), userId);
        if (mealFromDb == null) {
            return null;
        }
        meal.setUser(em.getReference(User.class, userId)); // точно найдёт -> exception не кинет

        // "entity, переданный в merge, не меняется. Нужно использовать возвращаемый результат"
        // ???: то есть возвращается новая сущность такого же типа?
        return em.merge(meal);
    }

    @Override
    public Meal get(int id, int userId) {
        return getMealFromDb(id, userId);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }


    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED_HALF_OPEN, Meal.class)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .setParameter("userId", userId)
                .getResultList();
    }

    private Meal getMealFromDb(int id, int userId) {
        return em.createNamedQuery(Meal.GET_MEAL_BY_ID_AND_USER_ID, Meal.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst().orElse(null);
    }
}