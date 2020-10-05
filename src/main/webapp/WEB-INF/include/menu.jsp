<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.lang}" scope="session"/>
<fmt:setBundle basename="messages"/>
<nav class="navbar navbar-expand-sm bg-dark">

    <div class="container-fluid">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link text-white" href="${pageContext.request.contextPath}/"><fmt:message key="navbar.home"/></a>
            </li>
            <c:if test="${sessionScope.logginedUserName != null && sessionScope.logginedUserRoles.contains('ADMIN')}">
                <li class="nav-item">
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/user-list"><fmt:message key="navbar.userlist"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/order-list"><fmt:message key="navbar.orders"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/car-list"><fmt:message key="navbar.carlist"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/driver-list"><fmt:message key="navbar.driverlist"/></a>
                </li>

            </c:if>
        </ul>

        <div>
            <c:if test="${sessionScope.logginedUserName != null && sessionScope.logginedUserRoles.contains('USER')}">
                <a class="nav-link text-white" href="${pageContext.request.contextPath}/user/order-create"><fmt:message key="navbar.book"/></a>
            </c:if>

        </div>

        <div style="display: flex; align-items: center;">
            <c:if test="${sessionScope.logginedUserName != null}">
                <a class="nav-link text-white" href="${pageContext.request.contextPath}/user/user-logout"><fmt:message key="navbar.logout"/></a>
            </c:if>
            <c:if test="${sessionScope.logginedUserName == null}">
                <a class="nav-link text-white" href="${pageContext.request.contextPath}/guest/user-login"><fmt:message key="navbar.login"/></a>
                <a class="nav-link text-white" href="${pageContext.request.contextPath}/guest/user-registration"><fmt:message key="navbar.register"/></a>
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
