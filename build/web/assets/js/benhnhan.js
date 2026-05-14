/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


/**
 * ==================== NHA KHOA 5AE - QUẢN LÝ BỆNH NHÂN ====================
 * File: benhnhan.js
 */

// ==================== DỮ LIỆU MẪU ====================
let patients = [
    { id: 1, fullName: "Nguyễn Văn Hiển", gender: "Nam", phone: "0908123456", birthDate: "1990-05-15", address: "Hà Nội", allergy: "Penicillin", medicalHistory: "Cao huyết áp", status: "active", registerDate: "2024-01-10", notes: "" },
    { id: 2, fullName: "Trần Thị Thảo", gender: "Nữ", phone: "0918765432", birthDate: "1995-08-20", address: "Hồ Chí Minh", allergy: "Không", medicalHistory: "Hen suyễn", status: "active", registerDate: "2024-02-15", notes: "" },
    { id: 3, fullName: "Lê Anh Nam", gender: "Nam", phone: "0987654321", birthDate: "1988-12-10", address: "Đà Nẵng", allergy: "Amoxicillin", medicalHistory: "Tiểu đường type 2", status: "completed", registerDate: "2023-11-20", notes: "" },
    { id: 4, fullName: "Phạm Thu Hà", gender: "Nữ", phone: "0978123456", birthDate: "2000-03-25", address: "Hải Phòng", allergy: "Không", medicalHistory: "Không", status: "followup", registerDate: "2024-03-05", notes: "Cần tái khám sau 1 tháng" },
    { id: 5, fullName: "Hoàng Văn Tuấn", gender: "Nam", phone: "0965123456", birthDate: "1985-07-30", address: "Bình Dương", allergy: "Paracetamol", medicalHistory: "Viêm gan B", status: "active", registerDate: "2024-01-20", notes: "" },
    { id: 6, fullName: "Đặng Thị Hoa", gender: "Nữ", phone: "0932123456", birthDate: "1992-11-12", address: "Cần Thơ", allergy: "Không", medicalHistory: "Bệnh tim mạch", status: "completed", registerDate: "2023-12-10", notes: "" },
    { id: 7, fullName: "Bùi Minh Quân", gender: "Nam", phone: "0945123456", birthDate: "1998-04-18", address: "Huế", allergy: "Ibuprofen", medicalHistory: "Không", status: "followup", registerDate: "2024-02-28", notes: "" },
    { id: 8, fullName: "Ngô Thị Lan", gender: "Nữ", phone: "0912345678", birthDate: "1993-09-22", address: "Nha Trang", allergy: "Không", medicalHistory: "Suy giáp", status: "active", registerDate: "2024-03-10", notes: "" },
    { id: 9, fullName: "Vũ Minh Đức", gender: "Nam", phone: "0978123456", birthDate: "1991-07-07", address: "Quảng Ninh", allergy: "Không", medicalHistory: "Không", status: "active", registerDate: "2024-03-15", notes: "" },
    { id: 10, fullName: "Đinh Bảo Ngọc", gender: "Nữ", phone: "0988123456", birthDate: "1996-11-11", address: "Lạng Sơn", allergy: "Aspirin", medicalHistory: "Thiếu máu", status: "followup", registerDate: "2024-03-20", notes: "" }
];

// ==================== BIẾN TOÀN CỤC ====================
let currentFilter = 'all';
let currentPage = 1;
let rowsPerPage = 5;
let editingId = null;

// ==================== HÀM TIỆN ÍCH ====================
function formatDate(dateStr) {
    if (!dateStr) return '';
    let date = new Date(dateStr);
    return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
}

function getStatusBadge(status) {
    switch (status) {
        case 'active':
            return '<span class="badge badge-active"><i class="fas fa-stethoscope"></i> Đang điều trị</span>';
        case 'completed':
            return '<span class="badge badge-completed"><i class="fas fa-check-circle"></i> Hoàn thành</span>';
        case 'followup':
            return '<span class="badge badge-followup"><i class="fas fa-calendar-check"></i> Tái khám</span>';
        default:
            return '<span class="badge badge-active">Đang điều trị</span>';
    }
}

