/**
 * Configuration for clinical examination interface
 */
const DOCTOR_HOSO_CONFIG = {
    API_BASE: '/Dental_Clinic_Management/api/doctor' 
};

// Global state for selected teeth and services
let currentSelectedTeeth = [];
let clinicalSelectedServices = []; // Format: { dichVu_ID, tenDichVu, donGia, soLuong, isPerUnit, viTriRang }

/**
 * Initialize interface components and events
 */
function init() {
    // 1. Dịch vụ Dropdown
    const addServiceBtn = document.getElementById('addServiceBtn');
    const serviceSelect = document.getElementById('serviceSelect');

    // Load services into select
    if (serviceSelect && typeof danhSachDichVuTuDB !== 'undefined') {
        serviceSelect.innerHTML = '<option value="">-- Chọn dịch vụ --</option>';
        danhSachDichVuTuDB.forEach(service => {
            const option = document.createElement('option');
            option.value = service.dichVuID; // Khớp với model Java (Gson chuyển qua)
            option.textContent = `${service.tenDichVu} (${service.giaTien.toLocaleString()}đ)`;
            serviceSelect.appendChild(option);
        });
    }

    if (addServiceBtn) {
        addServiceBtn.addEventListener('click', function() {
            const serviceId = serviceSelect.value;
            if (!serviceId) {
                alert('Vui lòng chọn dịch vụ!');
                return;
            }
            addClinicalService(serviceId);
        });
    }

    // 2. Odontogram (Sơ đồ răng)
    const teeth = document.querySelectorAll('.tooth');
    teeth.forEach(tooth => {
        tooth.addEventListener('click', function() {
            const toothNumber = this.dataset.tooth;
            if (this.classList.contains('selected')) {
                this.classList.remove('selected');
                currentSelectedTeeth = currentSelectedTeeth.filter(t => t !== toothNumber);
            } else {
                this.classList.add('selected');
                currentSelectedTeeth.push(toothNumber);
            }
        });
    });

    // 3. Nút Lưu
    const saveBtn = document.getElementById('saveBtn');
    if(saveBtn) {
        saveBtn.addEventListener('click', function() {
            const currentId = getQueryParam('id');
            if (currentId) {
                luuPhieuKhamLamSang(currentId, 1, 1); 
            } else {
                alert('Lỗi: Không tìm thấy ID lịch hẹn trên URL!');
            }
        });
    }

    // 4. Load dữ liệu bệnh nhân
    const lichHenId = getQueryParam('id');
    if (lichHenId) {
        loadPatientProfile(lichHenId);
    }
}

/**
 * Helper to get query parameters from URL
 */
function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

/**
 * Add a service to the clinical examination table
 */
function addClinicalService(serviceId) {
    // Tìm dịch vụ từ biến toàn cục danhSachDichVuTuDB
    const service = danhSachDichVuTuDB.find(s => s.dichVuID == serviceId);
    if (!service) return;

    // Validation: If service is per-tooth, user must select teeth
    if (service.tinhTheoRang && currentSelectedTeeth.length === 0) {
        alert('Dịch vụ này yêu cầu chọn răng trên sơ đồ!');
        return;
    }

    // Create or Update grouped row
    const existingIndex = clinicalSelectedServices.findIndex(s => s.dichVu_ID == serviceId);
    
    if (service.tinhTheoRang) {
        if (existingIndex > -1) {
            // Merge teeth
            const existingTeeth = clinicalSelectedServices[existingIndex].viTriRang.split(', ').filter(t => t);
            currentSelectedTeeth.forEach(t => {
                if (!existingTeeth.includes(t)) existingTeeth.push(t);
            });
            clinicalSelectedServices[existingIndex].viTriRang = existingTeeth.sort((a, b) => a - b).join(', ');
            clinicalSelectedServices[existingIndex].soLuong = existingTeeth.length;
        } else {
            clinicalSelectedServices.push({
                dichVu_ID: service.dichVuID,
                tenDichVu: service.tenDichVu,
                donGia: service.giaTien,
                soLuong: currentSelectedTeeth.length,
                isPerUnit: true,
                viTriRang: currentSelectedTeeth.sort((a, b) => a - b).join(', ')
            });
        }
    } else {
        // Whole mouth service (Toàn hàm)
        if (existingIndex > -1) {
            clinicalSelectedServices[existingIndex].soLuong += 1;
        } else {
            clinicalSelectedServices.push({
                dichVu_ID: service.dichVuID,
                tenDichVu: service.tenDichVu,
                donGia: service.giaTien,
                soLuong: 1,
                isPerUnit: false,
                viTriRang: 'Toàn hàm'
            });
        }
    }

    // Reset tooth selection and update UI
    clearToothSelection();
    renderClinicalTable();
}

/**
 * Clear visual tooth selection
 */
function clearToothSelection() {
    document.querySelectorAll('.tooth.selected').forEach(el => {
        el.classList.remove('selected');
    });
    currentSelectedTeeth = [];
}

/**
 * Render the clinical services table
 */
