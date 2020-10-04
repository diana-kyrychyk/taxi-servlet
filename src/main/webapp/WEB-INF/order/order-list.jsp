<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html>
<head>
    <title>Order List</title>

    <jsp:include page="/WEB-INF/include/head.jsp"/>
</head>

<body>

<jsp:include page="../include/menu.jsp"/>
<fmt:setBundle basename="messages"/>

<div class="container">
    <c:if test="${errorMessage != null}">
        <div class="alert alert-warning alert-dismissible">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <strong>${errorMessage}</strong>
        </div>
    </c:if>
</div>

<div class="container-fluid">


    <form action="/admin/order-list" method="get">
        <div style="display: flex; justify-content: space-between;">
            <div>
                <label><fmt:message key="orderlist.form.label.passenger"/></label>
                <select name="selectedPassenger" title="<fmt:message key="orderlist.passenger.filter.title"/>">
                    <option value=""><fmt:message key="orderlist.form.option.passenger-filter"/></option>
                    <c:forEach var="item" items="${allPassengers}">
                        <option value="${item.id}" ${item.id == searchParameters.selectedPassenger ? 'selected="selected"' : ''}>${item.phone}</option>
                    </c:forEach>
                </select>
            </div>

            <div>
                <label><fmt:message key="orderlist.label.calendar.from"/></label>
                <input type="date" name="startDate" title="<fmt:message key="orderlist.startdate.filter.title"/>"
                       value="${searchParameters.startDate != null ? searchParameters.startDate : null}">

                <label><fmt:message key="orderlist.label.calendar.to"/></label>
                <input type="date" name="endDate" title="<fmt:message key="orderlist.enddate.filter.title"/>"
                       value="${searchParameters.endDate != null ? searchParameters.endDate : null}">
            </div>

            <div>
                <label><fmt:message key="orderlist.label.sort-by"/></label>
                <select name="selectedSortType">
                    <c:forEach var="item" items="${allSortTypes}">
                        <option value="${item}" ${item == searchParameters.sortType ? 'selected="selected"' : ''}>
                            <fmt:message key="order-list.sortby.label.${item}"/></option>
                    </c:forEach>
                </select>

                <button type="submit" class="btn btn-success"><fmt:message key="orderlist.button.apply"/></button>
            </div>
        </div>

        <div>

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
                            <fmt:message key="enum.OrderStatus.${order.status}"/>
                        </span>
                        </td>

                        <td>
                            <c:if test="${'NEW'.equals(order.status.toString()) || 'ON_ROAD'.equals(order.status.toString())}">
                                <a href="/admin/order-cancel?id=${order.orderId}" class="btn btn-danger"
                                   role="button"><fmt:message key="orderlist.button.cancel"/></a>
                            </c:if>

                            <c:if test="${'ON_ROAD'.equals(order.status.toString())}">
                                <a href="/admin/order-complete?id=${order.orderId}" class="btn btn-success"
                                   role="button"><fmt:message key="orderlist.button.complete"/></a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div style="display: flex; justify-content: center;">
            <div>
                <c:if test="${searchParameters.pageNumber > 1}">
                    <button type="submit" class="btn btn-success" name="pageNumber"
                            value="${searchParameters.pageNumber - 1}"><fmt:message key="orderlist.button.prev"/>
                    </button>
                </c:if>
                <c:if test="${searchParameters.pageNumber < searchParameters.totalPages}">
                    <button type="submit" class="btn btn-success" name="pageNumber"
                            value="${searchParameters.pageNumber + 1}"><fmt:message key="orderlist.button.next"/>
                    </button>
                </c:if>
            </div>
        </div>
    </form>
</div>
<br/>
</body>
</html>