<%-- 
    Document   : error.jsp
    Created on : Apr 21, 2026, 5:41:19 AM
    Author     : kinhm
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>404 - Không Tìm Thấy Trang</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/error.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="error-container">
        <div class="gear-wrapper">
            <i class="fas fa-cog gear one"></i>
            <i class="fas fa-cog gear two"></i>
            <i class="fas fa-cog gear three"></i>
            
            <div class="robot-repair">
                <i class="fas fa-robot"></i>
                <div class="spark"></div> </div>
            
            <h1 class="error-code">404</h1>
        </div>

        <div class="error-message">
            <h2>TRANG KHÔNG TÌM THẤY</h2>
            <p>Đội ngũ kỹ thuật (và robot) đang nỗ lực sửa chữa đường link này.</p>
            <a href="${pageContext.request.contextPath}/trangchu/trang-chu" class="btn-home">
                <i class="fas fa-home"></i> TRỞ VỀ TRANG CHỦ
            </a>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/error.js"></script>
</body>
</html>
