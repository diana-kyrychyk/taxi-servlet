<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Driver List</title>

    <jsp:include page="/WEB-INF/include/head.jsp"/>
</head>
<body>

<jsp:include page="../include/menu.jsp"/>
<fmt:setBundle basename="messages"/>

<div class="container-fluid">
    <table class="table table-hover">
        <tr>
            <th>ID</th>
            <th><fmt:message key="driver-list.name"/></th>
        </tr>
        <c:forEach var="driver" items="${drivers}">
            <tr>
                <td>${driver.id}</td>
                <td>${driver.name}</td>
            </tr>
        </c:forEach>
    </table>
</div>
<br/>
</body>
</html>