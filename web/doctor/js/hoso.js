// ======================= DANH SÁCH DỊCH VỤ =======================
const DENTAL_SERVICES = [
    { id: 1, name: "Thăm khám & tư vấn", price: 100000 },
    { id: 2, name: "Lấy cao răng", price: 200000 },
    { id: 3, name: "Hàn răng thẩm mỹ (Composit)", price: 500000 },
    { id: 4, name: "Điều trị tủy", price: 1000000 },
    { id: 5, name: "Nhổ răng thường", price: 300000 },
    { id: 6, name: "Nhổ răng khôn phức tạp", price: 1500000 },
    { id: 7, name: "Bọc răng sứ kim loại", price: 2500000 },
    { id: 8, name: "Bọc răng toàn sứ", price: 4500000 },
    { id: 9, name: "Tẩy trắng răng tại phòng", price: 1800000 },
    { id: 10, name: "Chỉnh nha (niềng răng mắc cài)", price: 25000000 },
    { id: 11, name: "Chỉnh nha (niềng răng trong suốt)", price: 45000000 },
    { id: 12, name: "Cấy ghép Implant", price: 18000000 }
];

// Format tiền Việt
function formatVND(amount) {
    return amount.toLocaleString('vi-VN') + 'đ';
}

// Cập nhật đơn giá & thành tiền cho một dòng
function updateRowPrices(row) {
    const serviceSelect = row.querySelector('.service-select');
    const selectedOption = serviceSelect.options[serviceSelect.selectedIndex];
    const price = parseInt(selectedOption.getAttribute('data-price')) || 0;
    const quantity = parseInt(row.querySelector('.quantity-input').value) || 1;
    const total = price * quantity;
    
    const priceCell = row.querySelector('.price-cell');
    const totalCell = row.querySelector('.total-cell');
    if (priceCell) priceCell.innerText = formatVND(price);
    if (totalCell) totalCell.innerText = formatVND(total);
}

