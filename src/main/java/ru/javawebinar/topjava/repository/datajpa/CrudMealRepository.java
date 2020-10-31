package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:mealId AND m.user.id=:userId")
    int delete(
            @Param("mealId") int mealId,
            @Param("userId") int userId
    );

    List<Meal> getAllByUser(User user, Sort sort);

    List<Meal> findByUserAndDateTimeGreaterThanEqualAndDateTimeLessThan(
            User user, LocalDateTime startDateTime, LocalDateTime endDateTime, Sort sort);

    @EntityGraph(attributePaths = "user")
    @Query("SELECT m FROM Meal m WHERE m.id=:mealId AND m.user.id=:userId")
    Optional<Meal> findByUserAndId(
            @Param("mealId") int mealId,
            @Param("userId") int userId
    );
}
