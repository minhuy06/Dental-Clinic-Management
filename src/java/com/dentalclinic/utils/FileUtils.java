package com.dentalclinic.utils;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtils {
    public static void saveToFile(String path, String fileName, byte[] data) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs(); 
        }
        
        File file = new File(path + File.separator + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        } catch (Exception e) {
            System.err.println("Lỗi lưu file tại: " + path + " - Chi tiết: " + e.getMessage());
        }
    }
}
