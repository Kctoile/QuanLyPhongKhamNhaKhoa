<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="css/login.css"/>
<div class="login-page">

    <div class="login-box">

        <h2 class="login-title">Login</h2>

        <form class="login-form" action="${pageContext.request.contextPath}/LoginServlet" method="post">
            <label>Email</label>
            <input type="email" name="email" required /><br />

            <label>Password</label>
            <input type="password" name="password" required /><br />

            <button type="submit">Login</button>
        </form>

        <p class="login-register">
            Don't have an account?
            <a href="${pageContext.request.contextPath}/register.jsp">Register</a>
        </p>

        <p class="login-home">
            <a href="${pageContext.request.contextPath}/">Home</a>
        </p>

        <p class="login-error">${error}</p>

    </div>

</div>