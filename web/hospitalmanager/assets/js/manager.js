/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
document.addEventListener("DOMContentLoaded", function() {
    
    // 1. Xử lý Dropdown Menu
    const dropdownToggles = document.querySelectorAll('.dropdown-toggle');
    dropdownToggles.forEach(toggle => {
        toggle.addEventListener('click', function(e) {
            e.preventDefault();
            const parentLi = this.parentElement;
            parentLi.classList.toggle('open');
        });
    });

    // 2. Xử lý Load nội dung động (Single Page Application)
    const navItems = document.querySelectorAll('.nav-item');
    const mainContent = document.getElementById('main-content');

    navItems.forEach(item => {
        item.addEventListener('click', async function(e) {
            e.preventDefault();
            
            
            // Xóa class active cũ, thêm vào tab đang click
            document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));
            this.classList.add('active');

            // Lấy tên file jsp cần load từ thuộc tính data-target
            const targetPage = this.getAttribute('data-target');
            if(!targetPage) return;

            // Hiển thị hiệu ứng loading
            mainContent.innerHTML = '<div style="text-align:center; padding: 50px;"><i class="fas fa-spinner fa-spin fa-2x"></i><p>Đang tải dữ liệu...</p></div>';

            try {
                // Gọi tới các file JSP con nằm cùng thư mục
                const response = await fetch(targetPage);
                if (response.ok) {
                    const html = await response.text();
                    mainContent.innerHTML = html;
                } else {
                    window.location.href = targetPage;
                }
            } catch (error) {
                console.error("Lỗi Fetch:", error);
                mainContent.innerHTML = '<h3 style="color:red;">Lỗi kết nối máy chủ!</h3>';
            }
        });
    });
});

// --- HÀM TÌM KIẾM CHO DẠNG CARD ---
function filterData(className, inputId) {
    let filter = document.getElementById(inputId).value.toUpperCase();
    let cards = document.getElementsByClassName(className);
    for (let i = 0; i < cards.length; i++) {
        let text = cards[i].innerText;
        if (text.toUpperCase().indexOf(filter) > -1) {
            cards[i].style.display = "";
        } else {
            cards[i].style.display = "none";
        }
    }
}

// --- HÀM TÌM KIẾM CHO DẠNG TABLE ---
function filterTable(tableId, inputId) {
    let filter = document.getElementById(inputId).value.toUpperCase();
    let trs = document.getElementById(tableId).getElementsByTagName("tr");
    // Bắt đầu từ 1 để bỏ qua thẻ th (tiêu đề cột)
    for (let i = 1; i < trs.length; i++) {
        let text = trs[i].innerText;
        if (text.toUpperCase().indexOf(filter) > -1) {
            trs[i].style.display = "";
        } else {
            trs[i].style.display = "none";
        }
    }
}

// --- HÀM MỞ MODAL XEM CHI TIẾT ---
function openDetailModal(name, role, phone, status) {
    document.getElementById('modalName').innerText = name;
    document.getElementById('modalRole').innerText = role;
    document.getElementById('modalPhone').innerText = phone;
    document.getElementById('modalStatus').innerText = status;
    document.getElementById('detailModal').style.display = 'flex';
}

