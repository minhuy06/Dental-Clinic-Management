/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

/**
 * ==================== NHA KHOA 5AE - CHĂM SÓC KHÁCH HÀNG ====================
 * File: cskh.js
 */

// ==================== DỮ LIỆU MẪU ====================
let staffList = [
    { id: 1, fullName: "Nguyễn Thị Hoa", phone: "0908123456", email: "hoa.nguyen@5ae.com", position: "Chăm sóc khách hàng", birthDate: "1990-05-15", status: "active", notes: "", avatar: "H" },
    { id: 2, fullName: "Trần Văn Nam", phone: "0918765432", email: "nam.tran@5ae.com", position: "Trưởng phòng CSKH", birthDate: "1985-08-20", status: "active", notes: "", avatar: "N" },
    { id: 3, fullName: "Lê Thị Lan", phone: "0987654321", email: "lan.le@5ae.com", position: "Tư vấn viên", birthDate: "1992-12-10", status: "busy", notes: "", avatar: "L" },
    { id: 4, fullName: "Phạm Văn Tuấn", phone: "0978123456", email: "tuan.pham@5ae.com", position: "Xử lý khiếu nại", birthDate: "1988-03-25", status: "offline", notes: "", avatar: "T" },
    { id: 5, fullName: "Hoàng Thị Mai", phone: "0965123456", email: "mai.hoang@5ae.com", position: "Chăm sóc khách hàng", birthDate: "1995-07-30", status: "active", notes: "", avatar: "M" }
];

let messages = [
    { id: 1, sender: "Nguyễn Thị Hoa", receiver: "Khách hàng", content: "Chào bạn, tôi có thể giúp gì cho bạn?", time: "2026-04-20 08:30:00", status: "read" },
    { id: 2, sender: "Khách hàng", receiver: "Nguyễn Thị Hoa", content: "Cho tôi hỏi giá niềng răng bao nhiêu ạ?", time: "2026-04-20 08:35:00", status: "read" },
    { id: 3, sender: "Trần Văn Nam", receiver: "Khách hàng", content: "Dạ chào anh/chị, bên em có gói niềng răng từ 25-35 triệu ạ", time: "2026-04-20 09:00:00", status: "read" },
    { id: 4, sender: "Khách hàng", receiver: "Trần Văn Nam", content: "Cảm ơn anh, tôi sẽ đến khám sau", time: "2026-04-20 09:05:00", status: "unread" },
    { id: 5, sender: "Lê Thị Lan", receiver: "Khách hàng", content: "Phòng khám có hỗ trợ trả góp không ạ?", time: "2026-04-19 14:00:00", status: "read" }
];

// ==================== BIẾN TOÀN CỤC ====================
let currentFilter = 'all';
let currentPage = 1;
let rowsPerPage = 6;
let editingId = null;
let currentMessageStaff = null;

// ==================== HÀM TIỆN ÍCH ====================
function formatDate(dateStr) {
    if (!dateStr) return "";
    let date = new Date(dateStr);
    let day = date.getDate().toString().padStart(2, '0');
    let month = (date.getMonth() + 1).toString().padStart(2, '0');
    let hour = date.getHours().toString().padStart(2, '0');
    let minute = date.getMinutes().toString().padStart(2, '0');
    return `${day}/${month} ${hour}:${minute}`;
}

function getStatusBadge(status) {
    switch(status) {
        case 'active':
            return '<span class="status-badge status-active"><i class="fas fa-circle"></i> Đang hoạt động</span>';
        case 'offline':
            return '<span class="status-badge status-offline"><i class="fas fa-circle"></i> Offline</span>';
        case 'busy':
            return '<span class="status-badge status-busy"><i class="fas fa-circle"></i> Đang bận</span>';
        default:
            return '<span class="status-badge status-active">Đang hoạt động</span>';
    }
}

function getPositionBadge(position) {
    let color = '';
    if (position === 'Trưởng phòng CSKH') color = '#8b5cf6';
    else if (position === 'Tư vấn viên') color = '#10b981';
    else if (position === 'Xử lý khiếu nại') color = '#f59e0b';
    else color = '#3b82f6';
    return `<span class="staff-position" style="background: ${color}20; color: ${color};">${position}</span>`;
}

