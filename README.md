# 🏥 DenCli - Quản Lý Phòng Khám Nha Khoa

Hệ thống quản lý phòng khám nha khoa hiện đại - Ứng dụng web được xây dựng bằng Java JSP/Servlet.

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

## 🚀 Cài Đặt và Chạy

### 1️⃣ Clone Project

```bash
git clone -b postgresql https://github.com/Kctoile/DenCli.git
cd DenCli
```

### 2️⃣ Cấu Hình Database PostgreSQL

**Bước 1:** Cài đặt PostgreSQL và tạo database

**Bước 2:** Chạy script khởi tạo database:

```bash
psql -U <username> -d <database_name> -f db_init_postgresql.sql
```

**Bước 3:** Cấu hình biến môi trường:

| Biến Môi Trường | Mô Tả | Ví Dụ |
|---|---|---|
| `DB_URL` | Chuỗi kết nối JDBC PostgreSQL | `jdbc:postgresql://localhost:5432/phongkhamnhakhoa` |
| `DB_USER` | Tên đăng nhập PostgreSQL | `postgres` |
| `DB_PASS` | Mật khẩu PostgreSQL | `your_password` |

### 3️⃣ Build Project Bằng Maven

```bash
mvn clean package -Dmaven.test.skip=true
```

**Output:** `target/QuanLyPhongKhamNhaKhoa.war`

### 4️⃣ Deploy Trên Tomcat (Local)

```bash
# Copy WAR vào thư mục webapps của Tomcat
cp target/QuanLyPhongKhamNhaKhoa.war $TOMCAT_HOME/webapps/

# Khởi động Tomcat
$TOMCAT_HOME/bin/startup.sh
```

**Truy cập ứng dụng:** http://localhost:8080/QuanLyPhongKhamNhaKhoa/

---

## 🐳 Build và Deploy Bằng Docker

### Multi-stage Build

- **Build Stage:** Sử dụng Maven image để biên dịch project
- **Runtime Stage:** Sử dụng Tomcat image để chạy WAR

### Build Docker Image

```bash
docker build -t dencli .
```

### Chạy Docker Container

```bash
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://<host>:5432/<database> \
  -e DB_USER=<username> \
  -e DB_PASS=<password> \
  dencli
```

**Truy cập ứng dụng:** http://localhost:8080/phongkhamnhakhoa/

---

## ☁️ Triển Khai Trên Render Cloud

**URL Production:** https://dencli.onrender.com/phongkhamnhakhoa/

**Các bước cấu hình:**

1. Đặt biến môi trường `DB_URL`, `DB_USER`, `DB_PASS` trong phần **Environment** trên Render
2. Render tự động build lại Docker image từ Dockerfile khi có thay đổi trên branch triển khai
3. Service sẽ tự động redeploy sau khi build hoàn tất

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
