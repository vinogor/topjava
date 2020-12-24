package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;

@Transactional(readOnly = true)
// после Spring-магии превратится в дефолтную конкретную реализацию (SimpleJpaRepository)
// аннотации о создании бина не надо - SpringDataJpa сама понимает (сканируется пакет + наследование от JpaRepository)
// см иерархию от интерфейса Repository
public interface CrudUserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying // указываем, если надо модифицировать через @Query
    // @Query(name = User.DELETE) // можно и так
    @Query("DELETE FROM User u WHERE u.id=:id")
    // указать @Param("id") чтобы корректно подставилось по имени в запрос
    int delete(@Param("id") int id);
    // см изменение поведения в delete() в DataJpaUserRepository

    // как ни странно, работает без реализации =>
    // Spring анализирует имя метода и понимает, что надо получить user по полю email
    // (формирует запрос через CriteriaApi)
    User getByEmail(String email);

    // TODO: добавление DISTINCT не убирает удвоение записей в meals когда в roles больше 1 записи
    @EntityGraph(attributePaths = {"meals", "roles"})
    @Query("SELECT DISTINCT u FROM User u WHERE u.id=?1")
    User getWithMeals(int id);
}
