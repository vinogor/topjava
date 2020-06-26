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
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealCrudServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MealCrudServlet.class);

    private static final long serialVersionUID = 1L;
    public static final int CALORIES_PER_DAY = 2000;
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("start doGet");
        String forward = "";
        String action = request.getParameter("action");
        action = action == null ? "" : action; // чтобы не ловить NPE при пустом запросе

        // delete + getAll
        if (action.equalsIgnoreCase("delete")) {
            log.debug("doGet - delete");
            int id = Integer.parseInt(request.getParameter("id"));
            dao.delete(id);
            request.setAttribute("meals", filteredByStreams(dao.getAll(), CALORIES_PER_DAY));
            forward = MEALS_TO;

            // read
        } else if (action.equalsIgnoreCase("edit")) {
            log.debug("doGet - edit");
            int id = Integer.parseInt(request.getParameter("id"));
            Meal meal = dao.read(id);
            request.setAttribute("meal", meal);
            forward = INSERT_OR_EDIT;

            // getAll
        } else if (action.equalsIgnoreCase("list")) {
            log.debug("doGet - list");
            request.setAttribute("meals", filteredByStreams(dao.getAll(), CALORIES_PER_DAY));
            forward = MEALS_TO;

            // insert
        } else if (action.equalsIgnoreCase("insert")) {
            log.debug("doGet - insert");
            forward = INSERT_OR_EDIT;

            // пустой или нераспознанный параметр action у get запроса --> getAll
            // при редиректе без параметров?
        } else {
            log.debug("doGet - empty ( -> list)");
            request.setAttribute("meals", filteredByStreams(dao.getAll(), CALORIES_PER_DAY));
            forward = MEALS_TO;
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
                Integer.parseInt(request.getParameter("calories"))
        );

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            log.debug("doPost - create");
            dao.create(meal);
        } else {
            log.debug("doPost - update");
            meal.setId(Integer.parseInt(id));
            dao.update(meal);
        }

        RequestDispatcher view = request.getRequestDispatcher(MEALS_TO);
        request.setAttribute("meals", filteredByStreams(dao.getAll(), CALORIES_PER_DAY));
        view.forward(request, response);

    }
}
