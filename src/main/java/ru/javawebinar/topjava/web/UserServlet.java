package ru.javawebinar.topjava.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// дебаг в браузере Ctrl + Shift + I, вкладка Network
// http://localhost:8080/topjava/
public class UserServlet extends HttpServlet {

    @Override // request.getParameterMap() - пары ключ-значение запросов из адресной строки
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.getRequestDispatcher("/users.jsp").forward(request, response);
        response.sendRedirect("users.jsp"); // запрос идёт через браузер
    }
}
