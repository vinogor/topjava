// дебаг в браузере Ctrl + Shift + I, вкладка Network
// http://localhost:8080/topjava/
// request.getParameterMap() - пары ключ-значение get запроса из адресной строки

package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.web.SecurityUtil.setUserId;

public class UserServlet extends HttpServlet {

    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet - forward to users");
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("doPost - forward to users");
        String id = request.getParameter("authUserId");
        log.debug("doPost - auth user = {}", id);
        setUserId(Integer.parseInt(id));
        response.sendRedirect("meals");
    }
}
