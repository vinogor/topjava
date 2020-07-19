package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {

//        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
//        System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));

//        UserRepository userRepository = (UserRepository) appCtx.getBean("inmemoryUserRepository");
//        UserRepository userRepository = appCtx.getBean(UserRepository.class);
//        userRepository.getAll();
//
//        UserService userService = appCtx.getBean(UserService.class);
//        userService.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
//
//        appCtx.close();

        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
//            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
//            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
//            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);

//            CREATE
//            System.out.println(mealRestController.create( new Meal(LocalDateTime.of(2222, Month.JANUARY, 30, 10, 0), "qqqqqqq", 500)));

//            GET
//            mealRestController.create( new Meal(LocalDateTime.of(2222, Month.JANUARY, 30, 10, 0), "qqqqqqq", 500));
//            System.out.println(mealRestController.get(1));

//            UPDATE
//            mealRestController.create(new Meal(LocalDateTime.of(2222, Month.JANUARY, 30, 10, 0), "qqqqqqq", 500));
//            mealRestController.update(new Meal(LocalDateTime.of(2222, Month.JANUARY, 30, 10, 0), "111qqqqqqq", 500), 1);

//            DELETE
//            mealRestController.create( new Meal(LocalDateTime.of(2222, Month.JANUARY, 30, 10, 0), "qqqqqqq", 500));
//            System.out.println(mealRestController.get(1));
//            mealRestController.delete(1);
//            System.out.println(mealRestController.get(1));

//            GETALL
//            System.out.println(mealRestController.getAll());

//            GETALL FILTERED

//            System.out.println(mealRestController.getFiltered(
//                    LocalDate.of(2020, Month.JANUARY, 30),
//                    LocalTime.of(10, 0),
//                    LocalDate.of(2020, Month.JANUARY, 31),
//                    LocalTime.of(20, 0)
//                    ));


        }
    }
}
