<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>

<head>
    <title>Create order</title>
    <jsp:include page="/WEB-INF/include/head.jsp"/>
</head>

<body>

<jsp:include page="../include/menu.jsp"/>
<fmt:setBundle basename="messages"/>

<br>

<div class="container">
    <div class="row">
        <div class="col-sm-3"></div>
        <div class="col-sm-6">
            <h3><fmt:message key="order-creation.title.order-booking"/></h3>
            <form action="${pageContext.request.contextPath}/user/order-create" method="post">

                <div class="form-group">
                    <label>
                        <strong>
                            <fmt:message key="order-confirm.departureaddress"/>
                        </strong>
                    </label>
                    <br>
                    <select required name="selectedDepartureAddress">
                        <option value=""><fmt:message key="order-creation.form.option.pleasechooseaddress"/></option>
                        <c:forEach var="item" items="${availableAddresses}">
                            <option value="${item.id}" ${item.id == selectedDepartureAddress ? 'selected="selected"' : ''}>${item.street} ${item.building}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label>
                        <strong>
                            <fmt:message key="order.confirm.arrivaladdress"/>
                        </strong>
                    </label>
                    <br>
                    <select required name="selectedArrivalAddress">
                        <option value=""><fmt:message key="order-creation.form.option.pleasechooseaddress"/></option>
                        <c:forEach var="item" items="${availableAddresses}">
                            <option value="${item.id}" ${item.id == selectedArrivalAddress ? 'selected="selected"' : ''}>${item.street} ${item.building}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label>
                        <strong>
                            <fmt:message key="order-confirm.category"/>
                        </strong>
                    </label>
                    <br>
                    <select name="selectedCategory">
                        <c:forEach var="item" items="${availableCategory}">
                            <option value="${item}" ${item == selectedCategory ? "selected=\"selected\"" : ""}>${item}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label>
                        <strong>
                            <fmt:message key="order-confirm.passengers"/>
                        </strong>
                    </label>
                    <br>
                    <select name="selectedPassengersCount">
                        <c:forEach var="item" items="${availablePassengerCountSets}">
                            <option value="${item}" ${item == selectedPassengerCounts ? "selected=\"selected\"" : ""}>${item}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-success"><fmt:message
                                key="order-creation.button.book"/></button>
                    </div>
                </div>

            </form>

        </div>
        <div class="col-sm-3"></div>
    </div>
</div>

</body>

</html>