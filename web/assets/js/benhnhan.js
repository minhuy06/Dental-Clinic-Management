/**
 * NHA KHOA 5AE - Quản lý bệnh nhân (dữ liệu từ API /api/patients)
 */

let patients = [];

let currentFilter = 'all';
let currentPage = 1;
let rowsPerPage = 20;
let editingId = null;

function getPatientApiBase() {
    const cp = (typeof window.APP_CONTEXT_PATH === 'string' && window.APP_CONTEXT_PATH)
        ? window.APP_CONTEXT_PATH
        : '';
    return cp + '/api';
}

async function requestApi(path, options = {}) {
    const method = options.method || 'GET';
    const body = options.body;
    const headers = options.headers || {};
    const fetchOptions = {
        method: method,
        credentials: 'same-origin',
        headers: Object.assign({ 'Content-Type': 'application/json' }, headers)
    };
    if (body !== undefined && body !== null) fetchOptions.body = JSON.stringify(body);

    const apiBase = getPatientApiBase();
    const res = await fetch(apiBase + '/' + path, fetchOptions);
    let json = null;
    try { json = await res.json(); } catch (e) { json = null; }
    if (!res.ok) throw new Error((json && json.message) || ('Lỗi HTTP: ' + res.status));
    return json;
}

function normalizeList(res) {
    if (!res) return [];
    if (Array.isArray(res)) return res;
    if (Array.isArray(res.data)) return res.data;
    return [];
}

const patientDataSource = {
    list: async () => normalizeList(await requestApi('patients')),
    update: async (payload) => requestApi('patients/update', { method: 'PUT', body: payload })
};

async function withDataGuard(action, successMsg) {
    try {
        const res = await action();
        if (res && res.success === false) {
            showToast(res.message || 'Thao tác thất bại', 'error');
            return null;
        }
        if (successMsg) showToast(successMsg);
        return res || { success: true };
    } catch (error) {
        console.error('Patient data error:', error);
        showToast(error.message || 'Lỗi kết nối dữ liệu', 'error');
        return null;
    }
}

function applyPatientList(list) {
    patients = normalizeList(list);
    renderTable();
}

function initBenhnhanBootstrap() {
    if (typeof AppBootstrap === 'undefined') return;
    var cp = AppBootstrap.getMetaContent('context-path');
    if (cp) window.APP_CONTEXT_PATH = cp;
    var seeded = AppBootstrap.readJsonScript('patientSeedJson', []);
    window.INITIAL_PATIENTS_FROM_SERVER = Array.isArray(seeded) ? seeded : [];
}

async function loadPatientsFromServer() {
    initBenhnhanBootstrap();
    const seeded = Array.isArray(window.INITIAL_PATIENTS_FROM_SERVER)
        ? window.INITIAL_PATIENTS_FROM_SERVER
        : [];
    if (seeded.length > 0) {
        applyPatientList(seeded);
        console.info('[benhnhan] Seed từ server:', seeded.length, 'bệnh nhân');
    }

    try {
        const res = await patientDataSource.list();
        const apiList = normalizeList(res);
        if (apiList.length > 0) {
            applyPatientList(apiList);
        }
        console.info('[benhnhan] API:', apiList.length, '| hiển thị:', patients.length);
    } catch (error) {
        console.error('[benhnhan] API load error:', error);
        if (patients.length === 0) {
            showToast(error.message || 'Không tải được danh sách bệnh nhân. Hãy đăng nhập lễ tân.', 'error');
            renderTable();
        }
    }
}

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

const RCV_BOOKING_PREFILL_KEY = 'rcv_booking_prefill';

/** Chuyển sang trang lịch hẹn và mở popup đặt lịch (điền sẵn họ tên + SĐT). */
function gotoBookFromPatient(id) {
    const patient = patients.find(function (p) {
        return Number(p.id) === Number(id);
    });
    if (!patient) {
        showToast('Không tìm thấy thông tin bệnh nhân', 'error');
        return;
    }
    const cp = typeof window.APP_CONTEXT_PATH === 'string' ? window.APP_CONTEXT_PATH : '';
    const patientName = String(patient.fullName || patient.hoTen || '').trim();
    const patientPhone = String(patient.phone || patient.soDienThoai || '').trim();
    if (!patientName || !patientPhone) {
        showToast('Thiếu họ tên hoặc số điện thoại của bệnh nhân', 'error');
        return;
    }
    const notesParts = [];
    if (patient.notes && String(patient.notes).trim()) {
        notesParts.push(String(patient.notes).trim());
    }
    if (patient.allergy && patient.allergy !== 'Không') {
        notesParts.push('Dị ứng: ' + patient.allergy);
    }
    if (patient.medicalHistory && patient.medicalHistory !== 'Không') {
        notesParts.push('Tiền sử BN: ' + patient.medicalHistory);
    }
    const payload = {
        patientName: patientName,
        patientPhone: patientPhone,
        benhNhanId: patient.id,
        notes: notesParts.join(' | ')
    };
    try {
        sessionStorage.setItem(RCV_BOOKING_PREFILL_KEY, JSON.stringify(payload));
    } catch (e) {
        console.error(e);
    }
    const q = new URLSearchParams();
    q.set('openBooking', '1');
    q.set('patientName', patientName);
    q.set('patientPhone', patientPhone);
    if (patient.id) q.set('benhNhanId', String(patient.id));
    showToast('Đang mở form đặt lịch cho ' + patientName, 'success');
    window.location.href = cp + '/reception-dashboard?' + q.toString();
}

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

