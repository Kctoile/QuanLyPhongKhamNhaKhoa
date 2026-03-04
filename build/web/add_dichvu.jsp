<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- 
    Trang hiển thị form thêm dịch vụ mới
    Chỉ ADMIN mới được truy cập
-->

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Thêm dịch vụ</title>
    </head>
    <body>

        <h2>Thêm dịch vụ mới</h2>

        <!-- 
            Form gửi dữ liệu về Servlet:
            action="dichvu"
            method="post"
        -->
        <form action="dichvu" method="post">

            <!-- action = insert để servlet biết đang thêm mới -->
            <input type="hidden" name="action" value="insert">

            <!-- Tên dịch vụ -->
            <label>Tên dịch vụ:</label><br>
            <input type="text" name="ten" required><br><br>

            <!-- Mô tả -->
            <label>Mô tả:</label><br>
            <textarea name="moTa" required></textarea><br><br>

            <!-- Giá -->
            <label>Giá (VND):</label><br>
            <input type="number" name="gia" required><br><br>

            <!-- Thời gian thực hiện -->
            <label>Thời gian (phút):</label><br>
            <input type="number" name="thoiGian" required><br><br>

            <!-- Nút lưu -->
            <button type="submit">Lưu</button>
        </form>

        <br>

        <!-- Link quay lại -->
        <a href="dichvu">← Quay lại danh sách</a>

    </body>
</html>