// Gắn sự kiện cho một dòng (thay đổi dịch vụ, số lượng, focus)
function attachRowEvents(row) {
    const serviceSelect = row.querySelector('.service-select');
    const quantityInput = row.querySelector('.quantity-input');
    const toothInput = row.querySelector('.tooth-number-input');
    
    if (serviceSelect) {
        serviceSelect.removeEventListener('change', () => updateRowPrices(row));
        serviceSelect.addEventListener('change', () => updateRowPrices(row));
    }
    if (quantityInput) {
        quantityInput.removeEventListener('input', () => updateRowPrices(row));
        quantityInput.addEventListener('input', () => updateRowPrices(row));
    }
    if (toothInput) {
        toothInput.removeEventListener('focus', () => { window.currentActiveRow = row; });
        toothInput.addEventListener('focus', () => { window.currentActiveRow = row; });
    }
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

// Thêm dòng mới (có thể truyền số răng mặc định)
function addNewRow(toothNumber = '') {
    const tbody = document.getElementById('treatmentBody');
    const newRow = document.createElement('tr');
    // Tạo dropdown dịch vụ từ DENTAL_SERVICES
    let serviceOptions = '<option value="">-- Chọn dịch vụ --</option>';
    DENTAL_SERVICES.forEach(s => {
        serviceOptions += `<option value="${s.name}" data-price="${s.price}">${s.name} - ${formatVND(s.price)}</option>`;
    });
    
    newRow.innerHTML = `
        <td><select class="service-select">${serviceOptions}</select></td>
        <td><input type="text" class="tooth-number-input" value="${toothNumber}" placeholder="Số răng" style="width: 80px;"></td>
        <td><input type="number" class="quantity-input" value="1" min="1" step="1" style="width: 70px;"></td>
        <td class="price-cell">0đ</td>
        <td class="total-cell">0đ</td>
        <td><input type="text" class="note-input" placeholder="Ghi chú..." style="width: 100%;"></td>
        <td><button class="delete-row" type="button"><i class="fa-solid fa-trash"></i></button></td>
    `;
    tbody.appendChild(newRow);
    attachRowEvents(newRow);
    attachDeleteEvents();
    updateRowPrices(newRow);
    // Focus vào ô răng số của dòng mới để chuẩn bị click sơ đồ
    const toothInput = newRow.querySelector('.tooth-number-input');
    if (toothInput) toothInput.focus();
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

function initTeethMap() {
    const teeth = document.querySelectorAll('.tooth');
    teeth.forEach(tooth => {
        tooth.removeEventListener('click', toothClickHandler);
        tooth.addEventListener('click', toothClickHandler);
    });
}

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
function saveExamination() {
    const treatments = [];
    const rows = document.querySelectorAll('#treatmentBody tr');
    rows.forEach(row => {
        const serviceSelect = row.querySelector('.service-select');
        treatments.push({
            serviceName: serviceSelect ? serviceSelect.value : '',
            toothNum: row.querySelector('.tooth-number-input')?.value || '',
            quantity: row.querySelector('.quantity-input')?.value || 1,
            price: row.querySelector('.price-cell')?.innerText || '0',
            total: row.querySelector('.total-cell')?.innerText || '0',
            note: row.querySelector('.note-input')?.value || ''
        });
    });
    const examData = {
        patientName: 'Nguyễn Văn Hiển',
        patientId: 'BN001234',
        doctor: 'BS. Nguyễn Hoàng',
        date: new Date().toLocaleString('vi-VN'),
        symptoms: document.getElementById('symptoms')?.value || '',
        diagnosis: document.getElementById('diagnosis')?.value || '',
        prescription: document.getElementById('prescription')?.value || '',
        treatments: treatments
    };
    console.log('Dữ liệu khám bệnh:', examData);
    showToast('✅ Đã lưu thông tin thành công!', 3000);
    // Gửi AJAX nếu cần
}

// ========== IN ĐƠN THUỐC ==========
function printPrescription() {
    const prescription = document.getElementById('prescription')?.value || '';
    const patientName = 'Nguyễn Văn Hiển';
    const doctorName = 'BS. NGUYỄN HOÀNG';
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
    // Gắn sự kiện cho các dòng hiện có
    const existingRows = document.querySelectorAll('#treatmentBody tr');
    existingRows.forEach(row => {
        attachRowEvents(row);
        updateRowPrices(row);
    });
    attachDeleteEvents();
    
    // Gắn nút thêm dịch vụ
    const addBtn = document.getElementById('addServiceBtn');
    if (addBtn) addBtn.addEventListener('click', () => addNewRow(''));
    
    // Các nút chức năng
    const saveBtn = document.getElementById('saveBtn');
    if (saveBtn) saveBtn.addEventListener('click', saveExamination);
    
    const printBtn = document.getElementById('printBtn');
    if (printBtn) printBtn.addEventListener('click', printPrescription);
    
    const cancelBtn = document.getElementById('cancelBtn');
    const backBtn = document.getElementById('backBtn');
    if (cancelBtn) cancelBtn.addEventListener('click', handleBack);
    if (backBtn) backBtn.addEventListener('click', handleBack);
    
    const xrayBtn = document.getElementById('xrayBtn');
    if (xrayBtn) xrayBtn.addEventListener('click', () => showToast('📷 Chức năng X-Quang đang phát triển', 2000));
    
    // Khởi tạo sơ đồ răng
    initTeethMap();
    
    // Đặt dòng active mặc định là dòng đầu tiên
    const firstRow = document.querySelector('#treatmentBody tr');
    if (firstRow) {
        window.currentActiveRow = firstRow;
        const firstToothInput = firstRow.querySelector('.tooth-number-input');
        if (firstToothInput) firstToothInput.focus();
    }
}

// Chạy sau khi DOM tải xong
document.addEventListener('DOMContentLoaded', init);