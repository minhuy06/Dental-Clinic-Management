<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Truy cập bị từ chối</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/403.css">
</head>
<body>
    <div class="error-box">
        <h1>403</h1>
        <h2>Cấm Truy Cập!</h2>
        <p>Bạn không có quyền xem trang này. Vui lòng quay lại.</p>
        <a href="${pageContext.request.contextPath}/">Về trang chủ</a>
    </div>
</body>
</html>
