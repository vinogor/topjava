package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;

@Controller
public class RootController {

    // прилетает из корневого контекста
    @Autowired
    protected UserService userService;

    @Autowired
    protected MealService mealService;

    @GetMapping("/")
    public String root() {
        // возвращаем имя вьюхи
        return "index";
    }

}
