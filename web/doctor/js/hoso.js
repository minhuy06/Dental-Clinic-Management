// ======================= DANH SÁCH DỊCH VỤ =======================
const DENTAL_SERVICES = [
    { id: 1, name: "Thăm khám & tư vấn", price: 100000, perUnit: false },
    { id: 2, name: "Lấy cao răng", price: 200000, perUnit: false },
    { id: 3, name: "Hàn răng thẩm mỹ (Composit)", price: 500000, perUnit: true },
    { id: 4, name: "Điều trị tủy", price: 1000000, perUnit: true },
    { id: 5, name: "Nhổ răng thường", price: 300000, perUnit: true },
    { id: 6, name: "Nhổ răng khôn phức tạp", price: 1500000, perUnit: true },
    { id: 7, name: "Bọc răng sứ kim loại", price: 2500000, perUnit: true },
    { id: 8, name: "Bọc răng toàn sứ", price: 4500000, perUnit: true },
    { id: 9, name: "Tẩy trắng răng tại phòng", price: 1800000, perUnit: false },
    { id: 10, name: "Chỉnh nha (niềng răng mắc cài)", price: 25000000, perUnit: false },
    { id: 11, name: "Chỉnh nha (niềng răng trong suốt)", price: 45000000, perUnit: false },
    { id: 12, name: "Cấy ghép Implant", price: 18000000, perUnit: true }
];

// ======================= CỜ HIỆU MOCK/REAL + DATA SOURCE =======================
const DOCTOR_HOSO_CONFIG = {
    USE_MOCK: false,
    API_BASE: '/Dental_Clinic_Management/api/doctor',
    MOCK_DELAY_MS: 120
};

function hosoDelay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function hosoRequest(path, options = {}) {
    const method = options.method || 'POST';
    const body = options.body;
    const headers = options.headers || {};
    const fetchOptions = {
        method: method,
        headers: Object.assign({ 'Content-Type': 'application/json' }, headers)
    };
    if (body !== undefined && body !== null) fetchOptions.body = JSON.stringify(body);

    const res = await fetch(DOCTOR_HOSO_CONFIG.API_BASE + path, fetchOptions);
    let json = null;
    try { json = await res.json(); } catch (e) { json = null; }
    if (!res.ok) throw new Error((json && json.message) || ('Lỗi HTTP: ' + res.status));
    return json;
}

const hosoMockSource = {
    saveExam: async (payload) => {
        await hosoDelay(DOCTOR_HOSO_CONFIG.MOCK_DELAY_MS);
        console.log('[doctor/hoso] mock save payload:', payload);
        return { success: true };
    }
};

const hosoApiSource = {
    saveExam: async (payload) => hosoRequest('/examinations/save', { method: 'POST', body: payload })
};

const hosoDataSource = DOCTOR_HOSO_CONFIG.USE_MOCK ? hosoMockSource : hosoApiSource;

function getQueryParam(name) {
    const params = new URLSearchParams(window.location.search);
    return params.get(name) || '';
}

// Format tiền Việt
function formatVND(amount) {
    return amount.toLocaleString('vi-VN') + 'đ';
}

// Xóa dòng (giữ ít nhất 1 dòng)
function attachDeleteEvents() {
    document.querySelectorAll('.delete-row').forEach(btn => {
        btn.removeEventListener('click', deleteHandler);
        btn.addEventListener('click', deleteHandler);
    });
}
function deleteHandler(e) {
    const row = e.currentTarget.closest('tr');
    const tbody = document.getElementById('treatmentBody');
    if (tbody.children.length > 1) {
        row.remove();
        showToast('Đã xóa dịch vụ', 1500);
        if (window.currentActiveRow === row) window.currentActiveRow = null;
        // Nếu không còn dòng active, chọn dòng đầu tiên
        if (!window.currentActiveRow && tbody.children.length > 0) {
            const firstRow = tbody.children[0];
            const firstToothInput = firstRow.querySelector('.tooth-number-input');
            if (firstToothInput) firstToothInput.focus();
        }
    } else {
        showToast('Phải có ít nhất 1 dịch vụ điều trị', 2000);
    }
}

