package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.model.Car;
import ua.com.taxi.service.CarService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class CarListController implements Controller {

    private CarService carService;

    public CarListController(CarService carService) {
        this.carService = carService;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        List<Car> carList = carService.findAll();
        request.setAttribute("cars", carList);
        return "/WEB-INF/car/car-list.jsp";
    }
}
