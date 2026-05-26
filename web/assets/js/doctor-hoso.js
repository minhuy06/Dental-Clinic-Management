/**
 * Khám lâm sàng — gọi API context-path
 */
(function initDoctorHosoBootstrap() {
    if (typeof AppBootstrap === 'undefined') return;
    var cp = AppBootstrap.getMetaContent('context-path');
    if (cp) window.CONTEXT_PATH = cp;
    var boot = AppBootstrap.readJsonScript('doctorHosoBootstrapJson', null);
    if (boot) window.DOCTOR_HOSO_BOOTSTRAP = boot;
    var services = AppBootstrap.readJsonScript('doctorServicesJson', null);
    if (services !== null) window.danhSachDichVuTuDB = services;
    var booked = AppBootstrap.readJsonScript('doctorBookedServicesJson', null);
    if (booked !== null) window.INITIAL_BOOKED_SERVICES = booked;
})();

const DOCTOR_HOSO_CONFIG = {
    API_BASE: (typeof window.CONTEXT_PATH !== 'undefined' && window.CONTEXT_PATH ? window.CONTEXT_PATH : '') + '/api/doctor'
};

let currentSelectedTeeth = [];
let clinicalSelectedServices = [];

function init() {
    const addServiceBtn = document.getElementById('addServiceBtn');
    const serviceSelect = document.getElementById('serviceSelect');

    if (serviceSelect && typeof danhSachDichVuTuDB !== 'undefined' && danhSachDichVuTuDB) {
        serviceSelect.innerHTML = '<option value="">-- Chọn dịch vụ --</option>';
        danhSachDichVuTuDB.forEach(service => {
            const option = document.createElement('option');
            option.value = service.dichVuID;
            option.textContent = `${service.tenDichVu} (${service.giaTien.toLocaleString()}đ)`;
            serviceSelect.appendChild(option);
        });
    }

    if (addServiceBtn) {
        addServiceBtn.addEventListener('click', function() {
            const serviceId = serviceSelect.value;
            if (!serviceId) {
                AppNotify.warn('Vui lòng chọn dịch vụ!');
                return;
            }
            addClinicalService(serviceId);
        });
    }

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

    const saveBtn = document.getElementById('saveBtn');
    if (saveBtn) {
        saveBtn.addEventListener('click', function() {
            const currentId = getQueryParam('id');
            if (currentId) {
                luuPhieuKhamLamSang(currentId, {
                    draft: true,
                    redirect: true,
                    redirectUrl: doctorListUrl(),
                    successMessage: 'Đã lưu phiếu khám. Quay lại danh sách lịch hẹn.'
                });
            } else {
                AppNotify.error('Lỗi: Không tìm thấy ID lịch hẹn trên URL!');
            }
        });
    }

    const reasonParam = getQueryParam('reason');
    const symEl = document.getElementById('symptoms');
    if (symEl && reasonParam) {
        symEl.value = decodeURIComponent(reasonParam);
    }

    if (typeof window.INITIAL_BOOKED_SERVICES !== 'undefined'
            && Array.isArray(window.INITIAL_BOOKED_SERVICES)
            && window.INITIAL_BOOKED_SERVICES.length > 0) {
        applyBookedServices(window.INITIAL_BOOKED_SERVICES);
    }

    const lichHenId = getQueryParam('id');
    if (lichHenId) {
        loadPatientProfile(lichHenId);
    }
}

function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

function doctorListUrl() {
    const base = (typeof window.CONTEXT_PATH !== 'undefined' && window.CONTEXT_PATH)
        ? window.CONTEXT_PATH
        : '';
    return base + '/doctor/index.jsp';
}

