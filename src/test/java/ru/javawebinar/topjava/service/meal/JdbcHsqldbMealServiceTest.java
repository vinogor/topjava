package ru.javawebinar.topjava.service.meal;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(inheritProfiles = false, profiles = {"jdbc", "hsqldb"} )
public class JdbcHsqldbMealServiceTest extends MealServiceTest {
}
