package ua.com.taxi.controller;

import ua.com.taxi.util.factory.ApplicationFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainServlet extends HttpServlet {

    public static final String REDIRECT_PREFIX = "redirect:";

    private Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ApplicationFactory factory = ApplicationFactory.getInstance();
        controllers = factory.getControllers();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String method = req.getMethod();
        String controllerKey = method.concat(requestURI);

        Controller controller = controllers.getOrDefault(controllerKey, (request, response) -> "/error-404.jsp");
        String page = controller.processRequest(req, resp);


        if(page.startsWith(REDIRECT_PREFIX)){
            String redirectPath = page.replace(REDIRECT_PREFIX, "");
            resp.sendRedirect(redirectPath);
        } else {
            req.getRequestDispatcher(page).forward(req, resp);
        }
    }
}
