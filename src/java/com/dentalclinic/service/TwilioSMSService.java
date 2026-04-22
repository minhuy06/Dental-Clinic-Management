package com.dentalclinic.service;

import com.twilio.Twilio;  // thư viện khởi tạo hệ thống
import com.twilio.rest.api.v2010.account.Message;  // Thư viện xử lý tin nhắn
import com.twilio.type.PhoneNumber;  // Xác thực định dạng SĐT
import java.util.Random;

public class TwilioSMSService {
    
    // Khai báo các hằng số lấy từ Twilio Dashboard
    public static final String ACCOUNT_SID = "TWILIO_ACCOUNT_SID";
    public static final String AUTH_TOKEN = "TWILIO_AUTH_TOKEN";
    public static final String TWILIO_NUMBER = "TWILIO_PHONE_NUMBER"; // Số Twilio cấp

    // Khởi tạo kết nối Twilio
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    // Hàm sinh mã OTP ngẫu nhiên 6 số
    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Hàm gửi tin nhắn
    public boolean sendOTP(String userPhoneNumber, String otpCode) {
        try {
            // Định dạng lại số điện thoại cho Việt Nam (ví dụ: 0912345678 -> +84912345678)
            if(userPhoneNumber.startsWith("0")) {
                userPhoneNumber = "+84" + userPhoneNumber.substring(1);
            }

            Message message = Message.creator(
                    new PhoneNumber(userPhoneNumber), // Số người nhận
                    new PhoneNumber(TWILIO_NUMBER),   // Số người gửi (Twilio)
                    "Nha khoa 5AE - Ma xac thuc OTP cua ban la: " + otpCode)  // Nội dung tin nhắn
            .create();

            System.out.println("Đã gửi SMS. SID: " + message.getSid());
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi gửi SMS: " + e.getMessage());
            return false;
        }
    }
}