function getGenderIcon(gender) {
    if (gender === 'Nam') {
        return '<i class="fas fa-mars gender-male"></i> Nam';
    } else if (gender === 'Nữ') {
        return '<i class="fas fa-venus gender-female"></i> Nữ';
    }
    return '<i class="fas fa-genderless"></i> Khác';
}

function getAllergyDisplay(allergy) {
    if (!allergy || allergy === 'Không' || allergy === '') {
        return '<span class="no-data"><i class="fas fa-check-circle"></i> Không dị ứng</span>';
    }
    return `<span class="allergy-tag"><i class="fas fa-allergies"></i> ${escapeHtml(allergy)}</span>`;
}

function getMedicalHistoryDisplay(history) {
    if (!history || history === 'Không' || history === '') {
        return '<span class="no-data"><i class="fas fa-check-circle"></i> Không có</span>';
    }
    return `<span class="history-tag"><i class="fas fa-notes-medical"></i> ${escapeHtml(history)}</span>`;
}

function showToast(message, type = 'success') {
    let toast = document.createElement('div');
    toast.className = 'toast-message';
    toast.innerHTML = `<i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-info-circle'}"></i> ${message}`;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

function escapeHtml(text) {
    if (!text) return '';
    let div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// ==================== THỐNG KÊ ====================
function updateStats() {
    let total = patients.length;
    let active = patients.filter(p => p.status === 'active').length;
    let completed = patients.filter(p => p.status === 'completed').length;
    let followup = patients.filter(p => p.status === 'followup').length;

    document.getElementById('totalPatients').innerText = total;
    document.getElementById('activePatients').innerText = active;
    document.getElementById('completedPatients').innerText = completed;
    document.getElementById('followupPatients').innerText = followup;
}

// ==================== LỌC THEO TRẠNG THÁI ====================
function filterByStatus(status) {
    currentFilter = status;
    currentPage = 1;
    document.querySelectorAll('.stat-card').forEach(card => card.classList.remove('active'));
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
        if (btn.getAttribute('data-filter') === status) {
            btn.classList.add('active');
        }
    });
    
    if (status === 'all') document.getElementById('statAll').classList.add('active');
    else if (status === 'active') document.getElementById('statActive').classList.add('active');
    else if (status === 'completed') document.getElementById('statCompleted').classList.add('active');
    else if (status === 'followup') document.getElementById('statFollowup').classList.add('active');
    
    renderTable();
}

