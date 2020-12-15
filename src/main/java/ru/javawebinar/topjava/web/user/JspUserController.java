package ru.javawebinar.topjava.web.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.web.RootController;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/users")
public class JspUserController extends RootController {

    @GetMapping
    public String getUsers(Model model) {
        // заполняем инфой атрибут, который заюзаем в соответствующей вьюхе через ${users}
        model.addAttribute("users", userService.getAll());
        return "users";
    }

    @PostMapping
    public String setUser(HttpServletRequest request, Model model) {
        int userId = Integer.parseInt(request.getParameter("userId"));
        SecurityUtil.setAuthUserId(userId);
        model.addAttribute("meals", mealService.getAll(userId));
        return "redirect:meals";
    }
}
