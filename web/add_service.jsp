<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ include file="admin_menu.jsp" %>

        <div style="margin-left: 240px; padding: 20px;">
            <h2>THÊM DỊCH VỤ MỚI</h2>
            <a href="services">← Quay lại danh sách</a>
            <hr>

            <form action="services" method="post" style="max-width: 500px;">
                <input type="hidden" name="action" value="add">

                <div style="margin-bottom: 15px;">
                    <label style="display:block; font-weight:bold; margin-bottom:5px;">Tên dịch vụ *</label>
                    <input type="text" name="serviceName" required
                        style="width: 100%; padding: 8px; box-sizing: border-box;">
                </div>

                <div style="margin-bottom: 15px;">
                    <label style="display:block; font-weight:bold; margin-bottom:5px;">Mô tả</label>
                    <textarea name="description" rows="4"
                        style="width: 100%; padding: 8px; box-sizing: border-box;"></textarea>
                </div>

                <div style="margin-bottom: 15px;">
                    <label style="display:block; font-weight:bold; margin-bottom:5px;">Giá (VNĐ) *</label>
                    <input type="number" name="price" required
                        style="width: 100%; padding: 8px; box-sizing: border-box;" min="0" step="0.01">
                </div>

                <div style="margin-bottom: 15px;">
                    <label style="display:block; font-weight:bold; margin-bottom:5px;">Thời lượng dự kiến (phút)</label>
                    <input type="number" name="durationMinutes"
                        style="width: 100%; padding: 8px; box-sizing: border-box;" min="1" step="1">
                </div>

                <button type="submit"
                    style="padding: 10px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer;">
                    Thêm Dịch Vụ
                </button>
            </form>
        </div>