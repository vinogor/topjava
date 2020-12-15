package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.RootController;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends RootController {

    // 1.3.3 добавить локализацию и jsp:include в mealForm.jsp / meals.jsp

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", getTos());
        return "meals";
    }


    @GetMapping("/delete/{mealId}")
    public String delete(Model model, @PathVariable int mealId) {
        mealService.delete(mealId, SecurityUtil.authUserId());
        model.addAttribute("meals", getTos());
        return "meals";
    }

    @GetMapping("/update/{mealId}")
    public String update(Model model, @PathVariable int mealId) {
        Meal meal = mealService.get(mealId, SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/create")
    public String create(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
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

        List<Meal> mealsDateFiltered = mealService.getBetweenInclusive(startLocalDate, endLocalDate, SecurityUtil.authUserId());
        List<MealTo> filteredTos = MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startLocalTime, endLocalTime);
        model.addAttribute("meals", filteredTos);
        return "meals";
    }

    @PostMapping("/update")
    public String update(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = getNewMeal(request);
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        meal.setId(Integer.parseInt(paramId));
        mealService.update(meal, SecurityUtil.authUserId());
        model.addAttribute("meals", getTos());
        return "meals";
    }

    @PostMapping("/create")
    public String create(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = getNewMeal(request);
        mealService.create(meal, SecurityUtil.authUserId());
        model.addAttribute("meals", getTos());
        return "meals";
    }

    private List<MealTo> getTos() {
        return MealsUtil.getTos(mealService.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    private Meal getNewMeal(HttpServletRequest request) {
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }

}