// Toast thông báo
function showToast(message, duration = 3000) {
    let toast = document.getElementById('toastMessage');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'toastMessage';
        toast.className = 'toast-message';
        document.body.appendChild(toast);
    }
    toast.innerHTML = `<i class="fa-solid fa-circle-info"></i> ${message}`;
    toast.style.display = 'block';
    setTimeout(() => {
        toast.style.display = 'none';
    }, duration);
}

// ========== XỬ LÝ SƠ ĐỒ RĂNG ==========
let currentSelectedTooth = null;


function toothClickHandler() {
    // Cập nhật giao diện chọn răng
    if (currentSelectedTooth) currentSelectedTooth.classList.remove('selected');
    this.classList.add('selected');
    currentSelectedTooth = this;
    
    const toothNumber = this.getAttribute('data-tooth');
    if (!toothNumber) return;
    
    // Xác định dòng cần gán: ưu tiên dòng đang focus, nếu không có thì dùng dòng đầu
    let targetRow = window.currentActiveRow;
    if (!targetRow || !document.body.contains(targetRow)) {
        targetRow = document.querySelector('#treatmentBody tr');
    }
    if (targetRow) {
        const toothInput = targetRow.querySelector('.tooth-number-input');
        if (toothInput) {
            toothInput.value = toothNumber;
            showToast(`Đã gán răng số ${toothNumber} vào dòng đang chọn`, 1000);
        }
    } else {
        showToast('Không có dòng dịch vụ nào để gán', 2000);
    }
}

// ========== LƯU DỮ LIỆU ==========

// ========== IN ĐƠN THUỐC ==========
function printPrescription() {
    const prescription = document.getElementById('prescription')?.value || '';
    const patientName = document.getElementById('patientNameUI')?.innerText?.trim()
        || (window.DOCTOR_HOSO_BOOTSTRAP && window.DOCTOR_HOSO_BOOTSTRAP.patientName)
        || '';
    const doctorName = (window.DOCTOR_HOSO_BOOTSTRAP && window.DOCTOR_HOSO_BOOTSTRAP.doctorName)
        || (document.querySelector('.doctor-info')?.textContent || '').replace(/^[\s\S]*?-\s*/, '').trim()
        || 'Bác sĩ';
    const printWindow = window.open('', '_blank');
    printWindow.document.write(`
        <html>
        <head><title>Đơn thuốc - ${patientName}</title>
        <style>
            body { font-family: 'Inter', Arial, sans-serif; padding: 40px; }
            h2 { color: #6366f1; }
            .info { margin: 20px 0; padding: 10px; background: #f1f5f9; border-radius: 8px; }
            .prescription { white-space: pre-line; margin: 20px 0; }
            .footer { margin-top: 40px; text-align: right; }
        </style>
        </head>
        <body>
            <h2>ĐƠN THUỐC ĐIỀU TRỊ</h2>
            <div class="info">
                <strong>Bệnh nhân:</strong> ${patientName}<br>
                <strong>Bác sĩ:</strong> ${doctorName}<br>
                <strong>Ngày kê:</strong> ${new Date().toLocaleString('vi-VN')}
            </div>
            <hr>
            <div class="prescription">
                <strong>Đơn thuốc & dặn dò:</strong><br>
                ${prescription.replace(/\n/g, '<br>')}
            </div>
            <div class="footer">
                <p>Chữ ký bác sĩ</p>
                <p>${doctorName}</p>
            </div>
        </body>
        </html>
    `);
    printWindow.document.close();
    printWindow.print();
    showToast('🖨️ Đã gửi đến máy in', 1500);
}

// ========== XỬ LÝ NÚT QUAY LẠI / HỦY ==========
function handleBack() {
    if (confirm('Quay lại danh sách lịch hẹn? Dữ liệu chưa lưu sẽ bị mất.')) {
        window.location.href = 'index.jsp';
    }
}

