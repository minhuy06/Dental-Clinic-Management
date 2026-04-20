/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener("DOMContentLoaded", function() {
    const spark = document.querySelector('.spark');
    const robot = document.querySelector('.robot-repair');

    // Hàm tạo tia lửa điện nhấp nháy
    function createSpark() {
        // Cho hiện tia lửa
        spark.style.display = 'block';
        
        // Ngẫu nhiên vị trí xung quanh tay robot
        const randomX = Math.random() * 20 - 10;
        const randomY = Math.random() * 20 - 10;
        spark.style.transform = `translate(${randomX}px, ${randomY}px)`;

        // Ẩn đi sau một khoảng thời gian cực ngắn để tạo hiệu ứng chớp
        setTimeout(() => {
            spark.style.display = 'none';
        }, 50);
    }

    // Cứ mỗi 200ms-500ms lại nổ tia lửa một lần
    setInterval(() => {
        if(Math.random() > 0.3) { // Tỉ lệ xuất hiện 70%
            createSpark();
        }
    }, 150);

    // Hiệu ứng Parallax nhẹ khi di chuyển chuột
    document.addEventListener('mousemove', (e) => {
        const x = (window.innerWidth / 2 - e.pageX) / 30;
        const y = (window.innerHeight / 2 - e.pageY) / 30;
        
        robot.style.margin = `${y}px 0 0 ${x}px`;
    });
});
