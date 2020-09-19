package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcMealRepository.class);

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    @Autowired
    public JdbcMealRepository(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate
    ) {
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id"); // автогенерация id при вставке;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("create or update {} for user {}", meal, userId);

        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("userId", userId)
                .addValue("dateTime", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories());

        // если новое, то просто вставляем
        if (meal.isNew()) {
            Number newKey = insertMeal.executeAndReturnKey(map);
            meal.setId(newKey.intValue());

            // если уже есть, то обновляем
        } else if (
            // возвращает кол-во изменённых строк
                namedParameterJdbcTemplate.update(
                        "UPDATE meals SET date_time=:dateTime, description=:description, " +
                                "calories=:calories WHERE user_id=:userId AND id=:id", map) == 0) {
            return null; // если ничего не обновилось
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete meal {} for user {}", id, userId);
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get meal {} for user {}", id, userId);
        List<Meal> users = jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND user_id=?", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll for user {}", userId);
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id = ? ORDER BY date_time DESC", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        log.info("get filtered by startDate={} endDate={} for user {}", userId, startDate, endDate);
        return jdbcTemplate.query(
                "SELECT * FROM meals WHERE user_id = ? AND date_time >= ? AND date_time < ? ORDER BY date_time DESC",
                ROW_MAPPER, userId, startDate, endDate);
    }
}
