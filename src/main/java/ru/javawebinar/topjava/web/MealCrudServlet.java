package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoInMemoryImpl;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static ru.javawebinar.topjava.util.MealsUtil.*;

public class MealCrudServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MealCrudServlet.class);

    private static final long serialVersionUID = 1L;
    private static final String INSERT_OR_EDIT = "/insert_or_edit_meal.jsp";
    private static final String MEALS_TO = "/meals_crud.jsp";

    private MealDao dao;

    public MealCrudServlet() {
        super(); // зачем??
        dao = new MealDaoInMemoryImpl();
    }

    // --> getParameter() returns http request parameters. Those passed from the client to the server.
    // For example http://example.com/servlet?parameter=1. Can only return String
    //
    // --> getAttribute() is for server-side usage only - you fill the request with attributes that you can use
    // within the same request. For example - you set an attribute in a servlet, and read it from a JSP.
    // Can be used for any object, not just string.

    @Override
    public void init() throws ServletException {
        super.init();
        MEALS_HARDCODE.forEach(m -> dao.create(m));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("start doGet");
        String forward = MEALS_TO;
        String action = request.getParameter("action");
        action = action == null ? "" : action; // чтобы не ловить NPE при пустом запросе

        switch (action) {
            case ("delete"):
                log.debug("doGet - delete");
                dao.delete(parseIntParameter(request, "id"));
                response.sendRedirect("meals_crud");
                return;
            case ("edit"): {
                log.debug("doGet - edit");
                Meal meal = dao.read(parseIntParameter(request, "id"));
                request.setAttribute("meal", meal);
                forward = INSERT_OR_EDIT;
                break;
            }
            case ("list"): {
                log.debug("doGet - list");
                setAttributeMeals(request);
                break;
            }
            case ("insert"): {
                log.debug("doGet - insert");
                forward = INSERT_OR_EDIT;
                break;
            }
            default:
                log.debug("doGet - empty ( -> list)");
                setAttributeMeals(request);
                break;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response); // а при redirect атрибуты теряются
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("start doPost");
        request.setCharacterEncoding("UTF-8");

        Meal meal = new Meal(
                // DateTimeFormatter.ISO_LOCAL_DATE_TIME - идёт по дефолту
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                parseIntParameter(request, "calories")
        );

        int id = parseIntParameter(request, "id");

        if (id <= -1) {
            log.debug("doPost - create");
            dao.create(meal);
        } else {
            log.debug("doPost - update");
            meal.setId(id);
            dao.update(meal);
        }

        RequestDispatcher view = request.getRequestDispatcher(MEALS_TO);
        setAttributeMeals(request);
        view.forward(request, response);
    }

    private void setAttributeMeals(HttpServletRequest request) {
        request.setAttribute("meals", filteredByStreams(dao.getAll(), CALORIES_PER_DAY));
    }

    private int parseIntParameter(HttpServletRequest request, String parameter) {
        String str = request.getParameter(parameter);
        if (str == null || str.isEmpty()) {
            return -1;
        }
        return Integer.parseInt(str);
    }
}
