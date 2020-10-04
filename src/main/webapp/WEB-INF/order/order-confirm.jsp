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

            <h3><fmt:message key="order-confirm.title.order-confirmation"/></h3>

            <div class="form-group">
                <label><fmt:message key="order-confirm.departureaddress"/></label>
                <strong>
                    <label>${order.departureAddress}</label>
                </strong>
            </div>

            <div class="form-group">
                <label><fmt:message key="order.confirm.arrivaladdress"/></label>
                <strong>
                    <label>${order.arrivalAddress}</label>
                </strong>
            </div>


            <div class="form-group">
                <label><fmt:message key="order-confirm.client-phone"/></label>
                <strong>
                    <label>${order.userPhone}</label>
                </strong>
            </div>

            <div class="form-group">
                <label><fmt:message key="order-confirm.category"/></label>
                <strong>
                    <label>${order.category}</label>
                </strong>
            </div>

            <div class="form-group">
                <label><fmt:message key="order-confirm.passengers"/></label>
                <strong>
                    <label>${order.passengersCount}</label>
                </strong>
            </div>

            <div class="form-group">
                <label><fmt:message key="order-confirm.fare"/></label>
                <c:if test="${order.fare > order.finalFare}">
                    <label class="text-danger">
                        <s>
                            <fmt:formatNumber type="number" maxFractionDigits="2" value="${order.fare / 100}"/>
                        </s>
                    </label>
                    /
                </c:if>
                <label class="text-success">
                    <strong>
                        <fmt:formatNumber type="number" maxFractionDigits="2" value="${order.finalFare / 100}"/> UAH
                    </strong>
                </label>
            </div>

            <div class="form-group">
                <label><fmt:message key="order-confirm.car"/></label>
                <strong>
                    <label>${order.suggestedCar.brand }</label>
                    <label> ${order.suggestedCar.model}</label>
                    <label> ${order.suggestedCar.licensePlate}</label>
                </strong>
            </div>

            <div class="form-group" style="display: flex; justify-content: space-evenly;">

                <a href="/user/order-cancel?id=${order.orderId}" class="btn btn-danger" role="button">
                    <fmt:message key="order-confirm.button.cancel"/>
                </a>

                <form action="/user/order-confirmation" method="post">
                    <input name="orderId" type="hidden" value="${order.orderId}">
                    <input name="carId" type="hidden" value="${order.suggestedCar.id}">
                    <button type="submit" ${order.suggestedCar == null ? 'disabled' : '' } class="btn btn-success">
                        <fmt:message key="order-confirm.button.confirm"/>
                    </button>
                </form>

            </div>
        </div>
        <div class="col-sm-3"></div>
    </div>
</div>

</body>

</html>