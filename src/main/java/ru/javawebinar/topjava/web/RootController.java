package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        // возвращаем имя вьюхи
        return "index";
    }

}
