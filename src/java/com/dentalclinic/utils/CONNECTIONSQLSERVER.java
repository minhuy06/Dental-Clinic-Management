package com.dentalclinic.utils;

import java.sql.*;

public class CONNECTIONSQLSERVER implements ICONNECTIONDATA {
    private String UserName;
    private String PassWord;
    private String DataBaseName;
    private String ServerName;
    private String DriverClass;
    private Connection cnn = null;
    private String DriverURL;

    public CONNECTIONSQLSERVER() {
        try {
            this.UserName = "sa";
            this.PassWord = "123456";
            this.DataBaseName = "DbQuanLyNhaKhoa";
            this.ServerName = "DESKTOP-1UGPPMS";
            this.DriverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            this.DriverURL = "jdbc:sqlserver://" + ServerName + ":1433;"
                    + "databaseName=" + DataBaseName + ";user=" + UserName
                    + ";password=" + PassWord + ";encrypt=false;integratedSecurity=false;";
            Class.forName(DriverClass);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void open() {
        try {
            cnn = DriverManager.getConnection(DriverURL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (cnn != null && !cnn.isClosed()) {
                cnn.close();
            }
        } catch (SQLException ex) {
        }
    }

    @Override
    public ResultSet getResultSetSQL(String SQL) {
        try {
            open();
            Statement stm = cnn.createStatement();
            return stm.executeQuery(SQL);
        } catch (SQLException ex) {
        }
        return null;
    }

    @Override
    public ResultSet getResultSetStoredProcedure(String procedureName, Object[] param) {
        try {
            open();
            CallableStatement ps = cnn.prepareCall("{call " + procedureName + "}");
            if (param != null) {
                int i = 1;
                for (Object value : param) {
                    ps.setObject(i, value);
                    i++;
                }
            }
            return ps.executeQuery();
        } catch (SQLException ex) {
        }
        return null;
    }

    @Override
    public int executeUpdateSQL(String SQL) {
        try {
            open();
            int k;
            try (Statement stm = cnn.createStatement()) {
                k = stm.executeUpdate(SQL);
            }
            close();
            return k;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int ExecuteUpdatePrepared(String SQL, Object[] params) {
        try {
            open();
            PreparedStatement ps = cnn.prepareStatement(SQL);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            int k = ps.executeUpdate();
            ps.close();
            close();
            return k;
        } catch (SQLException e) {
        }
        return 0;
    }

    @Override
    public int executeStoredProcedure(String NameStoredProcedures, Object[] param) {
        try {
            open();
            int k;
            try (CallableStatement ps = cnn.prepareCall("{call " + NameStoredProcedures + "}")) {
                int i = 1;
                for (Object value : param) {
                    ps.setObject(i, value);
                    i++;
                }   k = ps.executeUpdate();
            }
            close();
            return k;
        } catch (SQLException e) {
        }
        return 0;
    }
}