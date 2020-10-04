package ua.com.taxi.controller;

import ua.com.taxi.model.Car;
import ua.com.taxi.service.CarService;
import ua.com.taxi.service.impl.CarServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/car-list")
public class CarListServlet extends HttpServlet {

    private CarService carService = new CarServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Car> carList = carService.findAll();

        request.setAttribute("cars", carList);

        request.getRequestDispatcher("/WEB-INF/car/car-list.jsp").forward(request, response);
    }
}
