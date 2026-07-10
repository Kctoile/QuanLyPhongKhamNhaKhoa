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

    private static final String USERS = "users";

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
        UserDAO dao = new UserDAO();

        if ("edit".equals(action)) {
            handleEdit(request, response, dao);
            return;
        }
        if ("add".equals(action)) {
            request.setAttribute("user", new User());
            request.setAttribute("formAction", "add");
            request.getRequestDispatcher("user_form.jsp").forward(request, response);
            return;
        }
        if ("delete".equals(action)) {
            dao.deleteUser(Integer.parseInt(request.getParameter("id")));
            response.sendRedirect(USERS);
            return;
        }
        handleList(request, response, dao);
    }

    private void handleEdit(HttpServletRequest request, HttpServletResponse response, UserDAO dao)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("user", dao.getUserById(id));
        request.setAttribute("formAction", "update");
        request.getRequestDispatcher("user_form.jsp").forward(request, response);
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response, UserDAO dao)
            throws ServletException, IOException {
        List<User> list = dao.getAllUsers();
        String roleFilter = request.getParameter("role");

        if (roleFilter != null && !roleFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(roleFilter)) {
            list = filterByRole(list, roleFilter);
        }

        request.setAttribute("currentRole", roleFilter != null ? roleFilter.toUpperCase() : "ALL");
        request.setAttribute(USERS, list);
        request.getRequestDispatcher("users.jsp").forward(request, response);
    }

    private List<User> filterByRole(List<User> list, String roleFilter) {
        if ("UNASSIGNED".equalsIgnoreCase(roleFilter)) {
            return list.stream().filter(u -> u.getRole() == null).collect(Collectors.toList());
        }
        return list.stream()
                .filter(u -> u.getRole() != null && roleFilter.equalsIgnoreCase(u.getRole().getRoleName()))
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
        User user = buildUserFromRequest(request);

        if ("update".equals(action)) {
            user.setUserId(Integer.parseInt(request.getParameter("userId")));
            dao.updateUser(user, user.getRoleId());
        } else if ("add".equals(action)) {
            dao.addUser(user, user.getRoleId());
        }
        response.sendRedirect(USERS);
    }

    private User buildUserFromRequest(HttpServletRequest request) {
        String roleIdStr = request.getParameter("roleId");
        User user = new User();
        user.setFullName(request.getParameter("fullName"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        user.setPhone(request.getParameter("phone"));
        user.setGender(request.getParameter("gender"));
        user.setAddress(request.getParameter("address"));

        String dobStr = request.getParameter("dob");
        if (dobStr != null && !dobStr.isEmpty()) {
            user.setDob(Date.valueOf(dobStr));
        }

        if (roleIdStr != null && !roleIdStr.trim().isEmpty()) {
            try {
                user.setRoleId(Integer.parseInt(roleIdStr));
            } catch (NumberFormatException e) {
                System.err.println("Invalid roleId format: " + roleIdStr);
            }
        }
        return user;
    }
}
