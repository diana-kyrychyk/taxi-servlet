package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginGetController implements Controller {

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        return "/WEB-INF/user/user-login.jsp";
    }
}
