<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /**
     * Trang chủ - redirect dựa theo vai trò người dùng
     * ADMIN -> trang quản trị
     * Các role khác -> trang dịch vụ
     */
    String role = (String) session.getAttribute("role");

    if ("ADMIN".equals(role)) {
        // Admin: chuyển đến dashboard quản trị
        response.sendRedirect(request.getContextPath() + "/admin");
    } else {
        // User thường: chuyển đến trang dịch vụ
        response.sendRedirect(request.getContextPath() + "/dichvu");
    }
%>
