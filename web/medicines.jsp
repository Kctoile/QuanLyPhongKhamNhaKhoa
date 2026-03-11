<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="css/medicines.css"/>
<div class="admin-layout">

    <%@ include file="admin_menu.jsp" %>

    <div class="admin-content">

        <div class="medicines-page">

            <div style="padding:20px;">
                <h2>QUẢN LÝ THUỐC</h2>

                <a href="add_medicine.jsp"
                   style="display:inline-block; margin-bottom:15px; padding:8px 15px; background-color:#4CAF50; color:white; text-decoration:none; border-radius:4px;">
                    + Thêm Thuốc Mới
                </a>

                <!-- Form Tìm Kiếm -->
                <form action="medicines" method="get" style="margin-bottom: 20px;">
                    <input type="text" name="keyword" value="${keyword}" placeholder="Tìm theo tên thuốc..."
                           style="padding: 8px; width: 250px;">
                    <button type="submit"
                            style="padding: 8px 15px; background-color: #008CBA; color: white; border: none; border-radius: 4px; cursor: pointer;">
                        Tìm kiếm
                    </button>
                    <c:if test="${not empty keyword}">
                        <a href="medicines" style="margin-left: 10px; text-decoration: none; color: red;">Xóa tìm
                            kiếm</a>
                        </c:if>
                </form>

                <table border="1" cellpadding="10" style="border-collapse: collapse; width: 100%;">
                    <tr style="background-color: #f2f2f2;">
                        <th>ID</th>
                        <th>Tên thuốc</th>
                        <th>Giá (VNĐ)</th>
                        <th>Số lượng tồn kho</th>
                        <th>Thao tác</th>
                    </tr>

                    <c:forEach var="m" items="${list}">
                        <tr>
                            <td>${m.medicineId}</td>
                            <td>${m.medicineName}</td>
                            <td>${m.price}</td>
                            <td>${m.stockQuantity}</td>
                            <td>
                                <a class="btn-edit"
                                   href="edit_medicine.jsp?id=${m.medicineId}&name=${m.medicineName}&price=${m.price}&stock=${m.stockQuantity}">
                                    Sửa
                                </a>

                                <a class="btn-delete"
                                   href="medicines?action=delete&id=${m.medicineId}"
                                   onclick="return confirm('Bạn có chắc chắn muốn xóa thuốc này?');">
                                    Xóa
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>

        </div>

    </div>

</div>