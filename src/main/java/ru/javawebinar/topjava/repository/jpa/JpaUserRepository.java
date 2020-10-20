package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
// чтение есть смысл делать в транзакции - ибо оптимизации (см ссылку в уроке)
// применяется ко всем методам, кто тех, где явно прописано иное. Аннотация Spring
public class JpaUserRepository implements UserRepository {

//    если бы работали с Hibernate без JPA, то надо было бы вот это:
/*
    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.getCurrentSession();
    }
*/

    //  это замена вышеперечисленному
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional // нужна там, где что-то меняется, аннотация JPA
    public User save(User user) {
        if (user.isNew()) {
            em.persist(user);
            return user;
        } else {
            // em.merge - при отсутствии старой записи (несуществующий id) создает новую.
            // Т. е. в JpaUserRepository нарушается логика
            return em.merge(user);
        }
    }

    @Override
    public User get(int id) {
        return em.find(User.class, id);
    }

    @Override
    @Transactional
    public boolean delete(int id) {

//        == 1й способ
//        если в контексте есть, то достаёт весь объект
//        если в контексте нет, то делает прокси (инициализирует только id, остальные lazy) и его возвращает
//        User ref = em.getReference(User.class, id);
//        удаляем объект (достаточно его прокси с заданным id-ником)
//        em.remove(ref);

//        == 2й способ - запрос ниже на языке JPQL (Java Persistence)
//        ещё бывает HSQL (Hibernate)
//        TypedQuery<User> query
//        Query query = em.createQuery("DELETE FROM User u WHERE u.id=:id");
//        return query.setParameter("id", id).executeUpdate() != 0;

//        == 3й способ - через NamedQuery, в константе JPQL
//        ++ запросы создаются, проверяются и кешируются на этапе загрузки Hibernate
        return em.createNamedQuery(User.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = em.createNamedQuery(User.BY_EMAIL, User.class)
                .setParameter(1, email)
                .getResultList();
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return em.createNamedQuery(User.ALL_SORTED, User.class).getResultList();
    }
}
