package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping("/delete/{mealId}")
    public String delete(Model model, @PathVariable int mealId) {
        model.addAttribute("meals", MealsUtil.getTos(mealService.getAll(deleteById(mealId)), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", getAllTos());
        return "meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        log.info("GET: create {} for user {}", meal, userId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/update/{mealId}")
    public String update(Model model, @PathVariable int mealId) {
        int userId = SecurityUtil.authUserId();
        Meal meal = mealService.get(mealId, SecurityUtil.authUserId());
        log.info("GET: update {} for user {}", meal, userId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    // https://stackoverflow.com/questions/63325484/problem-in-binding-form-with-get-method-to-a-getmapping-controller
    @GetMapping("/filter")
    public String filter(Model model,
                         @RequestParam String startDate, @RequestParam String endDate,
                         @RequestParam String startTime, @RequestParam String endTime) {

        LocalDate startLocalDate = parseLocalDate(startDate);
        LocalDate endLocalDate = parseLocalDate(endDate);
        LocalTime startLocalTime = parseLocalTime(startTime);
        LocalTime endLocalTime = parseLocalTime(endTime);

        model.addAttribute("meals", getMealTos(startLocalDate, endLocalDate, startLocalTime, endLocalTime));
        return "meals";
    }

    @PostMapping("/save")
    public String save(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = getNewMeal(request);
        String paramId = request.getParameter("id");
        int userId = SecurityUtil.authUserId();

        if (paramId == null || paramId.isEmpty()) {
            log.info("POST: create {} for user {}", meal, userId);
            mealService.create(meal, userId);
        } else {
            log.info("POST: update {} for user {}", meal, userId);
            meal.setId(Integer.parseInt(paramId));
            mealService.update(meal, userId);
        }
        model.addAttribute("meals", MealsUtil.getTos(mealService.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    private Meal getNewMeal(HttpServletRequest request) {
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }

}
