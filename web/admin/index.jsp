<%-- 
    Document   : index
    Created on : Apr 25, 2026, 2:17:05 AM
    Author     : kinhm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello ${loggedInUser.hoTen} ${loggedInUser.vaiTro}</h1>
        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
    </body>
</html>
