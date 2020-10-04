package ua.com.taxi.util.factory;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.controller.impl.AdminOrderCancelController;
import ua.com.taxi.controller.impl.CarListController;
import ua.com.taxi.controller.impl.DriverListController;
import ua.com.taxi.controller.impl.IndexController;
import ua.com.taxi.controller.impl.LoginGetController;
import ua.com.taxi.controller.impl.LoginPostController;
import ua.com.taxi.controller.impl.LogoutController;
import ua.com.taxi.controller.impl.OrderCompleteController;
import ua.com.taxi.controller.impl.OrderConfirmationGetController;
import ua.com.taxi.controller.impl.OrderConfirmationPostController;
import ua.com.taxi.controller.impl.OrderCreateGetController;
import ua.com.taxi.controller.impl.OrderCreatePostController;
import ua.com.taxi.controller.impl.OrderListController;
import ua.com.taxi.controller.impl.UserEditGetController;
import ua.com.taxi.controller.impl.UserEditPostController;
import ua.com.taxi.controller.impl.UserListController;
import ua.com.taxi.controller.impl.UserOrderCancelController;
import ua.com.taxi.controller.impl.UserRegistrationGetController;
import ua.com.taxi.controller.impl.UserRegistrationPostController;
import ua.com.taxi.controller.validation.UserValidator;
import ua.com.taxi.dao.AddressDao;
import ua.com.taxi.dao.CarDao;
import ua.com.taxi.dao.DriverDao;
import ua.com.taxi.dao.OrderDao;
import ua.com.taxi.dao.RoleDao;
import ua.com.taxi.dao.UserDao;
import ua.com.taxi.dao.impl.AddressDaoImpl;
import ua.com.taxi.dao.impl.CarDaoImpl;
import ua.com.taxi.dao.impl.DriverDaoImpl;
import ua.com.taxi.dao.impl.OrderDaoImpl;
import ua.com.taxi.dao.impl.RoleDaoImpl;
import ua.com.taxi.dao.impl.UserDaoImpl;
import ua.com.taxi.service.AddressService;
import ua.com.taxi.service.CarService;
import ua.com.taxi.service.DriverService;
import ua.com.taxi.service.OrderService;
import ua.com.taxi.service.RoleService;
import ua.com.taxi.service.UserService;
import ua.com.taxi.service.impl.AddressServiceImpl;
import ua.com.taxi.service.impl.CarServiceImpl;
import ua.com.taxi.service.impl.DriverServiceImpl;
import ua.com.taxi.service.impl.OrderServiceImpl;
import ua.com.taxi.service.impl.RoleServiceImpl;
import ua.com.taxi.service.impl.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class ApplicationFactory {

    private static ApplicationFactory instance;

    private AddressDao addressDao = new AddressDaoImpl();
    private CarDao carDao = new CarDaoImpl();
    private DriverDao driverDao = new DriverDaoImpl();
    private OrderDao orderDao = new OrderDaoImpl();
    private RoleDao roleDao = new RoleDaoImpl();
    private UserDao userDao = new UserDaoImpl();

    private AddressService addressService = new AddressServiceImpl(addressDao);
    private CarService carService = new CarServiceImpl(carDao);
    private DriverService driverService = new DriverServiceImpl(driverDao);
    private OrderService orderService = new OrderServiceImpl(orderDao, userDao, addressDao, carDao);
    private RoleService roleService = new RoleServiceImpl(roleDao);
    private UserService userService = new UserServiceImpl(userDao, roleDao);

    private UserValidator userValidator = new UserValidator(userService);

    private Map<String, Controller> controllers = new HashMap();

    private ApplicationFactory() {
        initControllers();
    }

    public static ApplicationFactory getInstance() {
        if (instance == null) {
            synchronized (ApplicationFactory.class) {
                if (instance == null) {
                    instance = new ApplicationFactory();
                }
            }
        }
        return instance;
    }

    public Map<String, Controller> getControllers() {
        return controllers;
    }


    private void initControllers() {
        controllers.put("GET/admin/order-cancel", new AdminOrderCancelController(orderService));
        controllers.put("GET/admin/car-list", new CarListController(carService));
        controllers.put("GET/admin/driver-list", new DriverListController(driverService));
        controllers.put("GET/", new IndexController());
        controllers.put("GET/guest/user-login", new LoginGetController());
        controllers.put("POST/guest/user-login", new LoginPostController(userService));
        controllers.put("GET/user/user-logout", new LogoutController());
        controllers.put("GET/admin/order-complete", new OrderCompleteController(orderService));
        controllers.put("GET/user/order-confirmation", new OrderConfirmationGetController(orderService));
        controllers.put("POST/user/order-confirmation", new OrderConfirmationPostController(orderService));
        controllers.put("GET/user/order-create", new OrderCreateGetController(orderService, addressService));
        controllers.put("POST/user/order-create", new OrderCreatePostController(orderService));
        controllers.put("GET/admin/order-list", new OrderListController(orderService, userService));
        controllers.put("GET/admin/user-edit", new UserEditGetController(userService, roleService));
        controllers.put("POST/admin/user-edit", new UserEditPostController(userService));
        controllers.put("GET/admin/user-list", new UserListController(userService));
        controllers.put("GET/user/order-cancel", new UserOrderCancelController(orderService));
        controllers.put("GET/guest/user-registration", new UserRegistrationGetController());
        controllers.put("POST/guest/user-registration", new UserRegistrationPostController(userService, userValidator));
    }
}