// ========== KHỞI TẠO TRANG ==========
function init() {
    console.info('[doctor/hoso] mode:', DOCTOR_HOSO_CONFIG.USE_MOCK ? 'MOCK' : 'REAL API');
    // Gắn sự kiện cho các dòng hiện có
    const existingRows = document.querySelectorAll('#treatmentBody tr');
    existingRows.forEach(row => {
        attachRowEvents(row);
        updateRowPrices(row);
    });
    attachDeleteEvents();
    
    // 1. Đổ dữ liệu vào ô Dropdown (serviceSelect)
    const serviceSelect = document.getElementById('serviceSelect');
    if (serviceSelect) {
        // Xóa các option cũ trừ option đầu tiên
        serviceSelect.innerHTML = '<option value="">-- Chọn dịch vụ để thêm --</option>';
        danhSachDichVuTuDB.forEach(svc => {
            const opt = document.createElement('option');
            opt.value = svc.id;
            opt.textContent = `${svc.name} (${svc.price.toLocaleString('vi-VN')}đ)`;
            serviceSelect.appendChild(opt);
        });
    }

    // 2. Gắn sự kiện cho nút thêm dịch vụ
    const addBtn = document.getElementById('addServiceBtn');
    if (addBtn) {
        addBtn.addEventListener('click', () => {
            if (serviceSelect && serviceSelect.value) {
                // Thêm dịch vụ bác sĩ vừa chọn vào bảng
                addClinicalService(serviceSelect.value);
                // Reset select về mặc định sau khi thêm (tùy chọn)
                // serviceSelect.value = "";
            } else {
                showToast('⚠️ Vui lòng chọn một dịch vụ từ danh sách!', 2500);
            }
        });
    }
        
    const printBtn = document.getElementById('printBtn');
    if (printBtn) printBtn.addEventListener('click', printPrescription);
    
    const cancelBtn = document.getElementById('cancelBtn');
    const backBtn = document.getElementById('backBtn');
    if (cancelBtn) cancelBtn.addEventListener('click', handleBack);
    if (backBtn) backBtn.addEventListener('click', handleBack);
    
    const xrayBtn = document.getElementById('xrayBtn');
    if (xrayBtn) xrayBtn.addEventListener('click', () => showToast('📷 Chức năng X-Quang đang phát triển', 2000));
    
    // Khởi tạo sơ đồ răng
    // initTeethMap();
    
    // Đặt dòng active mặc định là dòng đầu tiên
    const firstRow = document.querySelector('#treatmentBody tr');
    if (firstRow) {
        window.currentActiveRow = firstRow;
        const firstToothInput = firstRow.querySelector('.tooth-number-input');
        if (firstToothInput) firstToothInput.focus();
    }
}

// =========================================================================
// === LOGIC DÀNH RIÊNG CHO MÀN HÌNH KHÁM LÂM SÀNG (LƯU DATABASE THỰC TẾ) ===
// =========================================================================

// =========================================================================
// === LOGIC DÀNH RIÊNG CHO MÀN HÌNH KHÁM LÂM SÀNG (LƯU DATABASE THỰC TẾ) ===
// =========================================================================

let danhSachDichVuTuDB = DENTAL_SERVICES; // Tạm thời dùng list ảo, sau này gọi API gán lại
let clinicalSelectedServices = [];  
let currentSelectedTeeth = [];      

// Khi trang vừa load xong, tiến hành gắn sự kiện an toàn (tránh lỗi null)
document.addEventListener('DOMContentLoaded', function() {
    
    // 1. Gắn sự kiện click cho các răng trên sơ đồ Odontogram
    const teethElements = document.querySelectorAll('.tooth');
    if(teethElements.length > 0) {
        teethElements.forEach(el => {
            el.addEventListener('click', function() {
                // Không cho click vào răng đã có class 'issue' (nếu muốn)
                if(!this.classList.contains('issue')) {
                    const tNum = parseInt(this.getAttribute('data-tooth'));
                    toggleToothSelection(tNum);
                }
            });
        });
    }

    // 2. Gắn sự kiện cho nút "Hoàn tất & Lưu"
    const saveBtn = document.getElementById('saveBtn');
    if(saveBtn) {
        saveBtn.addEventListener('click', function() {
            // Tạm thời truyền cứng ID lịch hẹn (1), ID bác sĩ (1), ID phòng (1) để test. 
            // Sau này bạn lấy từ URL parameters hoặc Session
            luuPhieuKhamLamSang(1, 1, 1); 
        });
    }

    // 3. (SỬA LỖI ĐƠ MÀN HÌNH): Bọc các sự kiện cũ lại để kiểm tra null
    var editDateEl = document.getElementById('editDate');
    if (editDateEl) {
        editDateEl.addEventListener('change', function() {
            var day = new Date(this.value).getDay();
            buildEditTimeOptions(day === 0 ? sundayTimesE : weekdayTimesE, '');
            document.getElementById('editDateGroup').classList.remove('error');
        });
    }
    // Bạn nhớ bọc tương tự cho editTimeSelect, oldPassword, newPassword... nhé!
});

