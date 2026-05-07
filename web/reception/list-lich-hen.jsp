<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kiểm tra danh sách lịch hẹn</title>
    <style>
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #007bff; color: white; }
        tr:nth-child(even) { background-color: #f2 f2 f2; }
        .status { padding: 5px 10px; border-radius: 4px; font-weight: bold; }
        .pending { background-color: #ffeeba; color: #856404; }
    </style>
</head>
<body>

    <h2>Danh sách lịch hẹn hiện có trong hệ thống</h2>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên Bệnh Nhân</th>
                <th>Ngày Khám</th>
                <th>Giờ Khám</th>
                <th>Trạng Thái</th>
                <th>Ghi Chú</th>
            </tr>
        </thead>
        <tbody>
            <%-- Duyệt danh sách từ allLichHen --%>
            <c:forEach var="lh" items="${allLichHen}">
                <tr>
                    <td>${lh.lichHenID}</td>
                    <td><strong>${lh.benhNhan.taiKhoan.hoTen}</strong></td>
                    <td>${lh.ngayKham}</td>
                    <td>${lh.gioKham}</td>
                    <td>
                        <span class="status ${lh.trangThai == 'Chờ duyệt' ? 'pending' : ''}">
                            ${lh.trangThai}
                        </span>
                    </td>
                    <td>${lh.ghiChu}</td>
                </tr>
            </c:forEach>

            <%-- Hiển thị nếu danh sách trống --%>
            <c:if test="${empty allLichHen}">
                <tr>
                    <td colspan="6" style="text-align: center; color: red;">
                        Không tìm thấy dữ liệu lịch hẹn nào! Hãy kiểm tra lại Servlet hoặc Database.
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>

</body>
<p>Kiểm tra dữ liệu: ${allLichHen != null ? "Đã có dữ liệu" : "Dữ liệu đang bị NULL"}</p>
<p>Số lượng: ${allLichHen.size()}</p>
</html>