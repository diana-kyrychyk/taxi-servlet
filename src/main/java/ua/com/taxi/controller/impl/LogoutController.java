package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.controller.MainServlet;
import ua.com.taxi.filter.AuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutController implements Controller {

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(AuthenticationFilter.LOGGINED_USER_ID);
        request.getSession().removeAttribute(AuthenticationFilter.LOGGINED_USER_NAME);
        request.getSession().removeAttribute(AuthenticationFilter.LOGGINED_USER_ROLES);
        return MainServlet.REDIRECT_PREFIX.concat("/");
    }
}
