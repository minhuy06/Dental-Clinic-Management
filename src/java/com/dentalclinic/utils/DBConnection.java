package com.dentalclinic.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String HOST     = "localhost";
    private static final String PORT     = "1433";
    private static final String DATABASE = "DbQuanLyNhaKhoa";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123456";

    private static final String URL =
        "jdbc:sqlserver://" + HOST + ":" + PORT + ";"
        + "databaseName=" + DATABASE + ";"
        + "encrypt=false;"
        + "trustServerCertificate=true;";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Không tìm thấy SQL Server JDBC Driver!", e);
        }
    }

    // Chặn không cho dùng lệnh new DBConnection()
    private DBConnection() {}

    // Hàm cung cấp kết nối cho tầng DAO sử dụng
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}