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
import java.util.stream.Collectors;

@WebServlet("/users")
public class UserManagementServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(UserManagementServlet.class.getName());
    private static final String REDIRECT_USERS = "users";
    private static final String PAGE_USER_FORM = "user_form.jsp";
    private static final String PAGE_USERS = "users.jsp";

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkAdmin(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            handleEdit(request, response);
        } else if ("add".equals(action)) {
            handleAdd(request, response);
        } else if ("delete".equals(action)) {
            handleDelete(request, response);
        } else {
            handleList(request, response);
        }
    }

    private void handleEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO dao = new UserDAO();
        int id = Integer.parseInt(request.getParameter("id"));
        User user = dao.getUserById(id);
        request.setAttribute("user", user);
        request.setAttribute("formAction", "update");
        request.getRequestDispatcher(PAGE_USER_FORM).forward(request, response);
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("user", new User());
        request.setAttribute("formAction", "add");
        request.getRequestDispatcher(PAGE_USER_FORM).forward(request, response);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        UserDAO dao = new UserDAO();
        int id = Integer.parseInt(request.getParameter("id"));
        dao.deleteUser(id);
        response.sendRedirect(REDIRECT_USERS);
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO dao = new UserDAO();
        List<User> list = dao.getAllUsers();
        String roleFilter = request.getParameter("role");

        if (roleFilter != null && !roleFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(roleFilter)) {
            list = filterUsersByRole(list, roleFilter);
        }

        request.setAttribute("currentRole", roleFilter != null ? roleFilter.toUpperCase() : "ALL");
        request.setAttribute("users", list);
        request.getRequestDispatcher(PAGE_USERS).forward(request, response);
    }

    private List<User> filterUsersByRole(List<User> list, String roleFilter) {
        if ("UNASSIGNED".equalsIgnoreCase(roleFilter)) {
            return list.stream()
                    .filter(u -> u.getRole() == null)
                    .collect(Collectors.toList());
        }
        return list.stream()
                .filter(u -> u.getRole() != null && roleFilter.equalsIgnoreCase(u.getRole()))
                .collect(Collectors.toList());
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
                LOGGER.log(Level.WARNING, "Invalid roleId format: {0}", roleIdStr);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid role ID format.");
                return;
            }
        } else {
            LOGGER.log(Level.WARNING, "roleId is missing or empty.");
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
        if (roleId != null) {
            user.setRoleId(roleId);
        }

        if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("userId"));
            user.setUserId(id);
            dao.updateUser(user, roleId);
        } else if ("add".equals(action)) {
            dao.addUser(user, roleId);
        }
        response.sendRedirect(REDIRECT_USERS);
    }
}
