# 🏥 DenCli - Quản Lý Phòng Khám Nha Khoa

Hệ thống quản lý phòng khám nha khoa hiện đại - Ứng dụng web được xây dựng bằng Java JSP/Servlet.

> 🔗 **Live Demo:** [https://dencli.onrender.com/phongkhamnhakhoa/](https://dencli.onrender.com/phongkhamnhakhoa/)

Hệ thống có 4 role: Admin, Bác sĩ (Doctor), Nhân viên lễ tân (Staff), Khách hàng (Customer).

---

## 🛠️ Công Nghệ Sử Dụng

| Thành phần | Công nghệ |
|-----------|-----------|
| **Backend** | Java, JSP, Servlet (Jakarta EE 10) |
| **Database** | PostgreSQL |
| **Build Tool** | Apache Maven |
| **Server** | Apache Tomcat 10.1 |
| **Containerization** | Docker (multi-stage build) |
| **Deployment** | Render Cloud |

---

## 📋 Yêu Cầu Hệ Thống

- ☕ JDK 17 trở lên
- 📦 Apache Maven 3.8+
- 🐱 Apache Tomcat 10.1+ (tương thích Jakarta EE)
- 🗄️ PostgreSQL
- 🐳 Docker (tuỳ chọn, nếu sử dụng container)
- 💻 IDE hỗ trợ Maven (IntelliJ IDEA, Eclipse, VS Code, NetBeans, v.v.)

---

## 📁 Cấu Trúc Project

```
DenCli/
├── src/
│   └── main/
│       ├── java/com/dentalclinic/
│       │   ├── controller/          # Servlet xử lý request
│       │   ├── dao/                 # Data Access Object
│       │   ├── model/               # Model (entity)
│       │   └── utils/               # Tiện ích (DBConnection, ...)
│       └── webapp/                  # JSP, static files, WEB-INF
├── db_init_postgresql.sql           # Script khởi tạo database PostgreSQL
├── Dockerfile                       # Multi-stage build (Maven + Tomcat)
├── pom.xml                          # Cấu hình Maven
└── target/                          # Output build (WAR)
```

---

## 🚀 Cài Đặt Local (Developer)

### Yêu Cầu
- JDK 17+, Maven 3.8+, PostgreSQL, Tomcat 10.1

### Các Bước

```bash
git clone -b postgresql https://github.com/Kctoile/DenCli.git
cd DenCli
psql -U postgres -d phongkhamnhakhoa -f db_init_postgresql.sql
mvn clean package -Dmaven.test.skip=true
cp target/QuanLyPhongKhamNhaKhoa.war $TOMCAT_HOME/webapps/
```

Set biến môi trường `DB_URL`, `DB_USER`, `DB_PASS` trước khi chạy Tomcat.

### Docker

```bash
docker build -t dencli .
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/phongkhamnhakhoa \
  -e DB_USER=postgres \
  -e DB_PASS=your_password \
  dencli
```

### Deploy (Render)

Push lên GitHub → Render tự động build Dockerfile và deploy.

---

## 🔒 Security Improvements

Project đã được phân tích và fix **18 security bugs** từ static analysis report:

| Priority | Loại | Số lượng |
|----------|------|---------|
| **P1 - Critical** | Auth bypass, XSS Stored | 4 |
| **P2 - High** | Missing role check, IDOR, XSS | 8 |
| **P3 - Medium** | Silent fail, reset link leak, v.v. | 6 |

### Kỹ Thuật Đã Áp Dụng

- **Session & role validation** ở tất cả JSP/Servlet
- **fn:escapeXml()** chống XSS
- **IDOR protection** (kiểm tra quyền sở hữu dữ liệu)
- **PreparedStatement** chống SQL injection
- **Transaction SERIALIZABLE** chống race condition

---

## 👤 Tài Khoản Mẫu

| Vai Trò | Email | Mật Khẩu |
|--------|-------|----------|
| **Admin** | admin@dental.com | admin |
| **Bác Sĩ** | doctor1@dental.com | 123 |
| **Nhân Viên** | domixue@gmail.com | 123 |
| **Khách Hàng** | hung@gmail.com | 123 |

---

## ⚙️ Chức Năng Chính

### 👨‍💼 Admin
- Dashboard quản lý tổng thể
- Quản lý người dùng
- Quản lý dịch vụ & thuốc
- Quản lý lịch hẹn
- Xử lý thanh toán

### 👨‍⚕️ Bác Sĩ
- Xem lịch hẹn của bệnh nhân
- Ghi chú và cập nhật kết quả khám

### 👨‍💻 Nhân Viên Lễ Tân
- Quản lý bệnh nhân
- Đặt lịch hẹn
- Quản lý thanh toán

### 🙋 Khách Hàng
- Xem danh sách dịch vụ
- Đặt lịch hẹn
- Xem lịch sử

---

## 🤝 Hướng Dẫn Đóng Góp

1. **Fork** repository
2. **Tạo branch** mới cho chức năng: `git checkout -b feature/TenChucNang`
3. **Commit** thay đổi: `git commit -m "Thêm chức năng X"`
4. **Push** lên branch: `git push origin feature/TenChucNang`
5. **Tạo Pull Request** và mô tả chi tiết thay đổi

---

## 📄 License

Dự án cá nhân - Sử dụng cho mục đích học tập.

---

**Tác giả:** Kc Toile  
**Repository:** [GitHub](https://github.com/Kctoile/DenCli)
