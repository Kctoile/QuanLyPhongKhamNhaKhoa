<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.dentalclinic.model.Thuoc" %>
<%@ include file="admin_menu.jsp" %>

<%
    Thuoc t = (Thuoc) request.getAttribute("thuoc");
    if (t == null) {
        response.sendRedirect("thuoc");
        return;
    }
%>

<h2>Cập nhật thuốc</h2>
<a href="admin">← Quay lại Dashboard</a> | <a href="thuoc">← Danh sách thuốc</a>
<hr>

<form action="thuoc" method="post">
    <input type="hidden" name="action" value="update"/>
    <input type="hidden" name="maThuoc" value="<%= t.getMaThuoc() %>"/>

    <label>Tên thuốc:</label><br>
    <input type="text" name="tenThuoc" value="<%= t.getTenThuoc() %>" required/><br><br>

    <label>Đơn giá (VNĐ):</label><br>
    <input type="number" name="donGia" step="0.01" min="0" value="<%= t.getDonGia() %>" required/><br><br>

    <label>Số lượng tồn:</label><br>
    <input type="number" name="soLuongTon" value="<%= t.getSoLuongTon() %>" min="0" required/><br><br>

    <input type="submit" value="Cập nhật"/>
</form>
