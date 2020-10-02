package ua.com.taxi.controller;

import ua.com.taxi.filter.AuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user-logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute(AuthenticationFilter.LOGGINED_USER_ID);
        req.getSession().removeAttribute(AuthenticationFilter.LOGGINED_USER_NAME);
        req.getSession().removeAttribute(AuthenticationFilter.LOGGINED_USER_ROLES);
        resp.sendRedirect("/");
    }
}