// --- CÁC HÀM XỬ LÝ LÂM SÀNG ---

function toggleToothSelection(toothNumber) {
    const index = currentSelectedTeeth.indexOf(toothNumber);
    if (index > -1) {
        currentSelectedTeeth.splice(index, 1); 
    } else {
        currentSelectedTeeth.push(toothNumber); 
    }
    updateOdontogramUI();
}

function updateOdontogramUI() {
    document.querySelectorAll('.tooth').forEach(el => {
        const tNum = parseInt(el.getAttribute('data-tooth'));
        // Giữ nguyên class gốc (như 'issue'), chỉ toggle class 'selected'
        if (currentSelectedTeeth.includes(tNum)) {
            el.classList.add('selected');
        } else {
            el.classList.remove('selected');
        }
    });
}

// Hàm này bạn sẽ gọi khi bác sĩ chọn dịch vụ từ Dropdown (Bạn tự làm 1 cái dropdown nhé, hoặc gắn tạm vào nút Add)
function addClinicalService(serviceId) {
    const svc = danhSachDichVuTuDB.find(s => s.id == serviceId);
    if (!svc) return;

    if (svc.perUnit) { 
        if (currentSelectedTeeth.length === 0) {
            alert('Vui lòng click chọn ít nhất 1 răng trên sơ đồ trước khi thêm dịch vụ này!');
            return;
        }
        
        // Gộp tất cả răng đã chọn vào 1 dòng duy nhất
        clinicalSelectedServices.push({
            dichVu_ID: parseInt(svc.id), 
            viTriRang: currentSelectedTeeth.sort((a, b) => a - b).join(', '),
            soLuong: currentSelectedTeeth.length,
            donGia: svc.price,
            name: svc.name,
            isPerUnit: true
        });
        
        currentSelectedTeeth = []; 
        updateOdontogramUI();
    } else { 
        clinicalSelectedServices.push({
            dichVu_ID: parseInt(svc.id), 
            viTriRang: null, 
            soLuong: 1,      
            donGia: svc.price,
            name: svc.name,
            isPerUnit: false
        });
    }
    renderClinicalTable();
}

function removeClinicalService(index) {
    clinicalSelectedServices.splice(index, 1);
    renderClinicalTable();
}

function renderClinicalTable() {
    const tbody = document.getElementById('treatmentBody'); // ĐÃ CẬP NHẬT ID KHỚP VỚI JSP
    if(!tbody) return; 

    let html = '';
    clinicalSelectedServices.forEach((item, index) => {
        let thanhTien = item.donGia * item.soLuong;
        html += `
            <tr>
                <td>${item.name}</td>
                <td>${item.viTriRang ? 'Răng ' + item.viTriRang : 'Toàn hàm'}</td>
                <td>
                    <input type="number" style="width: 60px" value="${item.soLuong}" min="1" 
                        ${!item.isPerUnit ? 'readonly' : ''} 
                        onchange="clinicalSelectedServices[${index}].soLuong = parseInt(this.value); renderClinicalTable();">
                </td>
                <td>${item.donGia.toLocaleString('vi-VN')} đ</td>
                <td>${thanhTien.toLocaleString('vi-VN')} đ</td>
                <td><input type="text" placeholder="Ghi chú..." style="width: 100%"></td>
                <td><button type="button" class="btn btn-danger" style="padding: 5px 10px;" onclick="removeClinicalService(${index})"><i class="fa-solid fa-trash"></i></button></td>
            </tr>
        `;
    });
    tbody.innerHTML = html;
}