function addClinicalService(serviceId) {
    if (typeof danhSachDichVuTuDB === 'undefined' || !danhSachDichVuTuDB) return;
    const service = danhSachDichVuTuDB.find(s => s.dichVuID == serviceId);
    if (!service) return;

    if (service.tinhTheoRang && currentSelectedTeeth.length === 0) {
        AppNotify.warn('Dịch vụ này yêu cầu chọn răng trên sơ đồ!');
        return;
    }

    const existingIndex = clinicalSelectedServices.findIndex(s => s.dichVu_ID == serviceId);

    if (service.tinhTheoRang) {
        if (existingIndex > -1) {
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

    clearToothSelection();
    renderClinicalTable();
}

function clearToothSelection() {
    document.querySelectorAll('.tooth.selected').forEach(el => {
        el.classList.remove('selected');
    });
    currentSelectedTeeth = [];
}

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

function removeClinicalService(index) {
    clinicalSelectedServices.splice(index, 1);
    renderClinicalTable();
}

function applyBookedServices(bookingList) {
    clinicalSelectedServices = [];
    (bookingList || []).forEach(ct => {
        const dv = ct.dichVu || {};
        const id = dv.dichVuID || dv.dichVuId || ct.dichVuId || ct.dichVuID;
        if (!id) return;
        const meta = danhSachDichVuTuDB.find(s => String(s.dichVuID) === String(id)) || dv;
        const perUnit = !!(dv.tinhTheoRang || meta.tinhTheoRang);
        clinicalSelectedServices.push({
            dichVu_ID: parseInt(id, 10),
            tenDichVu: dv.tenDichVu || meta.tenDichVu,
            donGia: dv.giaTien != null ? dv.giaTien : meta.giaTien,
            soLuong: ct.soLuong || 1,
            isPerUnit: perUnit,
            viTriRang: perUnit ? '' : 'Toàn hàm'
        });
    });
    renderClinicalTable();
}

function applyPhieuKhamLines(lines) {
    clinicalSelectedServices = [];
    (lines || []).forEach(ct => {
        const dv = ct.dichVu || {};
        const id = ct.dichVuID || ct.dichVuId || dv.dichVuID || dv.dichVuId;
        if (!id) return;
        const meta = danhSachDichVuTuDB.find(s => String(s.dichVuID) === String(id)) || dv;
        const perUnit = !!(dv.tinhTheoRang || meta.tinhTheoRang);
        const toothNum = ct.viTriRang > 0 ? String(ct.viTriRang) : null;

        if (perUnit && toothNum) {
            const existingIndex = clinicalSelectedServices.findIndex(s => String(s.dichVu_ID) === String(id));
            if (existingIndex > -1) {
                const teeth = clinicalSelectedServices[existingIndex].viTriRang.split(', ').filter(Boolean);
                if (!teeth.includes(toothNum)) teeth.push(toothNum);
                clinicalSelectedServices[existingIndex].viTriRang = teeth.sort((a, b) => a - b).join(', ');
                clinicalSelectedServices[existingIndex].soLuong = teeth.length;
            } else {
                clinicalSelectedServices.push({
                    dichVu_ID: parseInt(id, 10),
                    tenDichVu: dv.tenDichVu || meta.tenDichVu,
                    donGia: ct.donGia != null ? ct.donGia : (meta.giaTien || dv.giaTien),
                    soLuong: ct.soLuong || 1,
                    isPerUnit: true,
                    viTriRang: toothNum
                });
            }
        } else {
            const existingIndex = clinicalSelectedServices.findIndex(s => String(s.dichVu_ID) === String(id) && !s.isPerUnit);
            if (existingIndex > -1) {
                clinicalSelectedServices[existingIndex].soLuong += (ct.soLuong || 1);
            } else {
                clinicalSelectedServices.push({
                    dichVu_ID: parseInt(id, 10),
                    tenDichVu: dv.tenDichVu || meta.tenDichVu,
                    donGia: ct.donGia != null ? ct.donGia : (meta.giaTien || dv.giaTien),
                    soLuong: ct.soLuong || 1,
                    isPerUnit: false,
                    viTriRang: 'Toàn hàm'
                });
            }
        }
    });
    renderClinicalTable();
}

function appendMissingBookedServices(bookingList) {
    (bookingList || []).forEach(ct => {
        const dv = ct.dichVu || {};
        const id = dv.dichVuID || dv.dichVuId || ct.dichVuId || ct.dichVuID;
        if (!id) return;
        if (clinicalSelectedServices.findIndex(s => String(s.dichVu_ID) === String(id)) >= 0) {
            return;
        }
        const meta = (typeof danhSachDichVuTuDB !== 'undefined' && danhSachDichVuTuDB)
            ? (danhSachDichVuTuDB.find(s => String(s.dichVuID) === String(id)) || dv)
            : dv;
        const perUnit = !!(dv.tinhTheoRang || meta.tinhTheoRang);
        clinicalSelectedServices.push({
            dichVu_ID: parseInt(id, 10),
            tenDichVu: dv.tenDichVu || meta.tenDichVu,
            donGia: dv.giaTien != null ? dv.giaTien : meta.giaTien,
            soLuong: ct.soLuong || 1,
            isPerUnit: perUnit,
            viTriRang: perUnit ? '' : 'Toàn hàm'
        });
    });
    renderClinicalTable();
}

/** Luôn hiển thị dịch vụ đã đặt lịch; phiếu khám (nếu có) ghi đè/bổ sung. */
function hydrateClinicalFromProfile(lh) {
    if (!lh) return;
    const booked = lh.danhSachDichVuDat || [];
    const clinical = (lh.phieuKham && lh.phieuKham.danhSachDichVu) ? lh.phieuKham.danhSachDichVu : [];

    if (clinical.length > 0) {
        applyPhieuKhamLines(clinical);
        appendMissingBookedServices(booked);
    } else if (booked.length > 0) {
        applyBookedServices(booked);
    }

    if (lh.phieuKham) {
        const diagEl = document.getElementById('diagnosis');
        const symEl = document.getElementById('symptoms');
        const prescEl = document.getElementById('prescription');
        if (diagEl && lh.phieuKham.chanDoan) diagEl.value = lh.phieuKham.chanDoan;
        if (symEl && lh.phieuKham.lyDoKham) symEl.value = lh.phieuKham.lyDoKham;
        if (prescEl && lh.phieuKham.ghiChu) prescEl.value = lh.phieuKham.ghiChu;
    }
}

async function luuPhieuKhamLamSang(lichHenId, options) {
    const opts = typeof options === 'boolean'
        ? { draft: options, redirect: !options }
        : (options || { draft: true, redirect: false });
    const asDraft = opts.draft !== false;
    const shouldRedirect = !!opts.redirect;
    const redirectTarget = opts.redirectUrl || doctorListUrl();
    const successMessage = opts.successMessage || null;
    if (clinicalSelectedServices.length === 0) {
        AppNotify.warn('Vui lòng chọn ít nhất một dịch vụ!');
        return;
    }

    const flattenedServices = [];
    clinicalSelectedServices.forEach(item => {
        if (item.isPerUnit && item.viTriRang && String(item.viTriRang).trim()) {
            const teeth = String(item.viTriRang).split(',').map(t => t.trim()).filter(Boolean);
            const qtyPerTooth = Math.max(1, Math.floor(item.soLuong / Math.max(1, teeth.length)));

            teeth.forEach(tNum => {
                const tooth = parseInt(tNum, 10);
                flattenedServices.push({
                    dichVuID: parseInt(item.dichVu_ID, 10),
                    viTriRang: Number.isFinite(tooth) ? tooth : 0,
                    donGia: item.donGia,
                    soLuong: qtyPerTooth
                });
            });
        } else {
            flattenedServices.push({
                dichVuID: parseInt(item.dichVu_ID, 10),
                viTriRang: 0,
                donGia: item.donGia,
                soLuong: item.soLuong || 1
            });
        }
    });

    const presc = document.getElementById('prescription');
    const payload = {
        lichHenID: parseInt(lichHenId, 10),
        chanDoan: document.getElementById('diagnosis').value,
        lyDoKham: document.getElementById('symptoms').value,
        ghiChu: presc ? presc.value : '',
        danhSachDichVu: flattenedServices,
        draft: !!asDraft
    };

    try {
        const response = await fetch(`${DOCTOR_HOSO_CONFIG.API_BASE}/save-examination`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const raw = (await response.text()).replace(/^\uFEFF/, '').trim();
        let result;
        try {
            result = JSON.parse(raw);
        } catch (parseErr) {
            console.error('Phản hồi không phải JSON:', raw);
            const preview = (raw || '').trim().substring(0, 300);
            if (preview.startsWith('<')) {
                AppNotify.error('API lưu phiếu khám không hoạt động (máy chủ trả HTML). Kiểm tra deploy WAR Dental_Clinic_Management và URL: ' + DOCTOR_HOSO_CONFIG.API_BASE + '/save-examination');
            } else {
                AppNotify.error('Phản hồi không hợp lệ từ máy chủ:<br>' + (preview || '(rỗng)'));
            }
            return;
        }

        if (result.success) {
            if (shouldRedirect) {
                await AppNotify.success(successMessage || result.message || 'Đã lưu phiếu khám. Quay lại danh sách lịch hẹn.');
                window.location.href = redirectTarget;
            } else {
                await AppNotify.success(result.message || 'Đã lưu nháp. Bạn có thể tiếp tục chỉnh sửa.');
            }
        } else {
            AppNotify.error('Lưu thất bại: ' + result.message);
        }
    } catch (error) {
        console.error('Lỗi khi lưu:', error);
        AppNotify.error('Có lỗi xảy ra khi kết nối tới máy chủ!');
    }
}

async function loadPatientProfile(lichHenId) {
    try {
        const response = await fetch(`${DOCTOR_HOSO_CONFIG.API_BASE}/patient-profile?id=${encodeURIComponent(lichHenId)}`);
        const raw = (await response.text()).replace(/^\uFEFF/, '').trim();
        let result;
        try {
            result = JSON.parse(raw);
        } catch (parseErr) {
            console.error('patient-profile không trả JSON:', raw);
            return;
        }

        if (result.success && result.data) {
            const lh = result.data;
            const bn = lh.benhNhan;
            if (!bn) return;
            const hs = bn.hoSo;
            const tkBN = bn.taiKhoan;

            if (!tkBN) return;

            const gioiTinhStr = tkBN.gioiTinh ? 'Nam' : 'Nữ';

            let tuoi = 'Chưa rõ';
            if (tkBN.ngaySinh) {
                const birthYear = new Date(tkBN.ngaySinh).getFullYear();
                const currentYear = new Date().getFullYear();
                tuoi = currentYear - birthYear;
            }

            if (document.getElementById('patientNameUI')) document.getElementById('patientNameUI').innerText = tkBN.hoTen;
            if (document.getElementById('patientAgeGenderUI')) document.getElementById('patientAgeGenderUI').innerText = `${gioiTinhStr} | ${tuoi} Tuổi`;
            if (document.getElementById('patientPhoneUI')) document.getElementById('patientPhoneUI').innerText = tkBN.soDienThoai;
            if (document.getElementById('patientCodeUI')) document.getElementById('patientCodeUI').innerText = `#BN${bn.benhNhanID.toString().padStart(5, '0')}`;
            if (document.getElementById('diUngThuocUI') && hs) document.getElementById('diUngThuocUI').innerText = hs.diUngThuoc;
            if (document.getElementById('tienSuBenhUI') && hs) document.getElementById('tienSuBenhUI').innerText = hs.tienSuBenh;

            const noteEl = document.getElementById('receptionNoteUI');
            if (noteEl && lh.ghiChu) {
                noteEl.textContent = lh.ghiChu;
            }

            hydrateClinicalFromProfile(lh);

            if ((!lh.phieuKham || !lh.phieuKham.danhSachDichVu || !lh.phieuKham.danhSachDichVu.length)
                    && (!lh.danhSachDichVuDat || !lh.danhSachDichVuDat.length)
                    && typeof window.INITIAL_BOOKED_SERVICES !== 'undefined'
                    && window.INITIAL_BOOKED_SERVICES.length) {
                applyBookedServices(window.INITIAL_BOOKED_SERVICES);
            }

            const doctorRoomUI = document.getElementById('doctorRoomUI');
            if (doctorRoomUI && lh.bacSi && lh.phongKham) {
                const tenBS = lh.bacSi.taiKhoan ? lh.bacSi.taiKhoan.hoTen : 'Chưa rõ';
                const tenPhong = lh.phongKham.tenPhong || 'Chưa rõ';
                doctorRoomUI.innerHTML = `<i class="fa-solid fa-user-doctor"></i> ${tenPhong.toUpperCase()} - ${tenBS}`;
            }
        }
    } catch (error) {
        console.error('Lỗi khi tải hồ sơ: ', error);
    }
}

document.addEventListener('DOMContentLoaded', init);