function renderTable() {
    let searchTerm = document.getElementById('searchInput').value.toLowerCase();

    let filtered = patients.filter(patient => {
        if (currentFilter !== 'all' && patient.status !== currentFilter) return false;
        if (searchTerm && !patient.fullName.toLowerCase().includes(searchTerm) &&
            !patient.phone.includes(searchTerm) &&
            !patient.id.toString().includes(searchTerm)) return false;
        return true;
    });

    let totalPages = Math.ceil(filtered.length / rowsPerPage) || 1;
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
                    <button type="button" class="btn-action btn-add-appointment" data-book-patient-id="${patient.id}" title="Thêm lịch hẹn">
                        <i class="fas fa-calendar-plus"></i>
                    </button>
                    <button type="button" class="btn-action btn-edit" data-edit-patient-id="${patient.id}" title="Sửa"><i class="fas fa-edit"></i></button>
                </td>
            </tr>
        `).join('');
    }

    renderPagination(filtered.length > 0 ? totalPages : 0);
    updateStats();
    updateTableFooter(filtered.length, start, paginatedPatients.length);
}

function updateTableFooter(totalFiltered, startIndex, pageCount) {
    let el = document.getElementById('patientTableFooter');
    if (!el) return;
    if (totalFiltered === 0) {
        el.textContent = 'Không có bệnh nhân';
        return;
    }
    const from = startIndex + 1;
    const to = startIndex + pageCount;
    el.textContent = 'Hiển thị ' + from + '–' + to + ' / ' + totalFiltered + ' bệnh nhân (tổng DB: ' + patients.length + ')';
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

function editPatient(id) {
    const targetId = Number(id);
    let patient = patients.find(p => Number(p.id) === targetId);
    if (patient) {
        editingId = targetId;
        document.getElementById('modalTitle').innerText = 'Sửa thông tin bệnh nhân';
        document.getElementById('patientId').value = patient.id;
        document.getElementById('fullName').value = patient.fullName || patient.hoTen || '';
        document.getElementById('gender').value = patient.gender || 'Nam';
        document.getElementById('phone').value = patient.phone || patient.soDienThoai || '';
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

async function savePatient() {
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

    if (!editingId) {
        showToast('Hồ sơ bệnh nhân mới được tạo tự động khi lễ tân đặt lịch.', 'error');
        return;
    }
    if (!fullName || !phone) {
        showToast('Vui lòng điền đầy đủ thông tin bắt buộc!', 'error');
        return;
    }

    let finalAllergy = allergy === '' ? 'Không' : allergy;
    let finalMedicalHistory = medicalHistory === '' ? 'Không' : medicalHistory;

    const updatePayload = {
        id: editingId, fullName, gender, phone, birthDate, address,
        allergy: finalAllergy, medicalHistory: finalMedicalHistory,
        status, registerDate, notes
    };
    const res = await withDataGuard(() => patientDataSource.update(updatePayload), 'Đã cập nhật thông tin bệnh nhân');
    if (!res) return;

    closeModal();
    await loadPatientsFromServer();
}

function closeModal() {
    document.getElementById('patientModal').style.display = 'none';
    editingId = null;
}

window.gotoBookFromPatient = gotoBookFromPatient;

function bindPatientTableActions() {
    const tbody = document.getElementById('patientTableBody');
    if (!tbody || tbody.dataset.actionsBound === '1') return;
    tbody.dataset.actionsBound = '1';
    tbody.addEventListener('click', function (e) {
        const bookBtn = e.target.closest('[data-book-patient-id]');
        if (bookBtn) {
            gotoBookFromPatient(bookBtn.getAttribute('data-book-patient-id'));
            return;
        }
        const editBtn = e.target.closest('[data-edit-patient-id]');
        if (editBtn) {
            editPatient(Number(editBtn.getAttribute('data-edit-patient-id')));
        }
    });
}

document.addEventListener('DOMContentLoaded', () => {
    console.info('[benhnhan] JS version', window.BENHNHAN_JS_VERSION || 'unknown');
    bindPatientTableActions();
    loadPatientsFromServer();

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
