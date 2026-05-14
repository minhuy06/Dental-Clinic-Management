/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.controller;

import com.dentalclinic.model.TaiKhoan;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.HashSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kinhm
 */
@WebServlet("/PatientProfileServlet")
public class PatientProfileServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException,ServletException {
        String action = req.getParameter("action");
        
        if(action == null){
            action = "info";
        }
        Gson json = new GsonBuilder().create();
        TaiKhoan currentUser = (TaiKhoan) req.getSession().getAttribute("loggedInUser");
                
        if(currentUser != null){
            String userJsonString = json.toJson(currentUser);
                    
            req.setAttribute("hosoUserJson",userJsonString);
        }
        
        switch (action){
            case "info":{
                req.getRequestDispatcher("/patient/hoso.jsp").forward(req, res);
            } break;
            case "history":{
                req.getRequestDispatcher("/patient/hoso.jsp").forward(req, res);
            } break;
            case "password":{
                req.getRequestDispatcher("/patient/hoso.jsp").forward(req, res);
            } break;
            default: break;
        }
    }
}
