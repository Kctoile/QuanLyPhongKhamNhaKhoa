package com.dentalclinic.controller;

import com.dentalclinic.utils.DBConnection;
import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.sql.Connection;

@WebServlet("/testdb")
public class TestDBServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            response.getWriter().println("KET NOI SQL SERVER THANH CONG");
        } else {
            response.getWriter().println("KET NOI THAT BAI");
        }
    }
}