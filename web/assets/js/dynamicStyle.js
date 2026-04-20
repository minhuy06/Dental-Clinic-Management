/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

// Đợi cho HTML load xong hết rồi mới chạy để tránh lỗi không tìm thấy phần tử
document.addEventListener("DOMContentLoaded", function() {
    
    // 1. Lấy tất cả các thẻ <a> nằm trong thanh menu
    const navLinks = document.querySelectorAll('.nav-menu li a');
    
    // 2. Lấy đường dẫn hiện tại của trình duyệt (VD: /NhaKhoa/benhnhan.jsp)
    let currentPath = window.location.pathname;

    // (Mẹo nhỏ: Nếu người dùng ở trang gốc "/", ta ngầm hiểu là trang index.jsp)
    if (currentPath.endsWith('/')) {
        currentPath += 'index.jsp';
    }

    // 3. Lặp qua từng thẻ link để xử lý
    navLinks.forEach(link => {
        // Xóa class active cũ đi cho sạch sẽ
        link.classList.remove('active');

        // Lấy giá trị href của thẻ <a> (VD: benhnhan.jsp)
        const href = link.getAttribute('href');

        // 4. Kiểm tra xem đường dẫn hiện tại có chứa cái href này không
        if (href && href !== "#" && currentPath.includes(href)) {
            // Nếu khớp -> Thêm class active vào để nó đổi màu
            link.classList.add('active');
        }
    });
});
