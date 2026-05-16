(function () {
    function updateDoctorHeaderDateTime() {
        var el = document.getElementById('currentDateTime');
        if (!el) return;
        var now = new Date();
        var dateStr = now.toLocaleDateString('vi-VN', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
        var timeStr = now.toLocaleTimeString('vi-VN');
        el.innerHTML = '<i class="fa-regular fa-calendar"></i> ' + dateStr + ' | ' + timeStr;
    }

    updateDoctorHeaderDateTime();
    setInterval(updateDoctorHeaderDateTime, 1000);
})();
