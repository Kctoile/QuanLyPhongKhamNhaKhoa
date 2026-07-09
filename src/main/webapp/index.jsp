<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.dentalclinic.dao.ServiceDAO, com.dentalclinic.dao.UserDAO" %>
<%@ page import="com.dentalclinic.model.Service, com.dentalclinic.model.User" %>
<%@ page import="java.util.List" %>
<% String keyword = request.getParameter("keyword");
    ServiceDAO serviceDAO = new ServiceDAO();
    UserDAO userDAO = new UserDAO();
    com.dentalclinic.dao.ClinicConfigDAO configDAO = new com.dentalclinic.dao.ClinicConfigDAO();

    List<Service> listDichVu;
    List<User> listBacSi;
                                
    com.dentalclinic.model.ClinicConfig clinicConfig = configDAO.getConfig();
    request.setAttribute("clinicConfig", clinicConfig);

    if (keyword != null && !keyword.trim().isEmpty()) {
    listDichVu = serviceDAO.searchServices(keyword);
    listBacSi = userDAO.searchDoctors(keyword);
    request.setAttribute("keyword", keyword);
    } else {
    listDichVu = serviceDAO.getAll();
    listBacSi = userDAO.getDoctors();
    }
    request.setAttribute("listDichVu", listDichVu);
    request.setAttribute("listBacSi", listBacSi);
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Phòng khám nha khoa uy tín - Đặt lịch khám, xem dịch vụ và đội ngũ bác sĩ chuyên nghiệp.">
    <title>Dental Clinic - Phòng Khám Nha Khoa</title>
    <link rel="stylesheet" href="css/global.css">
    <style>
        /* ── Hero ── */
        .hero {
            background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
            color: #fff;
            padding: 72px var(--space-6) 80px;
            text-align: center;
            position: relative;
            overflow: hidden;
        }
        .hero::before {
            content: '';
            position: absolute;
            inset: 0;
            background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.04'%3E%3Ccircle cx='30' cy='30' r='20'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
        }
        .hero > * { position: relative; }
        .hero-icon { font-size: 56px; margin-bottom: var(--space-4); display: block; }
        .hero h1 {
            font-size: clamp(28px, 5vw, 48px);
            font-weight: 700;
            color: #fff;
            margin-bottom: var(--space-3);
        }
        .hero p { font-size: 18px; opacity: .85; max-width: 520px; margin: 0 auto var(--space-6); }
        .hero-actions { display: flex; gap: var(--space-3); justify-content: center; flex-wrap: wrap; }

        /* ── Clinic Info ── */
        .clinic-info-bar {
            background: var(--color-surface);
            border-bottom: 1px solid var(--color-border);
            padding: var(--space-4) var(--space-6);
            display: flex;
            align-items: center;
            justify-content: center;
            gap: var(--space-8);
            flex-wrap: wrap;
            font-size: 14px;
        }
        .info-item { display: flex; align-items: center; gap: var(--space-2); color: var(--color-muted-text); }
        .info-item svg { width: 16px; height: 16px; color: var(--color-primary); flex-shrink: 0; }
        .info-item strong { color: var(--color-foreground); }

        /* ── Search bar ── */
        .search-bar {
            max-width: 540px;
            margin: 0 auto var(--space-8);
            display: flex;
            gap: var(--space-2);
        }
        .search-bar input {
            flex: 1;
            padding: 11px 16px;
            border: 1.5px solid var(--color-border);
            border-radius: var(--radius-sm);
            font-family: var(--font-body);
            font-size: 14px;
            background: var(--color-surface);
            color: var(--color-foreground);
            transition: border-color var(--transition), box-shadow var(--transition);
        }
        .search-bar input:focus {
            outline: none;
            border-color: var(--color-secondary);
            box-shadow: 0 0 0 3px rgba(20,184,166,.2);
        }

        /* ── Service list ── */
        .service-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 14px 0;
            border-bottom: 1px solid var(--color-muted);
            gap: var(--space-4);
        }
        .service-item:last-child { border-bottom: none; }
        .service-name { font-weight: 600; font-size: 15px; color: var(--color-foreground); }
        .service-desc { font-size: 13px; color: var(--color-muted-text); margin-top: 2px; }
        .service-price {
            font-weight: 700;
            color: var(--color-primary);
            font-size: 14px;
            white-space: nowrap;
        }
        .service-duration { font-size: 12px; color: var(--color-muted-text); margin-top: 2px; text-align: right; }

        /* ── Doctor card ── */
        .doctor-card {
            background: var(--color-surface);
            border: 1px solid var(--color-border);
            border-radius: var(--radius-md);
            padding: var(--space-5);
            text-align: center;
            transition: box-shadow var(--transition), transform var(--transition);
        }
        .doctor-card:hover { box-shadow: var(--shadow-md); transform: translateY(-3px); }
        .doctor-avatar {
            width: 64px; height: 64px;
            border-radius: 50%;
            background: linear-gradient(135deg, var(--color-primary-light), var(--color-muted));
            display: flex; align-items: center; justify-content: center;
            margin: 0 auto var(--space-3);
        }
        .doctor-avatar svg { width: 32px; height: 32px; color: var(--color-primary); }
        .doctor-name { font-family: var(--font-heading); font-weight: 600; font-size: 15px; margin-bottom: var(--space-1); }
        .doctor-contact { font-size: 12px; color: var(--color-muted-text); display: flex; align-items: center; justify-content: center; gap: 4px; }
    </style>
