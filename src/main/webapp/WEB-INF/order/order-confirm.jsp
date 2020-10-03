<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="messages"/>

<html>

<head>
    <title>Order Confirmation</title>
    <jsp:include page="/WEB-INF/include/head.jsp"/>
</head>

<body>

<jsp:include page="../include/menu.jsp"/>

<br>

<div class="container">
    <div class="row">
        <div class="col-sm-3"></div>
        <div class="col-sm-6">


            <c:if test="${order.suggestedCar == null}">
                <div class="alert alert-warning alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <strong><fmt:message key="order.confirmation.message.no-car"/></strong>
                </div>
            </c:if>


            <h2>Order confirmation</h2>

            <div class="form-group">
                <label>Departure Address: </label>
                <label>${order.departureAddress}</label>
            </div>

            <div class="form-group">
                <label>Arrival Address: </label>
                <label>${order.arrivalAddress}</label>
            </div>


            <div class="form-group">
                <label>Client Phone: </label>
                <label>${order.userPhone}</label>
            </div>

            <div class="form-group">
                <label>Category: </label>
                <label>${order.category}</label>
            </div>

            <div class="form-group">
                <label>Passengers: </label>
                <label>${order.passengersCount}</label>
            </div>

            <div class="form-group">
                <label>Fare: </label>
                <label>${order.fare}</label> <label>${order.finalFare}</label>
            </div>

            <div class="form-group">
                <label>Car: </label>
                <label>${order.suggestedCar.brand }</label>
                <label> ${order.suggestedCar.model}</label>
                <label> ${order.suggestedCar.licensePlate}</label>
            </div>

            <div class="form-group" style="display: flex; justify-content: space-evenly;">

                <a href="/user/order-cancel?id=${order.orderId}" class="btn btn-danger" role="button">Cancel</a>

                <form action="/user/order-confirmation" method="post">
                    <input name="orderId" type="hidden" value="${order.orderId}">
                    <input name="carId" type="hidden" value="${order.suggestedCar.id}">
                    <button type="submit" ${order.suggestedCar == null ? 'disabled' : '' } class="btn btn-success"> Confirm</button>
                </form>

            </div>
        </div>
        <div class="col-sm-3"></div>
    </div>
</div>

</body>

</html>