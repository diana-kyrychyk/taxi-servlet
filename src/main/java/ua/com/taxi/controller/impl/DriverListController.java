package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.model.Driver;
import ua.com.taxi.service.DriverService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class DriverListController implements Controller {

    private DriverService driverService;

    public DriverListController(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        List<Driver> driverList = driverService.findAll();
        request.setAttribute("drivers", driverList);
        return "/WEB-INF/driver/driver-list.jsp";
    }
}
