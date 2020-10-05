<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error403 occurs</title>
    <jsp:include page="/WEB-INF/include/head.jsp"/>
</head>
<body class="bg-dark">
<jsp:include page="/WEB-INF/include/menu.jsp"/>

<div class="container-fluid bg-dark">
    <div class="row">
        <div class="col-sm-2"></div>
        <div class="col-sm-8" style="text-align: center">
            <br/>
            <br/>
            <h2 style="color: white">The resource is forbidden</h2>
            <div>
                <br/>
                <br/>
                <br/>
                <br/>
                <img src="${pageContext.request.contextPath}/static/images/error404.jpg"/>
            </div>
        </div>
        <div class="col-sm-2"></div>
    </div>

</div>


</body>
</html>