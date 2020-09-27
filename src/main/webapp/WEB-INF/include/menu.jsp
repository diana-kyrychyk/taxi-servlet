<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<nav class="navbar navbar-expand-sm bg-dark">

    <div class="container-fluid">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link text-white" href="../../index.jsp">Home Page</a>
            </li>
            <c:if test="${sessionScope.logginedUserName != null && sessionScope.logginedUserRoles.contains('ADMIN')}">
                <li class="nav-item">
                    <a class="nav-link text-white" href="/user-list">User List</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white" href="/order-list">Orders</a>
                </li>

            </c:if>
        </ul>

        <div>
            <c:if test="${sessionScope.logginedUserName != null && sessionScope.logginedUserRoles.contains('USER')}">
                <a class="nav-link text-white" href="/order-create">BOOK now</a>
            </c:if>

        </div>

        <div style="display: flex; align-items: center;">
            <c:if test="${sessionScope.logginedUserName != null}">
                <a class="nav-link text-white" href="/user-logout">Logout</a>
            </c:if>
            <c:if test="${sessionScope.logginedUserName == null}">
                <a class="nav-link text-white" href="/user-login">Login</a>
                <a class="nav-link text-white" href="/user-registration">Registrate</a>
            </c:if>

            <div style="display: flex; flex-direction: column">
                <c:if test="${pageContext.request.servletPath == '/index.jsp'
                           || pageContext.request.servletPath == '/WEB-INF/user/user-login.jsp'
                           || pageContext.request.servletPath == '/WEB-INF/user/user-registration.jsp'}">
                    <a href="?lang=en"><fmt:message key="label.lang.en"/></a>
                    <a href="?lang=uk"><fmt:message key="label.lang.uk"/></a>
                </c:if>

            </div>
        </div>
    </div>


</nav>
