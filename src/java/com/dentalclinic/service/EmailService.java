package com.dentalclinic.service;

import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    
    // Thông tin tài khoản Gmail
    private static final String EMAIL_FROM = "1410huyvo@gmail.com";
    private static final String APP_PASSWORD = "orzpmqdmwldgvwvd";
    
    // Hàm sinh mã OTP ngẫu nhiên 6 số
    public String generateOTP(){
        Random ramdom = new Random();
        int otp = 100000 + ramdom.nextInt(900000);
        return String.valueOf(otp);
    }
    
    // Hàm gửi mail
    public boolean sendOTP(String toEmail, String otpCode){
        // Cấu hình thông số máy chủ SMTP của Google
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Kích hoạt bảo mật TLS
        
        // Đăng nhập vào Gmail
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, APP_PASSWORD);
            }
        });
        try{
            // Khởi tạo thư
            Message message = new MimeMessage(session);
            // Tên người gửi hiển thị là "Nha Khoa 5AE"
            message.setFrom(new InternetAddress(EMAIL_FROM, "Nha Khoa 5AE"));
            
            // Gửi đến email người dùng nhập trên form
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            
            // Tiêu đề email
            message.setSubject("Mã xác thực OTP đăng ký tài khoản");
            
            // Nội dung email
            message.setText("Chào bạn,\n\n"
                    + "Mã xác thực OTP để hoàn tất đăng ký tài khoản của bạn là: " + otpCode + "\n\n"
                    + "Mã này có hiệu lực trong vòng 60 giây. Vui lòng không chia sẻ mã này cho bất kỳ ai.\n\n"
                    + "Trân trọng,\nĐội ngũ Nha khoa 5AE.");
            
            // Phát lệnh gửi thư
            Transport.send(message);
            System.out.println("Đã gửi Email OTP thành công tới: " + toEmail);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi gửi Email OTP: " + e.getMessage());
            return false;
        }
    }
}
