package com.dentalclinic.utils;

import java.sql.ResultSet;

public interface ICONNECTIONDATA {
    
    void open() throws Exception;
    
    void close() throws Exception;
        
    int executeUpdateSQL(String sql) throws Exception;
    ResultSet getResultSetSQL(String sql) throws Exception;
    
    int executeStoredProcedure(String procedureName, Object[] params) throws Exception;
    ResultSet getResultSetStoredProcedure(String procedureName, Object[] params) throws Exception;
    
}