async function luuPhieuKhamLamSang(lichHenId, bacSiId, phongId) {
    if (clinicalSelectedServices.length === 0) {
        alert("Vui lòng chỉ định ít nhất 1 dịch vụ!");
        return;
    }

    // ĐÃ CẬP NHẬT CÁC ID KHỚP CHÍNH XÁC VỚI JSP CỦA BẠN
    const payload = {
        lichHenID: parseInt(lichHenId), 
        bacSiID: parseInt(bacSiId),     
        phongID: parseInt(phongId),     
        lyDoKham: document.getElementById('symptoms')?.value || '', 
        chanDoan: document.getElementById('diagnosis')?.value || '',
        ghiChu: document.getElementById('prescription')?.value || '', 
        danhSachDichVu: clinicalSelectedServices 
    };

    try {
        const btnSave = document.getElementById('saveBtn');
        if(btnSave) {
            btnSave.disabled = true;
            btnSave.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> ĐANG LƯU...';
        }

        const response = await fetch('/api/doctor/save-examination', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const result = await response.json();
            if (result.success) {
                alert('✅ Hoàn tất khám & Lưu dữ liệu thành công!');
                window.location.href = 'index.jsp'; // Quay lại danh sách lịch hẹn
            } else {
                alert('Lưu thất bại: ' + result.message);
            }
        } else {
             alert('Lỗi Server: ' + response.status);
        }
    } catch (error) {
        console.error("Lỗi khi call API: ", error);
        alert('Không thể kết nối đến hệ thống!');
    } finally {
        const btnSave = document.getElementById('saveBtn');
        if(btnSave) {
            btnSave.disabled = false;
            btnSave.innerHTML = '<i class="fa-solid fa-check-double"></i> HOÀN TẤT & LƯU';
        }
    }
}

function applyLichHenContextFromQuery() {
    const reason = getQueryParam('reason');
    const rec = document.getElementById('receptionNoteUI');
    if (rec) {
        rec.textContent = reason ? `"${reason}"` : '—';
    }
    const sym = document.getElementById('symptoms');
    if (sym && reason && !sym.value.trim()) {
        sym.value = reason;
    }
}

// --- HÀM LẤY THÔNG TIN HỒ SƠ ---
async function loadPatientProfile() {
    // Lấy ID từ URL (VD: hoso.jsp?id=1)
    const lichHenId = getQueryParam('id'); 
    if (!lichHenId) return;

    try {
        const response = await fetch(`${DOCTOR_HOSO_CONFIG.API_BASE}/patient-profile?id=${encodeURIComponent(lichHenId)}`);
        const result = await response.json();
        
        if (result.success && result.data) {
            const hs = result.data;
            if (!hs.benhNhan || !hs.benhNhan.taiKhoan) {
                console.error('Lỗi từ server: thiếu benhNhan/taiKhoan trong payload');
                return;
            }
            const tk = hs.benhNhan.taiKhoan;
            const maBN = hs.benhNhan.benhNhanID;
            
            // 1. Dịch giới tính 
            const gioiTinhStr = tk.gioiTinh ? 'Nam' : 'Nữ';
            
            // 2. Tính tuổi
            let tuoi = "Chưa rõ";
            if (tk.ngaySinh) {
                const birthYear = new Date(tk.ngaySinh).getFullYear();
                const currentYear = new Date().getFullYear();
                tuoi = currentYear - birthYear;
            }
            
            // 3. Đổ dữ liệu vào giao diện (Đảm bảo hoso.jsp của bạn có các ID thẻ HTML này)
            const nameEl = document.getElementById('patientNameUI');
            if(nameEl) nameEl.innerText = tk.hoTen;
            
            const ageEl = document.getElementById('patientAgeGenderUI');
            if(ageEl) ageEl.innerText = `${gioiTinhStr} | ${tuoi} Tuổi`;
            
            const phoneEl = document.getElementById('patientPhoneUI');
            if(phoneEl) phoneEl.innerText = tk.soDienThoai;
            
            // Hiển thị mã bệnh nhân (VD: #BN00005)
            const codeEl = document.getElementById('patientCodeUI');
            if(codeEl) codeEl.innerText = `#BN${maBN.toString().padStart(5, '0')}`;
            
            const diUngEl = document.getElementById('diUngThuocUI');
            if(diUngEl) diUngEl.innerText = hs.diUngThuoc;
            
            const tienSuEl = document.getElementById('tienSuBenhUI');
            if(tienSuEl) tienSuEl.innerText = hs.tienSuBenh;
            
        } else {
            console.error("Lỗi từ server:", result.message);
            showToast(result.message || 'Không tải được hồ sơ bệnh nhân', 3500);
        }
    } catch (error) {
        console.error("Lỗi khi tải hồ sơ: ", error);
        showToast('Không tải được hồ sơ bệnh nhân', 3500);
    }
}

// Gọi hàm này ngay khi trang load xong
document.addEventListener('DOMContentLoaded', function() {
    applyLichHenContextFromQuery();
    loadPatientProfile();
    init();
});