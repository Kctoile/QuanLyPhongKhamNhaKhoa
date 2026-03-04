<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.dentalclinic.model.DichVu" %>

<%
    DichVu dv = (DichVu) request.getAttribute("dichvu");
%>

<h2>Cập nhật dịch vụ</h2>

<form action="dichvu" method="post">

    <input type="hidden" name="action" value="update"/>
    <input type="hidden" name="maDV" value="<%= dv.getMaDV() %>"/>

    Tên dịch vụ:<br>
    <input type="text" name="tenDV" value="<%= dv.getTenDV() %>" required/><br><br>

    Mô tả:<br>
    <input type="text" name="moTa" value="<%= dv.getMoTa() %>" required/><br><br>

    Giá:<br>
    <input type="number" name="gia" value="<%= dv.getGia() %>" required/><br><br>

    Thời gian thực hiện:<br>
    <input type="number" name="thoiGian" value="<%= dv.getThoiGian() %>" required/><br><br>

    <input type="submit" value="Cập nhật"/>
</form>

<br>
<a href="dichvu">Quay lại danh sách</a>