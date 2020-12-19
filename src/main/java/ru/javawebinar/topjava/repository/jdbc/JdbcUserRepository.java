package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

import java.lang.reflect.Array;
import java.util.*;

import static ru.javawebinar.topjava.util.ValidationUtil.validateForJdbc;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> USER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final ResultSetExtractor<User> USER_WITH_ROLES_EXTRACTOR = rs -> {
        Set<Role> roles = new HashSet<>();
        User user;
        if (rs.next()) {
            user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("calories_per_day"),
                    rs.getBoolean("enabled"),
                    rs.getDate("registered"),
                    null);
            roles.add(Role.valueOf(rs.getString("role")));
        } else {
            return null;
        }
        while (rs.next()) {
            roles.add(Role.valueOf(rs.getString("role")));
        }
        user.setRoles(roles);
        return user;
    };

    private static final ResultSetExtractor<Map<Integer, Set<Role>>> ROLES_EXTRACTOR = rs -> {
        Map<Integer, Set<Role>> result = new HashMap<>();
        while (rs.next()) {
            int id = rs.getInt("user_id");
            result.computeIfAbsent(id, k -> new HashSet<>());
            result.get(id).add(Role.valueOf(rs.getString("role")));
        }
        return result;
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

        Map<String, String>[] params = getParams(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSourceUser);
            user.setId(newKey.intValue());

            namedParameterJdbcTemplate.batchUpdate(
                    "INSERT INTO user_roles VALUES (" + newKey + ", :role)", params);

        } else {
            int updateUsers = namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id",
                    parameterSourceUser);

            // TODO: удалить всё, а потом добавить? или как то ещё можно обновить?
            //       но как тогда учесть были ли реальные изменения в ролях?
            Integer id = user.getId();
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id);
            namedParameterJdbcTemplate.batchUpdate("INSERT INTO user_roles VALUES (" + id + ", :role)", params);

            if (updateUsers == 0) {
                return null;
            }
        }
        return user;
    }

    // TODO: подумать как бы сделать красивее
    private Map<String, String>[] getParams(User user) {
        Set<Role> roles = user.getRoles();
        Iterator<Role> iterator = roles.iterator();
        int size = roles.size();
        Map<String, String>[] parameters = (Map<String, String>[]) Array.newInstance(HashMap.class, size);
        for (int i = 0; i < size; i++) {
            parameters[i] = new HashMap<>() {{
                put("role", iterator.next().toString());
            }};
        }
        return parameters;
    }

    @Override
    public boolean delete(int id) {
        // а роли удалятся сами по каскаду
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        return jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_roles ur on u.id = ur.user_id WHERE id=?",
                USER_WITH_ROLES_EXTRACTOR, id);
    }

    @Override
    public User getByEmail(String email) {
        return jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_roles ur on u.id = ur.user_id WHERE email=?",
                USER_WITH_ROLES_EXTRACTOR, email);
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", USER_ROW_MAPPER);
        Map<Integer, Set<Role>> roles = jdbcTemplate.query("SELECT * FROM user_roles", ROLES_EXTRACTOR);
        users.forEach(user -> user.setRoles(roles.get(user.getId())));
        return users;
    }
}
