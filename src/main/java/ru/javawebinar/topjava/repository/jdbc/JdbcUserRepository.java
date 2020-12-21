package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static ru.javawebinar.topjava.util.ValidationUtil.validateForJdbc;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final ResultSetExtractor<List<User>> EXTRACTOR = rs -> {

        List<User> users = new LinkedList<>(); // если из базы приходит уже в отсортированном виде
        Set<Role> roles = new HashSet<>();
        int idPrev = 0;
        User user = null;

        while (rs.next()) {
            int id = rs.getInt("id");
            if (idPrev != id) {
                idPrev = id;
                // сохр роли предыдущего юзера, добавили/обнулили роли
                if (user != null) {
                    user.setRoles(roles);
                    users.add(0, user);
                    roles = new HashSet<>();
                }

                // новый юзер
                user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("calories_per_day"),
                        rs.getBoolean("enabled"),
                        rs.getDate("registered"),
                        null);

            }
            roles.add(Role.valueOf(rs.getString("role")));

        }

        if (user != null) {
            user.setRoles(roles);
            users.add(0, user);
        }

        return users;
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id"); // автогенерация id при вставке

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        //         MapSqlParameterSource map = new MapSqlParameterSource()
        //                .addValue("id", user.getId())
        //                .addValue("name", user.getName())
        //                .addValue("email", user.getEmail())
        //                .addValue("password", user.getPassword())
        //                .addValue("registered", user.getRegistered())
        //                .addValue("enabled", user.isEnabled())
        //                .addValue("caloriesPerDay", user.getCaloriesPerDay());

        validateForJdbc(user);

        BeanPropertySqlParameterSource parameterSourceUser = new BeanPropertySqlParameterSource(user);

        Integer id;

        if (user.isNew()) {
            id = (Integer) insertUser.executeAndReturnKey(parameterSourceUser);
            user.setId(id);

        } else {
            id = user.getId();
            int updateUsers = namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id",
                    parameterSourceUser);
            if (updateUsers == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id);
        }

        Set<Role> roles = user.getRoles();
        int size = roles.size();
        Iterator<Role> iterator = roles.iterator();
        jdbcTemplate.batchUpdate("INSERT INTO user_roles VALUES (" + id + ", ?)", new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, iterator.next().toString());
            }

            @Override
            public int getBatchSize() {
                return size;
            }
        });

        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        // а роли удалятся сами по каскаду
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_roles ur on u.id = ur.user_id WHERE id=?",
                EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_roles ur on u.id = ur.user_id WHERE email=?",
                EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_roles ur on u.id = ur.user_id", EXTRACTOR);
    }
}
