package com.dentalclinic.controller;

import com.dentalclinic.dao.UserDAO;
import com.dentalclinic.model.User;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/users")
public class UserManagementServlet extends HttpServlet {

    private boolean checkAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!checkAdmin(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        UserDAO dao = new UserDAO();

        if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            User user = dao.getUserById(id);
            request.setAttribute("user", user);
            request.setAttribute("formAction", "update");
            request.getRequestDispatcher("user_form.jsp").forward(request, response);
        } else if ("add".equals(action)) {
            request.setAttribute("user", new User());
            request.setAttribute("formAction", "add");
            request.getRequestDispatcher("user_form.jsp").forward(request, response);
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.deleteUser(id);
            response.sendRedirect("users");
        } else {
            List<User> list = dao.getAllUsers();

            String roleFilter = request.getParameter("role");
            if (roleFilter != null && !roleFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(roleFilter)) {
                if ("UNASSIGNED".equalsIgnoreCase(roleFilter)) {
                    list = list.stream()
                            .filter(u -> u.getRole() == null)
                            .collect(Collectors.toList());
                } else {
                    list = list.stream()
                            .filter(u -> u.getRole() != null && roleFilter.equalsIgnoreCase(u.getRole().getRoleName()))
                            .collect(Collectors.toList());
                }
            }

            request.setAttribute("currentRole", roleFilter != null ? roleFilter.toUpperCase() : "ALL");
            request.setAttribute("users", list);
            request.getRequestDispatcher("users.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!checkAdmin(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        UserDAO dao = new UserDAO();

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String dobStr = request.getParameter("dob");
        String address = request.getParameter("address");
        String roleIdStr = request.getParameter("roleId");
        Integer roleId = null;
        if (roleIdStr != null && !roleIdStr.trim().isEmpty()) {
            try {
                roleId = Integer.parseInt(roleIdStr);
            } catch (NumberFormatException e) {
                // Handle error if roleId is not a valid number, e.g., log it or send an error
                // response
                System.err.println("Invalid roleId format: " + roleIdStr);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid role ID format.");
                return;
            }
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setGender(gender);
        if (dobStr != null && !dobStr.isEmpty()) {
            user.setDob(Date.valueOf(dobStr));
        }
        user.setAddress(address);

        if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("userId"));
            user.setUserId(id);
            dao.updateUser(user, roleId);
        } else if ("add".equals(action)) {
            dao.addUser(user, roleId);
        }

        response.sendRedirect("users");
    }
}
