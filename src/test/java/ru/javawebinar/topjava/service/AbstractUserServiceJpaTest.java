package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.repository.JpaUtil;

import java.util.Arrays;

import static ru.javawebinar.topjava.Profiles.JDBC;

public abstract class AbstractUserServiceJpaTest extends AbstractUserServiceTest {

    @Autowired
    protected JpaUtil jpaUtil;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        if (!Arrays.asList(env.getActiveProfiles()).contains(JDBC)) {
            jpaUtil.clear2ndLevelHibernateCache();
        }
    }

}
