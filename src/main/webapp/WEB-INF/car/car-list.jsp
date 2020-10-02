<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Car List</title>

    <jsp:include page="/WEB-INF/include/head.jsp"/>
</head>
<body>

<jsp:include page="../include/menu.jsp"/>

<div class="container-fluid">
    <table class="table table-hover">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>License Plate</th>
            <th>Category</th>
            <th>Capacity</th>
            <th>Driver</th>
            <th>Status</th>
        </tr>
        <c:forEach var="car" items="${cars}">
            <tr>
                <td>${car.id}</td>
                <td>${car.brand} ${car.model}</td>
                <td>${car.licensePlate}</td>
                <td>
                    <span class="badge badge-secondary">${car.category}</span>
                </td>
                <td>${car.capacity}</td>
                <td>
                    <c:if test="${car.driver != null}">
                        ${car.driver.name}
                    </c:if>
                </td>
                <td>
                    <span class="badge ${'INACTIVE'.equals(car.status.toString()) ? 'badge-danger' :
                    'ON_ORDER'.equals(car.status.toString()) ? 'badge-primary' : 'badge-success'}">${car.status}</span>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<br/>
</body>
</html>