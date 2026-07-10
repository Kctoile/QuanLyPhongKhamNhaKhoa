package com.dentalclinic.controller;

import com.dentalclinic.dao.UserDAO;
import com.dentalclinic.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/" + UserManagementServlet.REDIRECT_USERS)
public class UserManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(UserManagementServlet.class.getName());
    public static final String REDIRECT_USERS = "users";

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            response.sendRedirect(REDIRECT_USERS);
        } else {
            List<User> list = dao.getAllUsers();
            String roleFilter = request.getParameter("role");
            
            if (roleFilter != null && !roleFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(roleFilter)) {
                if ("UNASSIGNED".equalsIgnoreCase(roleFilter)) {
                    list = list.stream().filter(u -> u.getRole() == null).toList();
                } else {
                    list = list.stream().filter(u -> u.getRole() != null && roleFilter.equalsIgnoreCase(u.getRole().getRoleName())).toList();
                }
            }
            request.setAttribute("currentRole", roleFilter != null ? roleFilter.toUpperCase() : "ALL");
            request.setAttribute(REDIRECT_USERS, list);
            request.getRequestDispatcher(REDIRECT_USERS + ".jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!checkAdmin(request, response)) {
            return;
        }
        String action = request.getParameter("action");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        Date dateOfBirth = (dateOfBirthStr != null && !dateOfBirthStr.trim().isEmpty()) ? Date.valueOf(dateOfBirthStr) : null;
        String gender = request.getParameter("gender");
        String username = request.getParameter("username");
        String statusStr = request.getParameter("status");
        boolean status = "true".equalsIgnoreCase(statusStr);
        UserDAO dao = new UserDAO();

        String roleStr = request.getParameter("role");
        Integer roleId = (roleStr != null && !roleStr.isEmpty()) ? Integer.valueOf(roleStr) : null;

        if ("add".equals(action)) {
            String password = request.getParameter("password");
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setAddress(address);
            user.setDateOfBirth(dateOfBirth);
            user.setGender(gender);
            user.setUsername(username);
            user.setPassword(password);
            user.setStatus(status);
            dao.registerUser(user, roleId);
            response.sendRedirect(REDIRECT_USERS);
        } else if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            User user = dao.getUserById(id);
            if (user != null) {
                user.setFullName(fullName);
                user.setEmail(email);
                user.setPhone(phone);
                user.setAddress(address);
                user.setDateOfBirth(dateOfBirth);
                user.setGender(gender);
                user.setUsername(username);
                user.setStatus(status);
                String password = request.getParameter("password");
                if (password != null && !password.trim().isEmpty()) {
                    user.setPassword(password);
                }
                dao.updateUserAndRole(user, roleId);
            }
            response.sendRedirect(REDIRECT_USERS);
        } else {
            response.sendRedirect(REDIRECT_USERS);
        }
    }
}
