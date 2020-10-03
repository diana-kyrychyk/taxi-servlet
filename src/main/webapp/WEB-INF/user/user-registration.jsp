<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>

<head>
    <title>Registration</title>
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
            <h2 align="center"><fmt:message key="userregistration.registration"/></h2>
            <form action="/guest/user-registration" method="post">

                <div class="form-group">
                    <label for="name"><fmt:message key="userregistration.name"/></label>
                    <input class="form-control" id="name" placeholder="<fmt:message key="userregistration.placeholder.name"/>" name="name" value="${name}">

                    <c:if test="${fieldsErrors != null && fieldsErrors.containsKey('name')}">
                        <label style="color: red;">${fieldsErrors.get('name')}</label>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="phone-number"><fmt:message key="userregistration.phone"/></label>
                    <input class="form-control" id="phone-number" placeholder="<fmt:message key="userlogin.placeholder.phone"/>"
                           name="phone" value="${phone}">

                    <c:if test="${fieldsErrors != null && fieldsErrors.containsKey('phone')}">
                        <label style="color: red;">${fieldsErrors.get('phone')}</label>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="password"><fmt:message key="userregistration.password"/></label>
                    <input class="form-control" id="password" placeholder="<fmt:message key="userlogin.placeholder.password"/>"
                           name="password" type="password">
                    <c:if test="${fieldsErrors != null && fieldsErrors.containsKey('password')}">
                        <label style="color: red;">${fieldsErrors.get('password')}</label>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="password-confirm"><fmt:message key="userregistration.password-repeat"/></label>
                    <input class="form-control" id="password-confirm" placeholder="<fmt:message key="userregistration.placeholder.password-confirm"/>"
                           name="password-confirm" type="password">
                    <c:if test="${fieldsErrors != null && fieldsErrors.containsKey('password-confirm')}">
                        <label style="color: red;">${fieldsErrors.get('password-confirm')}</label>
                    </c:if>
                </div>


                <div class="form-group" align="center">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-success"><fmt:message key="userregistration.button.create"/></button>
                    </div>
                </div>

            </form>

        </div>
        <div class="col-sm-3"></div>
    </div>
</div>

</body>

</html>