function showToast(message, type = 'success') {
    let toast = document.createElement('div');
    toast.className = 'toast-message';
    toast.innerHTML = `<i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-info-circle'}"></i> ${message}`;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

function updateStats() {
    let total = staffList.length;
    let todayMessages = messages.filter(m => {
        let today = new Date().toISOString().split('T')[0];
        return m.time.startsWith(today);
    }).length;
    let todayCalls = Math.floor(Math.random() * 20) + 5;
    let avgRating = 4.8;
    
    document.getElementById('totalStaff').innerText = total;
    document.getElementById('todayMessages').innerText = todayMessages;
    document.getElementById('todayCalls').innerText = todayCalls;
    document.getElementById('avgRating').innerHTML = `${avgRating} <i class="fas fa-star" style="font-size: 0.8rem;"></i>`;
}

// ==================== HIỂN THỊ DANH SÁCH CSKH ====================
function renderStaffGrid() {
    let searchTerm = document.getElementById('searchInput').value.toLowerCase();
    
    let filtered = staffList.filter(staff => {
        if (currentFilter !== 'all' && staff.status !== currentFilter) return false;
        if (searchTerm && !staff.fullName.toLowerCase().includes(searchTerm) && 
            !staff.phone.includes(searchTerm) && 
            !staff.email.toLowerCase().includes(searchTerm)) return false;
        return true;
    });
    
    let totalPages = Math.ceil(filtered.length / rowsPerPage);
    let start = (currentPage - 1) * rowsPerPage;
    let end = start + rowsPerPage;
    let paginated = filtered.slice(start, end);
    
    let grid = document.getElementById('staffGrid');
    if (paginated.length === 0) {
        grid.innerHTML = '<div style="text-align: center; padding: 40px; grid-column: 1/-1;">📭 Không có dữ liệu nhân viên CSKH</div>';
    } else {
        grid.innerHTML = paginated.map(staff => `
            <div class="staff-card">
                <div class="staff-avatar">
                    <i class="fas fa-user-tie"></i>
                </div>
                <div class="staff-name">${escapeHtml(staff.fullName)}</div>
                <div class="staff-phone"><i class="fas fa-phone"></i> ${staff.phone}</div>
                <div class="staff-email"><i class="fas fa-envelope"></i> ${staff.email}</div>
                <div class="staff-position">${getPositionBadge(staff.position)}</div>
                <div>${getStatusBadge(staff.status)}</div>
                <div class="staff-actions">
                    <button class="btn-action btn-message" onclick="openMessageModal(${staff.id})">
                        <i class="fas fa-comment"></i> Nhắn tin
                    </button>
                    <button class="btn-action btn-edit" onclick="editStaff(${staff.id})">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn-action btn-delete" onclick="deleteStaff(${staff.id})">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
        `).join('');
    }
    
    renderPagination(totalPages);
    updateStats();
}

function renderPagination(totalPages) {
    let paginationDiv = document.getElementById('pagination');
    if (totalPages <= 1) {
        paginationDiv.innerHTML = '';
        return;
    }
    
    let html = '';
    for (let i = 1; i <= totalPages; i++) {
        html += `<button class="page-btn ${i === currentPage ? 'active' : ''}" onclick="goToPage(${i})">${i}</button>`;
    }
    paginationDiv.innerHTML = html;
}

function goToPage(page) {
    currentPage = page;
    renderStaffGrid();
}

// ==================== HIỂN THỊ TIN NHẮN ====================
function renderMessages() {
    let container = document.getElementById('messagesList');
    let recentMessages = [...messages].reverse().slice(0, 10);
    
    container.innerHTML = recentMessages.map(msg => `
        <div class="message-item">
            <div class="message-info">
                <div>
                    <span class="message-sender"><i class="fas fa-user"></i> ${msg.sender}</span>
                    <span class="message-time">${formatDate(msg.time)}</span>
                </div>
                <div class="message-content">${escapeHtml(msg.content)}</div>
            </div>
            <div class="message-status ${msg.status === 'read' ? 'read' : 'unread'}">
                ${msg.status === 'read' ? '<i class="fas fa-check-double"></i> Đã đọc' : '<i class="fas fa-clock"></i> Chưa đọc'}
            </div>
        </div>
    `).join('');
}

// ==================== CRUD CSKH ====================
function openAddModal() {
    editingId = null;
    document.getElementById('modalTitle').innerText = 'Thêm nhân viên CSKH mới';
    document.getElementById('staffForm').reset();
    document.getElementById('staffId').value = '';
    document.getElementById('status').value = 'active';
    document.getElementById('staffModal').style.display = 'flex';
}

function editStaff(id) {
    let staff = staffList.find(s => s.id === id);
    if (staff) {
        editingId = id;
        document.getElementById('modalTitle').innerText = 'Sửa thông tin CSKH';
        document.getElementById('staffId').value = staff.id;
        document.getElementById('fullName').value = staff.fullName;
        document.getElementById('phone').value = staff.phone;
        document.getElementById('email').value = staff.email;
        document.getElementById('position').value = staff.position;
        document.getElementById('birthDate').value = staff.birthDate || '';
        document.getElementById('status').value = staff.status;
        document.getElementById('notes').value = staff.notes || '';
        document.getElementById('staffModal').style.display = 'flex';
    }
}

function deleteStaff(id) {
    if (confirm('Bạn có chắc chắn muốn xóa nhân viên CSKH này?')) {
        staffList = staffList.filter(s => s.id !== id);
        if (staffList.length === 0) currentPage = 1;
        renderStaffGrid();
        showToast('Đã xóa nhân viên CSKH thành công');
    }
}

function saveStaff() {
    let fullName = document.getElementById('fullName').value.trim();
    let phone = document.getElementById('phone').value.trim();
    let email = document.getElementById('email').value.trim();
    let position = document.getElementById('position').value;
    let birthDate = document.getElementById('birthDate').value;
    let status = document.getElementById('status').value;
    let notes = document.getElementById('notes').value.trim();
    
    if (!fullName || !phone || !email) {
        alert('Vui lòng điền đầy đủ thông tin bắt buộc!');
        return;
    }
    
    if (editingId) {
        let index = staffList.findIndex(s => s.id === editingId);
        if (index !== -1) {
            staffList[index] = { ...staffList[index], fullName, phone, email, position, birthDate, status, notes };
            showToast('Đã cập nhật thông tin CSKH');
        }
    } else {
        let newId = Math.max(...staffList.map(s => s.id), 0) + 1;
        staffList.push({ id: newId, fullName, phone, email, position, birthDate, status, notes, avatar: fullName.charAt(0) });
        showToast('Đã thêm nhân viên CSKH mới');
    }
    
    closeModal();
    renderStaffGrid();
}

// ==================== GỬI TIN NHẮN ====================
function openMessageModal(staffId) {
    let staff = staffList.find(s => s.id === staffId);
    if (staff) {
        currentMessageStaff = staff;
        document.getElementById('receiverName').value = staff.fullName;
        document.getElementById('messageContent').value = '';
        document.getElementById('messageModal').style.display = 'flex';
    }
}

function sendMessage() {
    let content = document.getElementById('messageContent').value.trim();
    if (!content) {
        alert('Vui lòng nhập nội dung tin nhắn!');
        return;
    }
    
    let newMessage = {
        id: messages.length + 1,
        sender: "CSKH",
        receiver: currentMessageStaff.fullName,
        content: content,
        time: new Date().toISOString().slice(0, 19).replace('T', ' '),
        status: "unread"
    };
    
    messages.unshift(newMessage);
    renderMessages();
    closeMessageModal();
    showToast(`Đã gửi tin nhắn đến ${currentMessageStaff.fullName}`);
}

// ==================== ĐÓNG MODAL ====================
function closeModal() {
    document.getElementById('staffModal').style.display = 'none';
    editingId = null;
}

function closeMessageModal() {
    document.getElementById('messageModal').style.display = 'none';
    currentMessageStaff = null;
}

// ==================== LỌC ====================
function filterByStatus(status) {
    currentFilter = status;
    currentPage = 1;
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
        if (btn.getAttribute('data-filter') === status) {
            btn.classList.add('active');
        }
    });
    renderStaffGrid();
}

