package com.dentalclinic.controller;

import com.dentalclinic.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/RecomputeDisplayOrder")
public class RecomputeDisplayOrderServlet extends HttpServlet {

    private final UserDAO dao = new UserDAO();

    private boolean checkAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login.jsp");
            return false;
        }
        String role = session.getAttribute("role").toString();
        if (!"ADMIN".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkAdmin(request, response))
            return;

        boolean ok = dao.recomputeDisplayOrderByIdAsc();
        if (ok) {
            response.sendRedirect("users?message=DisplayOrder+updated");
        } else {
            response.sendRedirect("users?error=DisplayOrder+update+failed");
        }
    }
}
