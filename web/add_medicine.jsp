<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ include file="admin_menu.jsp" %>

        <div style="margin-left: 240px; padding: 20px;">
            <h2>THÊM THUỐC MỚI</h2>
            <a href="medicines">← Quay lại danh sách</a>
            <hr>

            <form action="medicines" method="post" style="max-width: 500px;">
                <input type="hidden" name="action" value="add">

                <div style="margin-bottom: 15px;">
                    <label style="display:block; font-weight:bold; margin-bottom:5px;">Tên thuốc *</label>
                    <input type="text" name="medicineName" required
                        style="width: 100%; padding: 8px; box-sizing: border-box;">
                </div>

                <div style="margin-bottom: 15px;">
                    <label style="display:block; font-weight:bold; margin-bottom:5px;">Giá (VNĐ) *</label>
                    <input type="number" name="price" required
                        style="width: 100%; padding: 8px; box-sizing: border-box;" min="0" step="0.01">
                </div>

                <div style="margin-bottom: 15px;">
                    <label style="display:block; font-weight:bold; margin-bottom:5px;">Số lượng tồn *</label>
                    <input type="number" name="stockQuantity" required
                        style="width: 100%; padding: 8px; box-sizing: border-box;" min="0" step="1">
                </div>

                <button type="submit"
                    style="padding: 10px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer;">
                    Thêm Thuốc
                </button>
            </form>
        </div>