</head>
<body>
    <a href="#main-content" class="skip-link">Bỏ qua điều hướng</a>

    <!-- Navbar -->
    <nav class="navbar" aria-label="Điều hướng chính">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                <path d="M12 2C8.5 2 6 4.5 6 7c0 2 1 3.5 1.5 5C8 13.5 8 15 7 17c-1 2-.5 3 .5 4s2.5.5 3.5-1c.5-1 1-1 2-1s1.5 0 2 1c1 1.5 2.5 2 3.5 1s1.5-2 .5-4c-1-2-1-3.5-.5-5C19 10.5 20 9 20 7c0-2.5-2.5-5-6-5z"/>
                <path d="M9 7c0-1.7 1.3-3 3-3s3 1.3 3 3"/>
            </svg>
            Dental Clinic
        </a>
        <ul class="navbar-nav">
            <li><a class="nav-link active" href="${pageContext.request.contextPath}/" aria-current="page">Trang chủ</a></li>
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <li class="navbar-user">Xin chào, <strong>${sessionScope.user.fullName}</strong></li>
                    <c:if test="${sessionScope.role == 'ADMIN'}">
                        <li><a class="nav-link" href="${pageContext.request.contextPath}/admin">Quản trị</a></li>
                    </c:if>
                    <c:if test="${sessionScope.role == 'DOCTOR'}">
                        <li><a class="nav-link" href="${pageContext.request.contextPath}/doctor">Bác sĩ</a></li>
                    </c:if>
                    <c:if test="${sessionScope.role == 'STAFF'}">
                        <li><a class="nav-link" href="${pageContext.request.contextPath}/staff">Lễ tân</a></li>
                    </c:if>
                    <c:if test="${sessionScope.role == 'CUSTOMER'}">
                        <li><a class="nav-link" href="${pageContext.request.contextPath}/appointments">Lịch hẹn</a></li>
                        <li><a class="btn btn-accent btn-sm" href="${pageContext.request.contextPath}/booking">Đặt lịch</a></li>
                    </c:if>
                    <li><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/logout">Đăng xuất</a></li>
                </c:when>
                <c:otherwise>
                    <li><a class="nav-link" href="${pageContext.request.contextPath}/login.jsp">Đăng nhập</a></li>
                    <li><a class="btn btn-accent btn-sm" href="${pageContext.request.contextPath}/register.jsp">Đăng ký</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>

    <!-- Hero -->
    <section class="hero" aria-label="Giới thiệu">
        <span class="hero-icon" aria-hidden="true">🦷</span>
        <h1>Phòng Khám Nha Khoa</h1>
        <p>Chăm sóc sức khỏe răng miệng toàn diện — Đội ngũ bác sĩ chuyên nghiệp, tận tâm, hiện đại.</p>
        <div class="hero-actions">
            <c:choose>
                <c:when test="${sessionScope.role == 'CUSTOMER'}">
                    <a href="${pageContext.request.contextPath}/booking" class="btn btn-accent btn-lg">Đặt lịch khám ngay</a>
                </c:when>
                <c:when test="${empty sessionScope.user}">
                    <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-accent btn-lg">Đăng ký khám</a>
                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-outline btn-lg">Đăng nhập</a>
                </c:when>
            </c:choose>
        </div>
    </section>

    <!-- Clinic info bar -->
    <div class="clinic-info-bar">
        <div class="info-item">
            <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" aria-hidden="true"><circle cx="12" cy="12" r="10"/><path d="M12 6v6l4 2"/></svg>
            <span>Giờ mở cửa: <strong>
                <c:choose>
                    <c:when test="${not empty clinicConfig}">
                        <fmt:formatDate value="${clinicConfig.openingTime}" pattern="HH:mm"/> – <fmt:formatDate value="${clinicConfig.closingTime}" pattern="HH:mm"/>
                    </c:when>
                    <c:otherwise>08:00 – 20:00</c:otherwise>
                </c:choose>
            </strong></span>
        </div>
        <div class="info-item">
            <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M11 5.882V19.24a1.76 1.76 0 01-3.417.592l-2.147-6.15M18 13a3 3 0 100-6M5.436 13.683A4.001 4.001 0 017 6h1.832c4.1 0 7.625-1.234 9.168-3v14c-1.543-1.766-5.067-3-9.168-3H7a3.988 3.988 0 01-1.564-.317z"/></svg>
            <span><strong>
                <c:choose>
                    <c:when test="${not empty clinicConfig}">${clinicConfig.clinicInfo}</c:when>
                    <c:otherwise>Đang cập nhật thông tin...</c:otherwise>
                </c:choose>
            </strong></span>
        </div>
    </div>

    <!-- Main content -->
    <main id="main-content" class="container page-content">
        <!-- Search -->
        <form class="search-bar" action="" method="get" role="search" aria-label="Tìm kiếm dịch vụ và bác sĩ">
            <input type="search" name="keyword" placeholder="Tìm dịch vụ hoặc bác sĩ..." 
                   value="${keyword}" aria-label="Từ khóa tìm kiếm">
            <button type="submit" class="btn btn-primary">Tìm</button>
        </form>

        <div class="grid-2">
            <!-- Services -->
            <div class="card section">
                <div class="card-header">
                    <h2 class="section-title" style="margin-bottom:0">Dịch Vụ Của Chúng Tôi</h2>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${not empty listDichVu}">
                            <c:forEach var="dv" items="${listDichVu}">
                                <div class="service-item">
                                    <div>
                                        <div class="service-name">${dv.serviceName}</div>
                                        <div class="service-desc">${dv.description}</div>
                                    </div>
                                    <div>
                                        <div class="service-price"><fmt:formatNumber value="${dv.price}" type="currency" currencySymbol="VNĐ"/></div>
                                        <div class="service-duration">${dv.durationMinutes != null ? dv.durationMinutes : 'N/A'} phút</div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state"><p>Hiện tại chưa có dịch vụ nào.</p></div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Doctors -->
            <div class="section">
                <h2 class="section-title">Đội Ngũ Bác Sĩ</h2>
                <c:choose>
                    <c:when test="${not empty listBacSi}">
                        <div class="grid-2" style="grid-template-columns: repeat(auto-fill, minmax(160px,1fr))">
                            <c:forEach var="bs" items="${listBacSi}">
                                <div class="doctor-card">
                                    <div class="doctor-avatar" aria-hidden="true">
                                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5"><path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z"/></svg>
                                    </div>
                                    <div class="doctor-name">Bs. ${bs.fullName}</div>
                                    <div class="doctor-contact">
                                        <svg width="12" height="12" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/></svg>
                                        ${bs.phone}
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state"><p>Hiện tại chưa có thông tin bác sĩ.</p></div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>
</body>
</html>