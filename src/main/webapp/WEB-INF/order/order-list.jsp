<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<html>
<head>
    <title>Order List</title>

    <jsp:include page="/WEB-INF/include/head.jsp"/>
</head>
<body>

<jsp:include page="../include/menu.jsp"/>

<div class="container-fluid">
    <table class="table table-hover">
        <tr>
            <th><fmt:message key="orderlist.creationdate"/></th>
            <th><fmt:message key="orderlist.phone"/></th>
            <th><fmt:message key="orderlist.departureaddress"/></th>
            <th><fmt:message key="orderlist.arrivaladdress"/></th>
            <th><fmt:message key="orderlist.category"/></th>
            <th><fmt:message key="orderlist.license_plate"/></th>
            <th><fmt:message key="orderlist.fare"/></th>
            <th><fmt:message key="orderlist.status"/></th>
            <th><fmt:message key="orderlist.actions"/></th>
        </tr>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td>${order.creationDate}</td>
                <td>${order.userPhone}</td>
                <td>${order.departureAddress}</td>
                <td>${order.arrivalAddress}</td>
                <td>${order.category}</td>
                <td>${order.licensePlate}</td>
                <td>${order.fare}</td>
                <td>
						<span class="badge
                                    ${'NEW'.equals(order.status.toString()) ? 'badge-primary'
                                    : 'ON_ROAD'.equals(order.status.toString()) ? 'badge-warning'
                                    : 'COMPLETED'.equals(order.status.toString()) ? 'badge-success'
                                    : 'badge-danger'}">
                                ${order.status}
                        </span>
                </td>

                <td>

                    <c:if test="${'NEW'.equals(order.status.toString()) || 'ON_ROAD'.equals(order.status.toString())}">
                        <a href="/admin/order-cancel?id=${order.orderId}" class="btn btn-danger" role="button">Cancel</a>
                    </c:if>

                    <c:if test="${'ON_ROAD'.equals(order.status.toString())}">
                        <a href="/admin/order-complete?id=${order.orderId}" class="btn btn-success" role="button">Complete</a>
                    </c:if>

                </td>

            </tr>
        </c:forEach>
    </table>
</div>
<br/>
</body>
</html>