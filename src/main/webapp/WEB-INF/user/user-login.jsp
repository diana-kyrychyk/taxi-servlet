<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>

<head>
    <title>Login Page</title>

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
            <h2 align="center"><fmt:message key="userlogin.form.title"/></h2>
            <form action="${pageContext.request.contextPath}/guest/user-login" method="post">

                <div class="form-group">
                    <label for="phone"><fmt:message key="userlogin.phone"/></label>
                    <input class="form-control" id="phone" placeholder="<fmt:message key="userlogin.placeholder.phone"/>" name="phone"
                           value="">
                </div>

                <div class="form-group">
                    <label for="password"><fmt:message key="userlogin.password"/></label>
                    <input class="form-control" id="password" placeholder="<fmt:message key="userlogin.placeholder.password"/>" name="password"
                           value="" type="password">
                </div>

                <div class="form-group" align="center">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-success"><fmt:message
                                key="userlogin.button.login.label"/></button>

                    </div>
                </div>
            </form>
        </div>
        <div class="col-sm-3"></div>
    </div>
</div>

</body>
</html>
