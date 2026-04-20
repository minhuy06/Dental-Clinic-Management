/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import java.io.IOException;
import java.net.URL;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/*") // Áp dụng cho mọi đường dẫn
public class NotFoundFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getServletPath();

        // 1. Loại trừ các file tĩnh (css, js, images) vì tụi này thường nằm trong assets
        if (path.startsWith("/assets/") || path.contains(".")) {
            // Nếu là file có đuôi mở rộng (như .css, .png) thì cho qua để Tomcat tự xử lý
            chain.doFilter(request, response);
            return;
        }

        // 2. KIỂM TRA TÀI NGUYÊN CÓ TỒN TẠI KHÔNG
        // getResource() sẽ trả về null nếu không tìm thấy file hoặc Servlet tương ứng
        ServletContext context = req.getServletContext();
        URL resourceUrl = context.getResource(path);

        // Danh sách các "ngoại lệ" (các đường dẫn Servlet bro đã map)
        boolean isServlet = isRegisteredServlet(path);

        if (resourceUrl == null && !isServlet) {
            // KHÔNG TÌM THẤY TRANG -> Cho Robot ra hàn xì ngay!
            res.sendRedirect(req.getContextPath() + "/error/error.jsp");
            return;
        }

        chain.doFilter(request, response);
    }

    // Hàm phụ để kiểm tra xem đường dẫn có phải là một Servlet bro đã đặt tên không
    private boolean isRegisteredServlet(String path) {
        // Bro liệt kê các URL mapping của Servlet vào đây
        String[] servlets = {
            "/account/login", 
            "/hospitalmanager/receptionM", 
            "/logout",
            "/doctorM",
            "/trangchu/trang-chu"
        };
        for (String s : servlets) {
            if (s.equals(path)) return true;
        }
        return false;
    }
}