// ==================== REFRESH ====================
function refreshMessages() {
    renderMessages();
    showToast('Đã làm mới tin nhắn', 'info');
}

// ==================== ESCAPE HTML ====================
function escapeHtml(text) {
    if (!text) return '';
    let div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// ==================== KHỞI TẠO ====================
document.addEventListener('DOMContentLoaded', () => {
    renderStaffGrid();
    renderMessages();
    updateStats();
    
    // Gắn sự kiện
    document.getElementById('openAddBtn').onclick = openAddModal;
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.onclick = () => filterByStatus(btn.getAttribute('data-filter'));
    });
    document.getElementById('searchInput').onkeyup = () => {
        currentPage = 1;
        renderStaffGrid();
    };
    document.getElementById('refreshMessages').onclick = refreshMessages;
    
    // Modal events
    document.querySelector('.close').onclick = closeModal;
    document.querySelector('.btn-cancel').onclick = closeModal;
    document.querySelector('.btn-save').onclick = saveStaff;
    
    document.querySelector('.close-msg').onclick = closeMessageModal;
    document.getElementById('sendMessageBtn').onclick = sendMessage;
    document.querySelectorAll('#messageModal .btn-cancel').forEach(btn => {
        btn.onclick = closeMessageModal;
    });
    
    window.onclick = (e) => {
        if (e.target === document.getElementById('staffModal')) closeModal();
        if (e.target === document.getElementById('messageModal')) closeMessageModal();
    };
});