<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <title>User Edit</title>
    <jsp:include page="/WEB-INF/include/head.jsp"/>
</head>

<body>

<jsp:include page="../include/menu.jsp"/>

<br>

<div class="container">
    <div class="row">
        <div class="col-sm-3"></div>
        <div class="col-sm-6">
            <h2>Edit</h2>
            <form action="user-edit" method="post">
                <div class="form-group">
                    <label>ID:</label>
                    <input class="form-control" id="idID" name="id" readonly="readonly" value="${userId}">
                </div>

                <div class="form-group">
                    <label for="name">Name:</label>
                    <input class="form-control" id="name" placeholder="Please enter your name"
                           name="name"
                           value="${userName}">
                </div>

                <div class="form-group">
                    <label>Roles:</label>
                    <c:forEach var="role" items="${availableRoles}">
                        <input type="checkbox" ${selectedRoles.contains(role) ? "checked" : ""} name="selectedRoles"
                               value="${role.id}"/>${role.name}
                    </c:forEach>
                </div>


                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-success">Update</button>
                    </div>
                </div>

            </form>

        </div>
        <div class="col-sm-3"></div>
    </div>
</div>

</body>

</html>