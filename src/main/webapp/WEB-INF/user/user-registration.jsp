<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <title>Registration</title>
    <jsp:include page="/WEB-INF/include/head.jsp"/>
</head>

<body>

<jsp:include page="../include/menu.jsp"/>

<br>

<div class="container">
    <div class="row">
        <div class="col-sm-3"></div>
        <div class="col-sm-6">
            <h2>Registration</h2>
            <form action="user-registration" method="post">

                <div class="form-group">
                    <label for="name">Name:</label>
                    <input class="form-control" id="name" placeholder="Please enter your name" name="name">
                </div>

                <div class="form-group">
                    <label for="phone-number">Phone number:</label>
                    <input class="form-control" id="phone-number" placeholder="Please enter your new phone number"
                           name="phone">
                </div>

                <div class="form-group">
                    <label for="password">Password:</label>
                    <input class="form-control" id="password" placeholder="Please enter your new password"
                           name="password" type="password">
                </div>


                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-success">Create</button>
                    </div>
                </div>

            </form>

        </div>
        <div class="col-sm-3"></div>
    </div>
</div>

</body>

</html>