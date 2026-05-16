<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Truy cập bị từ chối</title>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; padding-top: 100px; background: #f0f4f8; }
        .error-box { background: white; padding: 40px; border-radius: 10px; display: inline-block; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
        h1 { color: #e74c3c; font-size: 80px; margin: 0; }
        h2 { color: #2c3e50; }
        a { display: inline-block; margin-top: 20px; padding: 10px 20px; background: #3498db; color: white; text-decoration: none; border-radius: 5px; }
    </style>
</head>
<body>
    <div class="error-box">
        <h1>403</h1>
        <h2>Cấm Truy Cập!</h2>
        <p>Bạn không có quyền xem trang này. Vui lòng quay lại.</p>
        <a href="${pageContext.request.contextPath}/index.jsp">Về trang chủ</a>
    </div>
</body>
</html>
