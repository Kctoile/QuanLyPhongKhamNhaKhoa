<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ page import="com.dentalclinic.dao.DichVuDAO, com.dentalclinic.dao.NguoiDungDAO" %>
                <%@ page import="com.dentalclinic.model.DichVu, com.dentalclinic.model.NguoiDung" %>
                    <%@ page import="java.util.List" %>
                        <% String keyword=request.getParameter("keyword"); DichVuDAO dichVuDAO=new DichVuDAO();
                            NguoiDungDAO nguoiDungDAO=new NguoiDungDAO(); List<DichVu> listDichVu;
                            List<NguoiDung> listBacSi;

                                if (keyword != null && !keyword.trim().isEmpty()) {
                                listDichVu = dichVuDAO.searchDichVu(keyword);
                                listBacSi = nguoiDungDAO.searchDoctors(keyword);
                                request.setAttribute("keyword", keyword);
                                } else {
                                listDichVu = dichVuDAO.getAll();
                                listBacSi = nguoiDungDAO.getDoctors();
                                }
                                request.setAttribute("listDichVu", listDichVu);
                                request.setAttribute("listBacSi", listBacSi);
                                %>
                                <!DOCTYPE html>
                                <html lang="vi">

                                <head>
                                    <meta charset="UTF-8">
                                    <title>Dental Clinic - Home</title>
                                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
                                        rel="stylesheet">
                                    <link
                                        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
                                        rel="stylesheet">
                                </head>

                                <body>

                                    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
                                        <div class="container">
                                            <a class="navbar-brand" href="${pageContext.request.contextPath}/"><i
                                                    class="fas fa-tooth"></i>
                                                Dental Clinic</a>
                                            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                                                data-bs-toggle="collapse" data-bs-target="#navbarNav">
                                                <span class="navbar-toggler-icon"></span>
                                            </button>
                                            <div class="collapse navbar-collapse" id="navbarNav">
                                                <ul class="navbar-nav me-auto">
                                                    <li class="nav-item"><a class="nav-link active"
                                                            href="${pageContext.request.contextPath}/">Trang chủ</a>
                                                    </li>
                                                </ul>
                                                <ul class="navbar-nav align-items-center">
                                                    <c:choose>
                                                        <c:when test="${not empty sessionScope.user}">
                                                            <li class="nav-item me-3"><span class="text-white">Xin chào,
                                                                    ${sessionScope.user.hoTen}
                                                                    (${sessionScope.role})</span></li>
                                                            <c:if test="${sessionScope.role == 'ADMIN'}">
                                                                <li class="nav-item"><a class="nav-link text-white"
                                                                        href="${pageContext.request.contextPath}/admin">Quản
                                                                        trị</a></li>
                                                            </c:if>
                                                            <c:if test="${sessionScope.role == 'DOCTOR'}">
                                                                <li class="nav-item"><a class="nav-link text-white"
                                                                        href="${pageContext.request.contextPath}/bacsi">Bác
                                                                        sĩ</a></li>
                                                            </c:if>
                                                            <c:if test="${sessionScope.role == 'STAFF'}">
                                                                <li class="nav-item"><a class="nav-link text-white"
                                                                        href="${pageContext.request.contextPath}/staff">Lễ
                                                                        tân</a></li>
                                                            </c:if>
                                                            <c:if test="${sessionScope.role == 'CUSTOMER'}">
                                                                <li class="nav-item"><a class="nav-link text-white"
                                                                        href="${pageContext.request.contextPath}/lichhen">Lịch
                                                                        hẹn của
                                                                        tôi</a></li>
                                                                <li class="nav-item"><a
                                                                        class="btn btn-warning text-dark fw-bold me-2"
                                                                        href="${pageContext.request.contextPath}/datlich.jsp">Đặt
                                                                        lịch
                                                                        khám</a></li>
                                                            </c:if>
                                                            <li class="nav-item"><a class="btn btn-outline-light ms-2"
                                                                    href="${pageContext.request.contextPath}/logout">Đăng
                                                                    xuất</a></li>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <li class="nav-item me-2"><a class="btn btn-outline-light"
                                                                    href="${pageContext.request.contextPath}/login.jsp">Đăng
                                                                    nhập</a></li>
                                                            <li class="nav-item"><a
                                                                    class="btn btn-light text-primary fw-bold px-4"
                                                                    href="${pageContext.request.contextPath}/register.jsp">Đăng
                                                                    ký</a></li>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </ul>
                                            </div>
                                        </div>
                                    </nav>

                                    <div class="container mt-4">
                                        <div class="row">
                                            <div class="col-12 text-center mb-5">
                                                <h1 class="display-4"><i class="fas fa-hospital-alt text-primary"></i>
                                                    Nha Khoa Nụ Cười</h1>
                                                <p class="lead text-muted">Chào mừng bạn đến với hệ thống quản lý phòng
                                                    khám nha khoa.</p>
                                            </div>

                                            <div class="col-md-6 mb-4">
                                                <div class="card shadow-sm h-100 border-0">
                                                    <div class="card-header bg-white border-0 text-center pt-4">
                                                        <h3 class="text-primary"><i class="fas fa-stethoscope"></i> Dịch
                                                            Vụ Của Chúng Tôi
                                                        </h3>
                                                    </div>
                                                    <div class="card-body">
                                                        <div class="list-group list-group-flush">
                                                            <c:forEach var="dv" items="${listDichVu}">
                                                                <div
                                                                    class="list-group-item d-flex justify-content-between align-items-center border-bottom py-3">
                                                                    <div>
                                                                        <h5 class="mb-1 text-dark fw-bold">${dv.tenDV}
                                                                        </h5>
                                                                        <small
                                                                            class="text-muted d-block">${dv.moTa}</small>
                                                                    </div>
                                                                    <div class="text-end">
                                                                        <span
                                                                            class="badge bg-success rounded-pill px-3 py-2 fs-6">
                                                                            <fmt:formatNumber value="${dv.gia}"
                                                                                type="currency" currencySymbol="VNĐ" />
                                                                        </span>
                                                                        <small class="text-muted d-block mt-1"><i
                                                                                class="far fa-clock"></i>
                                                                            ${dv.thoiGian} phút</small>
                                                                    </div>
                                                                </div>
                                                            </c:forEach>
                                                            <c:if test="${empty listDichVu}">
                                                                <div class="text-center text-muted p-4">Hiện tại chưa có
                                                                    dịch vụ nào.</div>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="col-md-6 mb-4">
                                                <div class="card shadow-sm h-100 border-0">
                                                    <div class="card-header bg-white border-0 text-center pt-4">
                                                        <h3 class="text-info"><i class="fas fa-user-md"></i> Đội Ngũ Bác
                                                            Sĩ</h3>
                                                    </div>
                                                    <div class="card-body">
                                                        <div class="row row-cols-1 row-cols-md-2 g-4">
                                                            <c:forEach var="bs" items="${listBacSi}">
                                                                <div class="col">
                                                                    <div
                                                                        class="card h-100 text-center border-light shadow-sm">
                                                                        <div class="card-body">
                                                                            <div class="mb-3">
                                                                                <i
                                                                                    class="fas fa-user-circle fa-4x text-secondary"></i>
                                                                            </div>
                                                                            <h5
                                                                                class="card-title fw-bold text-dark mb-1">
                                                                                Bs. ${bs.hoTen}
                                                                            </h5>
                                                                            <p class="card-text text-muted small"><i
                                                                                    class="fas fa-phone-alt"></i>
                                                                                ${bs.soDienThoai}</p>
                                                                            <p class="card-text text-muted small mb-0">
                                                                                <i class="fas fa-envelope"></i>
                                                                                ${bs.email}</p>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </c:forEach>
                                                        </div>
                                                        <c:if test="${empty listBacSi}">
                                                            <div class="text-center text-muted p-4">Hiện tại chưa có
                                                                thông tin bác sĩ.</div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <script
                                        src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
                                </body>

                                </html>