function renderClinicalTable() {
    const tableBody = document.getElementById('treatmentBody');
    if (!tableBody) return;

    tableBody.innerHTML = '';
    clinicalSelectedServices.forEach((item, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${item.tenDichVu}</td>
            <td>${item.viTriRang}</td>
            <td>
                <input type="number" class="qty-input" value="${item.soLuong}" min="1" 
                        style="width: 60px; padding: 4px; border-radius: 4px; border: 1px solid #ddd;"
                        ${!item.isPerUnit ? '' : 'readonly'}
                        onchange="clinicalSelectedServices[${index}].soLuong = parseInt(this.value); renderClinicalTable();">
            </td>
            <td>${item.donGia.toLocaleString()}đ</td>
            <td>${(item.donGia * item.soLuong).toLocaleString()}đ</td>
            <td><input type="text" placeholder="Ghi chú..." style="width:100%; padding:4px; border:none; background:transparent;"></td>
            <td><button class="btn-delete" onclick="removeClinicalService(${index})" style="color: #ff4d4d; background: none; border: none; cursor: pointer;"><i class="fa-solid fa-trash"></i></button></td>
        `;
        tableBody.appendChild(row);
    });
}

/**
 * Remove a service row from the table
 */
function removeClinicalService(index) {
    clinicalSelectedServices.splice(index, 1);
    renderClinicalTable();
}

/**
 * Handle data submission to backend
 */
async function luuPhieuKhamLamSang(lichHenId, bacSiId, phongId) {
    if (clinicalSelectedServices.length === 0) {
        alert('Vui lòng chọn ít nhất một dịch vụ!');
        return;
    }

    const flattenedServices = [];
    clinicalSelectedServices.forEach(item => {
        if (item.isPerUnit && item.viTriRang) {
            const teeth = item.viTriRang.split(', ');
            const qtyPerTooth = Math.floor(item.soLuong / teeth.length) || 1; 
            
            teeth.forEach(tNum => {
                flattenedServices.push({
                    dichVuID: parseInt(item.dichVu_ID),
                    viTriRang: parseInt(tNum.trim()),
                    donGia: item.donGia,
                    soLuong: qtyPerTooth
                });
            });
        } else {
            flattenedServices.push({
                dichVuID: parseInt(item.dichVu_ID),
                viTriRang: 0, 
                donGia: item.donGia,
                soLuong: item.soLuong
            });
        }
    });

    const payload = {
        lichHenID: parseInt(lichHenId),
        chanDoan: document.getElementById('diagnosis').value,
        lyDoKham: document.getElementById('symptoms').value,
        ghiChu: document.getElementById('notes') ? document.getElementById('notes').value : "",
        danhSachDichVu: flattenedServices
    };

    try {
        const response = await fetch(`${DOCTOR_HOSO_CONFIG.API_BASE}/save-examination`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const result = await response.json();
        if (result.success) {
            alert('Lưu phiếu khám thành công!');
            window.location.href = 'index.jsp';
        } else {
            alert('Lưu thất bại: ' + result.message);
        }
    } catch (error) {
        console.error('Lỗi khi lưu:', error);
        alert('Có lỗi xảy ra khi kết nối tới máy chủ!');
    }
}

/**
 * Load patient profile data from API
 */
async function loadPatientProfile(lichHenId) {
    try {
        const response = await fetch(`${DOCTOR_HOSO_CONFIG.API_BASE}/patient-profile?id=${encodeURIComponent(lichHenId)}`);
        const result = await response.json();
        
        if (result.success && result.data) {
            const lh = result.data; 
            const bn = lh.benhNhan;
            const hs = bn.hoSo;
            const tkBN = bn.taiKhoan;
            
            if (!tkBN) return;
            
            const gioiTinhStr = tkBN.gioiTinh ? 'Nam' : 'Nữ';
            
            let tuoi = "Chưa rõ";
            if (tkBN.ngaySinh) {
                const birthYear = new Date(tkBN.ngaySinh).getFullYear();
                const currentYear = new Date().getFullYear();
                tuoi = currentYear - birthYear;
            }
            
            if(document.getElementById('patientNameUI')) document.getElementById('patientNameUI').innerText = tkBN.hoTen;
            if(document.getElementById('patientAgeGenderUI')) document.getElementById('patientAgeGenderUI').innerText = `${gioiTinhStr} | ${tuoi} Tuổi`;
            if(document.getElementById('patientPhoneUI')) document.getElementById('patientPhoneUI').innerText = tkBN.soDienThoai;
            if(document.getElementById('patientCodeUI')) document.getElementById('patientCodeUI').innerText = `#BN${bn.benhNhanID.toString().padStart(5, '0')}`;
            if(document.getElementById('diUngThuocUI') && hs) document.getElementById('diUngThuocUI').innerText = hs.diUngThuoc;
            if(document.getElementById('tienSuBenhUI') && hs) document.getElementById('tienSuBenhUI').innerText = hs.tienSuBenh;
            
            const doctorRoomUI = document.getElementById('doctorRoomUI');
            if (doctorRoomUI && lh.bacSi && lh.phongKham) {
                const tenBS = lh.bacSi.taiKhoan ? lh.bacSi.taiKhoan.hoTen : "Chưa rõ";
                const tenPhong = lh.phongKham.tenPhong || "Chưa rõ";
                doctorRoomUI.innerHTML = `<i class="fa-solid fa-user-doctor"></i> ${tenPhong.toUpperCase()} - ${tenBS}`;
            }
        }
    } catch (error) {
        console.error("Lỗi khi tải hồ sơ: ", error);
    }
}

// Start everything on load
document.addEventListener('DOMContentLoaded', init);