// ==================== HIỂN THỊ BẢNG ====================
function renderTable() {
    let searchTerm = document.getElementById('searchInput').value.toLowerCase();

    let filtered = patients.filter(patient => {
        if (currentFilter !== 'all' && patient.status !== currentFilter) return false;
        if (searchTerm && !patient.fullName.toLowerCase().includes(searchTerm) &&
            !patient.phone.includes(searchTerm) &&
            !patient.id.toString().includes(searchTerm)) return false;
        return true;
    });

    let totalPages = Math.ceil(filtered.length / rowsPerPage);
    let start = (currentPage - 1) * rowsPerPage;
    let end = start + rowsPerPage;
    let paginatedPatients = filtered.slice(start, end);

    let tbody = document.getElementById('patientTableBody');
    if (paginatedPatients.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 40px;">Không có dữ liệu bệnh nhân</td></tr>';
    } else {
        tbody.innerHTML = paginatedPatients.map(patient => `
            <tr>
                <td><span style="font-weight: 600; color: var(--primary-color);">#${patient.id}</span></td>
                <td style="font-weight: 500;">${escapeHtml(patient.fullName)}</td>
                <td>${getGenderIcon(patient.gender)}</td>
                <td>${patient.phone}</td>
                <td>${getAllergyDisplay(patient.allergy)}</td>
                <td>${getMedicalHistoryDisplay(patient.medicalHistory)}</td>
                <td>${getStatusBadge(patient.status)}</td>
                <td class="action-btns">
                    <button class="btn-action btn-edit" onclick="editPatient(${patient.id})"><i class="fas fa-edit"></i></button>
                    <button class="btn-action btn-delete" onclick="deletePatient(${patient.id})"><i class="fas fa-trash"></i></button>
                </td>
            </tr>
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
    renderTable();
}

// ==================== CRUD BỆNH NHÂN ====================
function openAddModal() {
    editingId = null;
    document.getElementById('modalTitle').innerText = 'Thêm bệnh nhân mới';
    document.getElementById('patientForm').reset();
    document.getElementById('patientId').value = '';
    document.getElementById('registerDate').value = new Date().toISOString().split('T')[0];
    document.getElementById('patientModal').style.display = 'flex';
}

function editPatient(id) {
    let patient = patients.find(p => p.id === id);
    if (patient) {
        editingId = id;
        document.getElementById('modalTitle').innerText = 'Sửa thông tin bệnh nhân';
        document.getElementById('patientId').value = patient.id;
        document.getElementById('fullName').value = patient.fullName;
        document.getElementById('gender').value = patient.gender;
        document.getElementById('phone').value = patient.phone;
        document.getElementById('birthDate').value = patient.birthDate || '';
        document.getElementById('address').value = patient.address || '';
        document.getElementById('allergy').value = patient.allergy || '';
        document.getElementById('medicalHistory').value = patient.medicalHistory || '';
        document.getElementById('status').value = patient.status;
        document.getElementById('registerDate').value = patient.registerDate || '';
        document.getElementById('notes').value = patient.notes || '';
        document.getElementById('patientModal').style.display = 'flex';
    }
}

function deletePatient(id) {
    if (confirm('Bạn có chắc chắn muốn xóa bệnh nhân này?')) {
        patients = patients.filter(p => p.id !== id);
        if (patients.length === 0) currentPage = 1;
        renderTable();
        showToast('Đã xóa bệnh nhân thành công');
    }
}

function savePatient() {
    let fullName = document.getElementById('fullName').value.trim();
    let gender = document.getElementById('gender').value;
    let phone = document.getElementById('phone').value.trim();
    let birthDate = document.getElementById('birthDate').value;
    let address = document.getElementById('address').value;
    let allergy = document.getElementById('allergy').value.trim();
    let medicalHistory = document.getElementById('medicalHistory').value.trim();
    let status = document.getElementById('status').value;
    let registerDate = document.getElementById('registerDate').value;
    let notes = document.getElementById('notes').value.trim();
    
    if (!fullName || !phone) {
        alert('Vui lòng điền đầy đủ thông tin bắt buộc!');
        return;
    }
    
    let finalAllergy = allergy === '' ? 'Không' : allergy;
    let finalMedicalHistory = medicalHistory === '' ? 'Không' : medicalHistory;
    
    if (editingId) {
        let index = patients.findIndex(p => p.id === editingId);
        if (index !== -1) {
            patients[index] = { 
                ...patients[index], 
                fullName, gender, phone, birthDate, address, 
                allergy: finalAllergy, medicalHistory: finalMedicalHistory, 
                status, registerDate, notes
            };
            showToast('Đã cập nhật thông tin bệnh nhân');
        }
    } else {
        let newId = Math.max(...patients.map(p => p.id), 0) + 1;
        patients.push({
            id: newId, fullName, gender, phone, birthDate, address, 
            allergy: finalAllergy, medicalHistory: finalMedicalHistory, 
            status, registerDate: registerDate || new Date().toISOString().split('T')[0], 
            notes
        });
        showToast('Đã thêm bệnh nhân mới');
    }
    
    closeModal();
    renderTable();
}

function closeModal() {
    document.getElementById('patientModal').style.display = 'none';
    editingId = null;
}

// ==================== KHỞI TẠO ====================
document.addEventListener('DOMContentLoaded', () => {
    renderTable();
    
    document.getElementById('openAddBtn').onclick = openAddModal;
    document.querySelector('.close').onclick = closeModal;
    document.querySelector('.btn-cancel').onclick = closeModal;
    document.querySelector('.btn-save').onclick = savePatient;
    
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.onclick = () => filterByStatus(btn.getAttribute('data-filter'));
    });
    
    document.getElementById('searchInput').onkeyup = () => {
        currentPage = 1;
        renderTable();
    };
    
    window.onclick = (e) => {
        if (e.target === document.getElementById('patientModal')) closeModal();
    };
});