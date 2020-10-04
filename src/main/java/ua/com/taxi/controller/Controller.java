package ua.com.taxi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {

    String processRequest(HttpServletRequest request, HttpServletResponse response);

}
