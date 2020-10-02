<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<html>
<head>
    <title>Welcome Page</title>
    <jsp:include page="/WEB-INF/include/head.jsp"/>
</head>

<body>

<jsp:include page="/WEB-INF/include/menu.jsp"/>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3"></div>
        <div class="col-sm-6">
            <h3><fmt:message key="welcome.moto"/></h3>
            <div>
                <fmt:message key="welcomepage.task"/>
            </div>
        </div>
        <div class="col-sm-3"></div>
    </div>

</div>

</body>
</html>
