package ru.javawebinar.topjava.service.user;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = {"jdbc"})
public class JdbcUserServiceTest extends UserServiceTest {
}
