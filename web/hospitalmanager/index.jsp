<%-- 
    Document   : index
    Created on : Apr 21, 2026, 2:10:31 AM
    Author     : kinhm
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Bệnh Viện - Hospital Manager</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="assets/css/manager.css">
</head>
<body>
    <div class="wrapper">
        <nav class="sidebar">
            <div class="sidebar-header">
                <div class="user-profile nav-item" 
                     data-target="components/hoso.jsp" 
                     style="cursor: pointer; transition: all 0.3s ease; padding: 10px; border-radius: 8px;">

                    <div class="avatar">
                        <i class="fas fa-user-tie"></i>
                    </div>
                    <div class="user-details">
                        <h4>${not empty accountLogan ? accountLogan.hoTen : 'Quản Lý Cấp Cao'}</h4>
                        <span>${not empty accountLogan ? accountLogan.vaiTro : 'Hospital Manager'}</span>
                    </div>

                    <i class="fas fa-chevron-right" style="font-size: 10px; color: #ccc; margin-left: auto;"></i>
                </div>
            </div>

            <ul class="nav-links">
                <li>
                    <a href="#" class="nav-item active" data-target="dashboard.jsp">
                        <i class="fas fa-chart-pie"></i> Tổng quan
                    </a>
                </li>
                
                <li class="dropdown-menu">
                    <a href="#" class="dropdown-toggle">
                        <i class="fas fa-user-md"></i> Quản lý nhân sự 
                        <i class="fas fa-chevron-down caret"></i>
                    </a>
                    <ul class="submenu">
                        <li><a href="#" class="nav-item" data-target="${pageContext.request.contextPath}/doctorM">Danh sách Bác sĩ</a></li>
                        <li><a href="#" class="nav-item" data-target="${pageContext.request.contextPath}/hospitalmanager/receptionM">Danh sách Lễ tân</a></li>
                    </ul>
                </li>

                <li>
                    <a href="#" class="nav-item" data-target="components/benhnhan.jsp">
                        <i class="fas fa-users"></i> Thông tin bệnh nhân
                    </a>
                </li>
                <li>
                    <a href="#" class="nav-item" data-target="components/phongbenh.jsp">
                        <i class="fas fa-bed"></i> Quản lý phòng bệnh
                    </a>
                </li>
            </ul>

            <div class="sidebar-footer">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
                    <i class="fas fa-sign-out-alt"></i> Đăng xuất
                </a>
            </div>
        </nav>

        <div class="main-panel">
            <div class="topbar">
                <h3>Hệ Thống Quản Lý</h3>
                <div class="topbar-actions">
                    <i class="fas fa-bell"></i>
                </div>
            </div>

            <div id="main-content" class="content-area">
                <h2>Chào mừng trở lại! Vui lòng chọn một mục bên trái.</h2>
            </div>
        </div>
    </div>
     <div id="detailModal" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 9999; align-items: center; justify-content: center;">
    <div style="background: white; padding: 30px; border-radius: 10px; width: 400px; position: relative;">
        <span onclick="document.getElementById('detailModal').style.display='none'" style="position: absolute; top: 15px; right: 20px; font-size: 20px; cursor: pointer; color: #888;">&times;</span>
        <h3 id="modalName" style="color: var(--primary-color); margin-bottom: 5px; text-align: center;">Tên</h3>
        <p id="modalRole" style="text-align: center; color: #666; margin-bottom: 20px; padding-bottom: 15px; border-bottom: 1px solid #eee;">Vai trò</p>
        <p><strong><i class="fas fa-phone"></i> SĐT:</strong> <span id="modalPhone"></span></p>
        <p style="margin-top: 10px;"><strong><i class="fas fa-info-circle"></i> Trạng thái:</strong> <span id="modalStatus"></span></p>
    </div>
</div>
    <script src="assets/js/manager.js"></script>
</body>
</html>
