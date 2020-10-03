<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
	<title>User List</title>

	<jsp:include page="/WEB-INF/include/head.jsp"/>
</head>
<body>

<jsp:include page="../include/menu.jsp"/>

<div class="container-fluid">
		<table class="table table-hover">
			<tr>
				<th>ID</th>
				<th>Name</th>
				<th>Phone</th>
				<th>Role</th>
				<th>Actions</th>
			</tr>
			<c:forEach var="user" items="${users}">
				<tr>
					<td>${user.id}</td>
					<td>${user.name}</td>
					<td>${user.phone}</td>
					<td>
						<c:forEach var="role" items="${user.roles}">
							<span class="badge badge-primary">${role.name}</span>
						</c:forEach>
					</td>
					<td>
						<a href="/admin/user-edit?id=${user.id}" class="btn btn-info" role="button">Edit</a>
					</td>

				</tr>
			</c:forEach>
		</table>
</div>
<br />
</body>
</html>