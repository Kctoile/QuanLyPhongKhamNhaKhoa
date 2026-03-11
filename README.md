# QuanLyPhongKhamNhaKhoa

Hệ thống quản lý phòng khám nha khoa - Ứng dụng web Java JSP/Servlet.

---

## Công nghệ sử dụng

- **Backend:** Java, JSP, Servlet (Jakarta EE)
- **Database:** Microsoft SQL Server
- **Build:** Apache Ant (NetBeans)
- **Server:** Apache Tomcat

---

## Yêu cầu hệ thống

- JDK 17 trở lên
- Apache Tomcat 10+ (hoặc tương thích Jakarta EE)
- Microsoft SQL Server
- NetBeans IDE (khuyến nghị) hoặc IDE hỗ trợ Java web

---

## Cấu trúc project

```
QuanLyPhongKhamNhaKhoa/
├── src/java/com/dentalclinic/
│   ├── controller/     # Servlet xử lý request
│   ├── dao/            # Data Access Object
│   ├── model/          # Model (entity)
│   └── utils/          # Tiện ích (DBConnection...)
├── web/                # JSP, static files
├── nbproject/          # Cấu hình NetBeans
├── build/              # Output build
└── build.xml           # Ant build script
```

---

## Cài đặt và chạy

### 1. Clone project

```bash
git clone https://github.com/Kctoile/QuanLyPhongKhamNhaKhoa.git
cd QuanLyPhongKhamNhaKhoa
```

### 2. Cấu hình Database

- Chạy script SQL tạo database `PhongKhamNhaKhoa` (file SQL đính kèm hoặc trong tài liệu)
- Đảm bảo SQL Server chạy ở `localhost:1433`
- Mặc định kết nối: user `sa`, password `123`

**Tùy biến (biến môi trường):**

- `DB_USER` – tên đăng nhập SQL Server
- `DB_PASS` – mật khẩu SQL Server

### 3. Cấu hình Tomcat

- Cài đặt Tomcat 10+ và cấu hình trong NetBeans
- Context path: `/phongkhamnhakhoa`

### 4. Build và chạy

**NetBeans:**

- Mở project → chuột phải → **Run**
- Ứng dụng chạy tại: `http://localhost:8080/phongkhamnhakhoa/`

**Dòng lệnh (Ant):**

```bash
ant
ant run
```

---

## Tài khoản mẫu

| Vai trò   | Email              | Mật khẩu |
|-----------|--------------------|----------|
| Admin     | admin@gmail.com    | admin    |
| Bác sĩ    | doctor1@gmail.com  | 123      |
| Nhân viên | domixue@gmail.com  | 123      |
| Khách hàng| hung@gmail.com     | 123      |

---

## Chức năng chính

- **Admin:** Dashboard, quản lý người dùng, dịch vụ, thuốc, lịch hẹn, thanh toán
- **Bác sĩ:** Trang bác sĩ (doctor.jsp)
- **Nhân viên:** Trang lễ tân (staff.jsp)
- **Khách hàng:** Xem dịch vụ, đặt lịch hẹn

---

## Đóng góp

1. Fork repository
2. Tạo branch mới (`git checkout -b feature/TenChucNang`)
3. Commit thay đổi (`git commit -m "Thêm chức năng X"`)
4. Push lên branch (`git push origin feature/TenChucNang`)
5. Tạo Pull Request

---

## License

Dự án nhóm - Sử dụng cho mục đích học tập.
