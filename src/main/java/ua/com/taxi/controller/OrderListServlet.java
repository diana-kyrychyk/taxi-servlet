package ua.com.taxi.controller;

import ua.com.taxi.filter.LocaleFilter;
import ua.com.taxi.model.User;
import ua.com.taxi.model.dto.SearchParameters;
import ua.com.taxi.model.dto.OrderListDto;
import ua.com.taxi.service.OrderService;
import ua.com.taxi.service.UserService;
import ua.com.taxi.service.impl.OrderServiceImpl;
import ua.com.taxi.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet("/admin/order-list")
public class OrderListServlet extends HttpServlet {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private OrderService orderService = new OrderServiceImpl();
    private UserService userService = new UserServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SearchParameters searchParameters = extractSearchParameters(request);
        validatesDates(searchParameters, request);
        fillSearchParameters(request, searchParameters);
        List<OrderListDto> orderList = orderService.findAllListDto(searchParameters);

        request.setAttribute("orders", orderList);
        request.getRequestDispatcher("/WEB-INF/order/order-list.jsp").forward(request, response);
    }


    private SearchParameters extractSearchParameters(HttpServletRequest request) {
        SearchParameters searchParameters = new SearchParameters();

        String selectedPassengerStr = request.getParameter("selectedPassenger");
        Integer selectedPassenger = selectedPassengerStr != null && !selectedPassengerStr.isEmpty() ? Integer.valueOf(selectedPassengerStr) : null;
        searchParameters.setSelectedPassenger(selectedPassenger);

        String sortType = request.getParameter("selectedSortType");
        sortType = sortType != null ? sortType : SearchParameters.DEFAULT_SORT_TYPE;
        searchParameters.setSortType(sortType);

        String pageNumberStr = request.getParameter("pageNumber");
        int page = pageNumberStr != null ? Integer.valueOf(pageNumberStr) : 1;
        searchParameters.setPageNumber(page);

        String startDateStr = request.getParameter("startDate");
        LocalDate startDate = startDateStr != null && !startDateStr.isEmpty() ? LocalDate.parse(startDateStr, DATE_TIME_FORMATTER) : null;
        searchParameters.setStartDate(startDate);

        String endDateStr = request.getParameter("endDate");
        LocalDate endDate = endDateStr != null && !endDateStr.isEmpty() ? LocalDate.parse(endDateStr, DATE_TIME_FORMATTER) : null;
        searchParameters.setEndDate(endDate);

        return searchParameters;
    }

    private void fillSearchParameters(HttpServletRequest request, SearchParameters searchParameters) {
        fillTotalPages(searchParameters);

        List<User> passengers = userService.findAllUsers();
        List<String> sortTypes = Arrays.asList(SearchParameters.SORT_BY_DATE, SearchParameters.SORT_BY_FARE);

        request.setAttribute("allPassengers", passengers);
        request.setAttribute("allSortTypes", sortTypes);
        request.setAttribute("searchParameters", searchParameters);
    }

    private void fillTotalPages(SearchParameters searchParameters) {
        int orderCount = orderService.findCount(searchParameters);
        int totalPages = orderCount / searchParameters.getPageSize();
        if (orderCount % searchParameters.getPageSize() != 0) {
            totalPages++;
        }
        searchParameters.setTotalPages(totalPages);
    }

    private void validatesDates(SearchParameters searchParameters, HttpServletRequest request) {
        if (!isValidDates(searchParameters.getStartDate(), searchParameters.getEndDate())) {
            searchParameters.setStartDate(null);
            searchParameters.setEndDate(null);

            Locale locale = extractLocale(request);
            ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
            String message = resourceBundle.getString("validation.datefilter.wrongorder.message");
            request.setAttribute("errorMessage", message);
        }
    }

    private boolean isValidDates(LocalDate start, LocalDate end) {
        boolean result = true;
        if(start != null && end != null && end.isBefore(start)){
            result = false;
        }
        return result;
    }

    private Locale extractLocale(HttpServletRequest req) {
        String lang = (String) req.getSession().getAttribute(LocaleFilter.LANGUAGE_PARAM);
        return Locale.forLanguageTag(lang);
    }
}
