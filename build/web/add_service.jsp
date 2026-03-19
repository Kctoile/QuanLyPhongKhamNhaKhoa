<%@ page pageEncoding="UTF-8" %>
<link rel="stylesheet" href="css/edit_medicine.css"/>
<div class="admin-layout">
    <%@ include file="admin_menu.jsp" %>
    <div class="admin-content">
        <h2>SỬA DỊCH VỤ</h2>
        <a href="services">← Quay lại danh sách</a>
        <hr>

        <form action="services" method="post" style="max-width: 500px;">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="serviceId" value="${param.id}">

            <div style="margin-bottom: 15px;">
                <label style="display:block; font-weight:bold; margin-bottom:5px;">Tên dịch vụ *</label>
                <input type="text" name="serviceName" value="${param.name}" required
                       style="width: 100%; padding: 8px; box-sizing: border-box;">
            </div>

            <div style="margin-bottom: 15px;">
                <label style="display:block; font-weight:bold; margin-bottom:5px;">Mô tả</label>
                <textarea name="description" rows="4"
                          style="width: 100%; padding: 8px; box-sizing: border-box;">${param.desc}</textarea>
            </div>

            <div style="margin-bottom: 15px;">
                <label style="display:block; font-weight:bold; margin-bottom:5px;">Giá (VNĐ) *</label>
                <input type="number" name="price" value="${param.price}" required
                       style="width: 100%; padding: 8px; box-sizing: border-box;" min="0" step="1000">
            </div>

            <div style="margin-bottom: 15px;">
                <label style="display:block; font-weight:bold; margin-bottom:5px;">Thời lượng dự kiến (phút)</label>
                <input type="number" name="durationMinutes" value="${param.duration}"
                       style="width: 100%; padding: 8px; box-sizing: border-box;" min="1" step="1">
            </div>

            <button type="submit"
                    style="padding: 10px 20px; background-color: #008CBA; color: white; border: none; border-radius: 4px; cursor: pointer;">
                Cập nhật
            </button>
        </form>
    </div>
</div>