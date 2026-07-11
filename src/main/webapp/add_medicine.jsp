<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Thêm Thuốc</title>
    </head>
    <body>
        <form action="MedicineServlet" method="post">
            <input type="hidden" name="action" value="add"/>

            <!-- Fix: thêm id cho input và for cho label — SonarCloud accessibility -->
            <div>
                <label for="medicineName">Tên thuốc:</label>
                <input type="text" id="medicineName" name="name" required/>
            </div>
            <div>
                <label for="medicineUnit">Đơn vị:</label>
                <input type="text" id="medicineUnit" name="unit"/>
            </div>
            <div>
                <label for="medicinePrice">Giá:</label>
                <input type="number" id="medicinePrice" name="price" step="0.01"/>
            </div>
            <div>
                <label for="medicineDescription">Mô tả:</label>
                <textarea id="medicineDescription" name="description" rows="3"></textarea>
            </div>
            <div>
                <button type="submit">Thêm</button>
                <a href="MedicineServlet">Quay lại</a>
            </div>
        </form>
    </body>
</html>
