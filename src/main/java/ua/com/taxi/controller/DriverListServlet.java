package ua.com.taxi.controller;

import ua.com.taxi.model.Driver;
import ua.com.taxi.service.DriverService;
import ua.com.taxi.service.impl.DriverServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/driver-list")
public class DriverListServlet extends HttpServlet {

    private DriverService driverService = new DriverServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Driver> driverList = driverService.findAll();

        request.setAttribute("drivers", driverList);

        request.getRequestDispatcher("/WEB-INF/driver/driver-list.jsp").forward(request, response);
    }
}
