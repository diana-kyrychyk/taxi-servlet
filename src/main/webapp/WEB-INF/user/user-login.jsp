<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <title>Login Page</title>

    <jsp:include page="/WEB-INF/include/head.jsp"/>
</head>

<body>

<jsp:include page="../include/menu.jsp"/>

<br>

<div class="container">
    <div class="row">
        <div class="col-sm-3"></div>
        <div class="col-sm-6">
            <h2>form</h2>
            <form action="/user-login" method="post">

                <div class="form-group">
                    <label for="phone">Phone:</label>
                    <input class="form-control" id="phone" placeholder="Please enter your phone" name="phone"
                           value="">
                </div>

                <div class="form-group">
                    <label for="password">Password:</label>
                    <input class="form-control" id="password" placeholder="Please enter your password" name="password"
                           value="" type="password">
                </div>

                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-success">Login</button>

                    </div>
                </div>

            </form>

        </div>
        <div class="col-sm-3"></div>
    </div>
</div>

</body>
</html>